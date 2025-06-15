import { Application, Request, Response } from 'express';
import * as types from '../../types';
import { use } from '../moduleManager';
import * as path from 'path';
import { ChildProcessWithoutNullStreams, spawn } from 'child_process';
import { emailText } from './emailText';
import { encode } from 'html-entities';

let modulesPath: string = '';

export function config(
    server: Application,
    modules: types.obj,
    rootPath: string
) {
    modulesPath = rootPath;
    server.get('/shs/admin', (req: Request, res: Response) => {
        use('user', (user: types.obj) => {
            if (
                user.login.isLoggedIn(req) &&
                (user.session
                    .getSessionValue(req, 'berechtigung')
                    .includes('3') ||
                    user.session
                        .getSessionValue(req, 'berechtigung')
                        .includes('0'))
            ) {
                res.sendFile('admin.html', {
                    root: path.join(rootPath, 'shs', 'html'),
                });
            } else {
                res.redirect('/login');
            }
        });
    });

    server.get('/shs/admin/count', (req: Request, res: Response) => {
        use('user', (user: types.obj) => {
            if (
                user.login.isLoggedIn(req) &&
                (user.session
                    .getSessionValue(req, 'berechtigung')
                    .includes('3') ||
                    user.session
                        .getSessionValue(req, 'berechtigung')
                        .includes('0'))
            ) {
                use('databaseConnection', (database: types.obj) => {
                    database.query(
                        'SELECT COUNT(*) FROM shsAnmeldung',
                        ['empty'],
                        (err: boolean, result: types.obj) => {
                            if (err) {
                                res.json({ count: -1 });
                                return;
                            }

                            res.json({ count: result[0]['COUNT(*)'] });
                        }
                    );
                });
            } else {
                res.redirect('/login');
            }
        });
    });

    server.get('/shs/admin/start', (req: Request, res: Response) => {
        use('user', (user: types.obj) => {
            if (
                user.login.isLoggedIn(req) &&
                (user.session
                    .getSessionValue(req, 'berechtigung')
                    .includes('3') ||
                    user.session
                        .getSessionValue(req, 'berechtigung')
                        .includes('0'))
            ) {
                getStudentDataFromDB()
                    .then((studentData: types.obj) =>
                        runSortScript(studentData)
                    )
                    .then((sortedStudentData: types.obj) =>
                        sendMailsToStudents(sortedStudentData)
                    )
                    .then(() => {
                        res.json({ success: true });
                    })
                    .catch((err: any) => {
                      console.error(err);
                      res.json({success: false});
                    });
            } else {
                res.redirect('/login');
            }
        });
    });

    server.post('/shs/admin/searchUser', (req: Request, res: Response) => {
        use('user', (user: types.obj) => {
            if (
                !(
                    user.login.isLoggedIn(req) &&
                    (user.session
                        .getSessionValue(req, 'berechtigung')
                        .includes('3') ||
                        user.session
                            .getSessionValue(req, 'berechtigung')
                            .includes('0'))
                )
            ) {
                res.json({ userlist: [] });
                return;
            }

            const USERNAME: string = encode(req.body.username, {
                mode: 'specialChars',
                level: 'html5',
            });

            use('databaseConnection', (database: types.obj) => {
                database.query(
                    'SELECT fach, id, name FROM shsAnmeldung WHERE name LIKE ?;',
                    [`%${USERNAME}%`],
                    (err: boolean, result: types.obj) => {
                        if (err) {
                            res.json({ userlist: [] });
                            return;
                        }

                        res.json({ userlist: result });
                    }
                );
            });
        });
    });

    server.get('/shs/admin/userdata/:userID', (req: Request, res: Response) => {
        use('user', (user: types.obj) => {
            if (
                !(
                    user.login.isLoggedIn(req) &&
                    (user.session
                        .getSessionValue(req, 'berechtigung')
                        .includes('3') ||
                        user.session
                            .getSessionValue(req, 'berechtigung')
                            .includes('0'))
                )
            ) {
                res.json({ userlist: {} });
                return;
            }

            const USER_ID: string = encode(req.params.userID, {
                mode: 'specialChars',
                level: 'html5',
            });

            use('databaseConnection', (database: types.obj) => {
                database.query(
                    'SELECT name, mail, telefon, fach, einzelnachhilfe, nachhilfe, klasse, zielKlasse, accountID FROM shsAnmeldung WHERE id LIKE ?;',
                    [USER_ID],
                    (err: boolean, result: types.obj) => {
                        if (err || result.length === 0) {
                            res.json({ userdata: {} });
                            return;
                        }

                        res.json({ userdata: result[0] });
                    }
                );
            });
        });
    });

    server.get(
        '/shs/admin/deleteUser/:userID',
        (req: Request, res: Response) => {
            use('user', (user: types.obj) => {
                if (
                    !(
                        user.login.isLoggedIn(req) &&
                        (user.session
                            .getSessionValue(req, 'berechtigung')
                            .includes('3') ||
                            user.session
                                .getSessionValue(req, 'berechtigung')
                                .includes('0'))
                    )
                ) {
                    res.json({ success: false });
                    return;
                }

                const USER_ID: string = encode(req.params.userID, {
                    mode: 'specialChars',
                    level: 'html5',
                });

                use('databaseConnection', (database: types.obj) => {
                    database.query(
                        'DELETE FROM shsAnmeldung WHERE id = ?',
                        [USER_ID],
                        (err: boolean, result: types.obj) => {
                            if (err) {
                                res.json({ success: false });
                                return;
                            }

                            res.json({ success: result['affectedRows'] > 0 });
                        }
                    );
                });
            });
        }
    );

    server.post(
        '/shs/admin/updateUser/:userID',
        (req: Request, res: Response) => {
            use('user', (user: types.obj) => {
                if (
                    !(
                        user.login.isLoggedIn(req) &&
                        (user.session
                            .getSessionValue(req, 'berechtigung')
                            .includes('3') ||
                            user.session
                                .getSessionValue(req, 'berechtigung')
                                .includes('0'))
                    )
                ) {
                    res.json({ success: false });
                    return;
                }

                const dataKeys: string[] = [
                    'name',
                    'klasse',
                    'mail',
                    'telefon',
                    'fach',
                    'einzelnachhilfe',
                    'nachhilfe',
                ];

                const USER_ID: string = encode(req.params.userID, {
                    mode: 'specialChars',
                    level: 'html5',
                });

                use('databaseConnection', (database: types.obj) => {
                    database.query(
                        `UPDATE shsAnmeldung SET ${dataKeys.join(
                            '=?, '
                        )}=? WHERE id = ?`,
                        [
                            ...dataKeys.map((key: string) => {
                                return encode(req.body[key], {
                                    mode: 'specialChars',
                                    level: 'html5',
                                });
                            }),
                            USER_ID,
                        ],
                        (err: boolean, result: types.obj) => {
                            if (err) {
                                res.json({ success: false });
                                return;
                            }

                            res.json({ success: result['affectedRows'] > 0 });
                        }
                    );
                });
            });
        }
    );
}

function getStudentDataFromDB(): Promise<types.obj> {
    return new Promise<types.obj>((resolve) => {
        use('databaseConnection', (database: types.obj) => {
            database.query(
                'SELECT * FROM shsAnmeldung',
                ['empty'],
                (error: boolean, result: types.obj) => {
                    if (error) {
                        return;
                    }

                    let studentData: types.obj = [];
                    result.forEach((student: types.obj) => {
                        studentData.push({
                            name: student['name'],
                            mail: student['mail'],
                            klasse: student['klasse'],
                            telefon: student['telefon'],
                            nachhilfe: student['nachhilfe'],
                            facher: student['fach'], // falls irgendwann mehrere Fächer im FE ausgewählt werden können
                            zeit: student['zeit'],
                            einzelnachhilfe: student['einzelnachhilfe'],
                            accountID: student['accountID'],
                            Bemerkung: student['bemerkung'] || '', // ich geb's ja zu, ich bin dumm und faul
                            zielKlasse: student['zielKlasse'],
                        });
                    });
                    resolve(studentData);
                }
            );
        });
    });
}

function runSortScript(studentData: types.obj): Promise<types.obj> {
    return new Promise<types.obj>((resolve) => {
        console.log('starting script');
        const process: ChildProcessWithoutNullStreams = spawn('python3', [
            path.join(modulesPath, '/shs/script.py'),
            JSON.stringify(studentData),
        ]);

        let scriptOutput: string = '';

        process.stdout.on('data', (data: any) => {
            scriptOutput += data.toString();
        });
        process.on('close', (code: number) => {
            console.log('script done');
            resolve(
                code === 0
                    ? JSON.parse(scriptOutput)
                    : { einzel: [], gruppe: [], ohne: [] }
            );
        });
    });
}

function sendMailsToStudents(sortedStudentData: types.obj): Promise<boolean> {
    return new Promise<boolean>((resolve) => {
        let queries: string[] = ['DELETE FROM handschlag'];
        let queryData: string[][] = [['empty']];
        use('databaseConnection', (database: types.obj) => {
            sortedStudentData['einzel'].forEach((paar: types.obj) => {
                queries.push(
                    'INSERT INTO handschlag (fach, id, partner, klasse, typ) VALUES (?, ?, ?, ?, ?)'
                );
                queryData.push([
                    paar['facher'],
                    paar['accountID'],
                    paar['partner']['accountID'],
                    paar['klasse'],
                    'lehrer',
                ]);
                use("mail", (mail: types.obj) => {
                  mail.sendMail(
                    'Schüler*innen helfen Schüler*innen',
                    {
                      name: paar['name'],
                      mail: paar['mail'],
                    },
                    { name: 'ShS-Team', mail: 'shs@kantgym-leipzig.de' },
                    'Rückmeldung Partner*in',
                    emailText.get('einzel', 'lehrer', {
                      name: paar['name'],
                      namePartner: paar['partner']['name'],
                      fach: paar['facher'],
                      mailPartner: `<a href='mailto:${paar['partner']['mail']}'>${paar['partner']['mail']}</a>`,
                      telefonPartner: paar['partner']['telefon'],
                    })
                  );
                });

                queries.push(
                    'INSERT INTO handschlag (fach, id, partner, klasse, typ) VALUES (?, ?, ?, ?, ?)'
                );
                queryData.push([
                    paar['partner']['facher'],
                    paar['partner']['accountID'],
                    paar['accountID'],
                    paar['partner']['klasse'],
                    'schueler',
                ]);

                use("mail", (mail: types.obj) => {
                  mail.sendMail(
                    'Schüler*innen helfen Schüler*innen',
                    {
                      name: paar['partner']['name'],
                      mail: paar['partner']['mail'],
                    },
                    { name: 'ShS-Team', mail: 'shs@kantgym-leipzig.de' },
                    'Rückmeldung Partner*in',
                    emailText.get('einzel', 'schueler', {
                      name: paar['partner']['name'],
                      partner: paar['name'],
                      fach: paar['facher'],
                      mailPartner: `<a href='mailto:${paar['mail']}'>${paar['mail']}</a>`,
                      telefonPartner: paar['telefon'],
                    })
                  );
                });
            });

            sortedStudentData['gruppe'].forEach((paar: types.obj) => {
                queries.push(
                    'INSERT INTO handschlag (fach, id, partner, klasse, typ) VALUES (?, ?, ?, ?, ?)'
                );
                queryData.push([
                    paar['facher'],
                    paar['accountID'],
                    Object.keys(paar['partner'])
                        .map((partnerIndex: string) => {
                            return paar['partner'][partnerIndex]['accountID'];
                        })
                        .join(','),
                    paar['klasse'],
                    'lehrer',
                ]);

                use("mail", (mail: types.obj) => {
                  mail.sendMail(
                    'Schüler*innen helfen Schüler*innen',
                    {
                      name: paar['name'],
                      mail: paar['mail'],
                    },
                    { name: 'ShS-Team', mail: 'shs@kantgym-leipzig.de' },
                    'Rückmeldung Partner*in',
                    emailText.get('gruppe', 'lehrer', {
                      name: paar['name'],
                      fach: paar['facher'],
                      gruppenPartner: Object.keys(paar['partner'])
                      .map((partnerIndex: string) => {
                        return paar['partner'][partnerIndex]['name'];
                      })
                      .join(', '),
                      mailsStudents: Object.keys(paar['partner'])
                      .map((partnerIndex: string) => {
                        return `<a href="mailto:${paar['partner'][partnerIndex]['mail']}">${paar['partner'][partnerIndex]['mail']}</a>`;
                      })
                      .join(', '),
                      telefonStudents: Object.keys(paar['partner'])
                      .map((partnerIndex: string) => {
                        return paar['partner'][partnerIndex]['telefon'];
                      })
                      .join(', '),
                    })
                  );
                });

                Object.keys(paar['partner']).forEach((partnerIndex: string) => {
                    queries.push(
                        'INSERT INTO handschlag (fach, id, partner, klasse, typ) VALUES (?, ?, ?, ?, ?)'
                    );
                    queryData.push([
                        paar['partner'][partnerIndex]['facher'],
                        paar['partner'][partnerIndex]['accountID'],
                        paar['accountID'],
                        paar['partner'][partnerIndex]['klasse'],
                        'schueler',
                    ]);

                    use("mail", (mail: types.obj) => {
                      mail.sendMail(
                        'Schüler*innen helfen Schüler*innen',
                        {
                          name: paar['partner'][partnerIndex]['name'],
                          mail: paar['partner'][partnerIndex]['mail'],
                        },
                        { name: 'ShS-Team', mail: 'shs@kantgym-leipzig.de' },
                        'Rückmeldung Partner*in',
                        emailText.get('gruppe', 'schueler', {
                          name: paar['partner'][partnerIndex]['name'],
                          nameTeacher: paar['name'],
                          fach: paar['facher'],
                          mailTeacher: paar['mail'],
                          telefonTeacher: paar['telefon'],
                        })
                      );
                    });
                });
            });

            sortedStudentData['ohne'].forEach((looser: types.obj) => {
              use("mail", (mail: types.obj) => {
                mail.sendMail(
                  'Schüler*innen helfen Schüler*innen',
                  {
                    name: looser['name'],
                    mail: looser['mail'],
                  },
                  { name: 'ShS-Team', mail: 'shs@kantgym-leipzig.de' },
                  'Rückmeldung Partner*in',
                  emailText.get('ohne', 'ohne', {
                    name: looser['name'],
                  })
                );
              });
            });

            doBatchQueryStudents(queries, queryData, 0, database);
            resolve(true);
        });
    });
}

function doBatchQueryStudents(
    queries: string[],
    queryData: string[][],
    index: number,
    database: types.obj
) {
    if (index == queries.length) {
        return;
    }
    database.query(
        queries[index],
        queryData[index],
        (
            error: boolean,
            result: types.obj /*ignore both we are cowboys, YEEHA!*/
        ) => {
            doBatchQueryStudents(queries, queryData, ++index, database);
        }
    );
}
