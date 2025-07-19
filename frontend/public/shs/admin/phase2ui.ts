function renderPhase2Ui() {
    edom.fromTemplate(
        [
            {
                tag: 'div',
                children: [
                    {
                        tag: 'button',
                        text: 'SHS-Daten zurücksetzen',
                        classes: ['searchButton', 'dangerButton'],
                        handler: [
                            {
                                type: 'click',
                                id: 'clickResetData',
                                arguments: '',
                                body: 'resetToPhase1()',
                            },
                        ],
                    },
                ],
            },
            {
                tag: 'div',
                children: sectionSingle(),
            },
            {
                tag: 'div',
                children: sectionGroup(),
            },
            {
                tag: 'div',
                children: sectionWithout(),
            },
        ],
        edom.findById('content')!
    );
}

function sectionSingle(): edomObj[] {
    setTimeout(() => loadSingle(), 100);
    return [
        {
            tag: 'h1',
            text: 'Einzelnachhilfe:',
        },
        {
            tag: 'div',
            id: 'ouputSingle',
            classes: ['cardOutput'],
        },
    ];
}

function sectionGroup(): edomObj[] {
    setTimeout(() => loadGroup(), 100);
    return [
        {
            tag: 'h1',
            text: 'Gruppennachhilfe:',
        },
        {
            tag: 'div',
            id: 'ouputGroup',
            classes: ['cardOutput'],
        },
    ];
}

function sectionWithout(): edomObj[] {
    setTimeout(() => loadWithout(), 100);
    return [
        {
            tag: 'h1',
            text: 'ohne Partner*in:',
        },
        {
            tag: 'div',
            id: 'ouputWithout',
            classes: ['p2'],
        },
    ];
}

type Student = {
    id: number;
    name: String;
    sureName: String;
    mail: String;
    targetGrade: number;
    subject: String;
    grade: number;
    phoneNumber: String;
    group: boolean;
    teacher: boolean;
};

type Group = {
    teacher: Student;
    students: Student[];
    subject: String;
    released: boolean;
    id: number;
};

function loadSingle() {
    fetch(`${backend}/api/shs/admin/pairs/single`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
        },
    })
        .then((response: Response) => {
            if (!response.ok) {
                throw new Error(
                    `HTTP ${response.status} ${
                        response.statusText
                    } - ${response.text()}`
                );
            }
            return response.json();
        })
        .then((pairs: Group[]) => {
            const cards: edomObj[] = pairs.map((pair: Group) => {
                return {
                    tag: 'div',
                    classes: ['pairCard'],
                    children: [
                        {
                            tag: 'label',
                            text: 'Lehrer*in:',
                        },
                        {
                            tag: 'label',
                            text:
                                pair.teacher.name + ' ' + pair.teacher.sureName,
                        },
                        {
                            tag: 'label',
                            text: 'Schüler*in:',
                        },
                        {
                            tag: 'label',
                            text:
                                pair.students[0].name +
                                ' ' +
                                pair.students[0].sureName,
                        },
                        pair.released
                            ? { tag: 'div' }
                            : {
                                  tag: 'div',
                                  classes: ['buttonBar'],
                                  children: [
                                      {
                                          tag: 'button',
                                          text: 'verwerfen',
                                          classes: [
                                              'searchButton',
                                              'dangerButton',
                                          ],
                                      },
                                      {
                                          tag: 'button',
                                          text: 'freigeben',
                                          classes: ['searchButton'],
                                          handler: [
                                              {
                                                  type: 'click',
                                                  id: 'clickReleaseGroup',
                                                  arguments: '',
                                                  body: `releaseGroup(${pair.id})`,
                                              },
                                          ],
                                      },
                                  ],
                              },
                    ],
                };
            });

            edom.fromTemplate(cards, edom.findById('ouputSingle')!);
        })
        .catch((e: any) => {
            console.error(e);
        });
}

function loadGroup() {
    fetch(`${backend}/api/shs/admin/pairs/group`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
        },
    })
        .then((response: Response) => {
            if (!response.ok) {
                throw new Error(
                    `HTTP ${response.status} ${
                        response.statusText
                    } - ${response.text()}`
                );
            }
            return response.json();
        })
        .then((pairs: Group[]) => {
            const cards: edomObj[] = pairs.map((pair: Group) => {
                return {
                    tag: 'div',
                    classes: ['pairCard'],
                    children: [
                        {
                            tag: 'label',
                            text: 'Lehrer*in:',
                        },
                        {
                            tag: 'label',
                            text:
                                pair.teacher.name + ' ' + pair.teacher.sureName,
                        },
                        {
                            tag: 'label',
                            text: 'Schüler*innen:',
                        },
                        {
                            tag: 'ul',
                            children: pair.students.map((student: Student) => {
                                return {
                                    tag: 'li',
                                    text: student.name + ' ' + student.sureName,
                                };
                            }),
                        },
                        pair.released
                            ? { tag: 'div' }
                            : {
                                  tag: 'div',
                                  classes: ['buttonBar'],
                                  children: [
                                      {
                                          tag: 'button',
                                          text: 'verwerfen',
                                          classes: [
                                              'searchButton',
                                              'dangerButton',
                                          ],
                                      },
                                      {
                                          tag: 'button',
                                          text: 'freigeben',
                                          classes: ['searchButton'],
                                          handler: [
                                              {
                                                  type: 'click',
                                                  id: 'clickReleaseGroup',
                                                  arguments: '',
                                                  body: `releaseGroup(${pair.id})`,
                                              },
                                          ],
                                      },
                                  ],
                              },
                    ],
                };
            });

            edom.fromTemplate(cards, edom.findById('ouputGroup')!);
        })
        .catch((e: any) => {
            console.error(e);
        });
}

function loadWithout() {
    fetch(`${backend}/api/shs/admin/pairs/without`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
        },
    })
        .then((response: Response) => {
            if (!response.ok) {
                throw new Error(
                    `HTTP ${response.status} ${
                        response.statusText
                    } - ${response.text()}`
                );
            }
            return response.json();
        })
        .then((without: Student[]) => {
            const students: Student[] = without.filter(
                (o: Student) => !o.teacher
            );
            const teachers: Student[] = without.filter(
                (o: Student) => o.teacher
            );
            const tableData: String[][] = Array.from(
                { length: Math.max(students.length, teachers.length) },
                (_) => ['', '']
            );

            for (let i = 0; i < students.length; i++) {
                const tmp = tableData[i];
                tmp[1] = students[i].name + ' ' + students[i].sureName;
                tableData[i] = tmp;
            }

            for (let i = 0; i < teachers.length; i++) {
                const tmp = tableData[i];
                tmp[0] = teachers[i].name + ' ' + teachers[i].sureName;
                tableData[i] = tmp;
            }

            edom.fromTemplate(
                [
                    {
                        tag: 'button',
                        text: 'verknüpfen',
                        classes: ['searchButton'],
                    },
                    {
                        tag: 'table',
                        children: [
                            {
                                tag: 'tr',
                                children: [
                                    {
                                        tag: 'th',
                                        text: 'Lehrer*innen',
                                    },
                                    {
                                        tag: 'th',
                                        text: 'Schüler*innen',
                                    },
                                ],
                            },
                            ...tableData.map((names: String[]) => {
                                return {
                                    tag: 'tr',
                                    children: [
                                        {
                                            tag: 'td',
                                            text: names[0],
                                        },
                                        {
                                            tag: 'td',
                                            text: names[1],
                                        },
                                    ],
                                };
                            }),
                        ],
                    },
                ],
                edom.findById('ouputWithout')!
            );
        })
        .catch((e: any) => {
            console.error(e);
        });
}

function resetToPhase1() {
    fetch(`${backend}/api/shs/admin/reset`, {
        method: 'PUT',
        headers: {
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
        },
    })
        .then((response: Response) => {
            if (response.status === 200) {
                location.reload();
            } else {
                alert('Beim Zurücksetzen ist ein Fehler aufgetreten.');
            }
        })
        .catch((e: any) => {
            console.error(e);
            alert('Beim Zurücksetzen ist ein Fehler aufgetreten.');
        });
}

function releaseGroup(id: number) {
    fetch(`${backend}/api/shs/admin/pairs/id/${id}/release`, {
        method: 'PUT',
        headers: {
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
        },
    })
        .then((response: Response) => {
            if (response.status === 200) {
                location.reload();
            } else {
                alert('Beim Freigeben ist ein Fehler aufgetreten.');
            }
        })
        .catch((e: any) => {
            console.error(e);
            alert('Beim Freigeben ist ein Fehler aufgetreten.');
        });
}
