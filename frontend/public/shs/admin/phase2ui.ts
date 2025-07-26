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
                            text: 'Fach:',
                        },
                        {
                            tag: 'label',
                            text: pair.subject,
                        },
                        {
                            tag: 'label',
                            text: 'JT:',
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
                                          handler: [
                                              {
                                                  type: 'click',
                                                  id: 'clickDeleteGroup',
                                                  arguments: '',
                                                  body: `deleteGroup(${pair.id})`,
                                              },
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
                            text: 'Fach:',
                        },
                        {
                            tag: 'label',
                            text: pair.subject,
                        },
                        {
                            tag: 'label',
                            text: 'JT:',
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
                                          handler: [
                                              {
                                                  type: 'click',
                                                  id: 'clickDeleteGroup',
                                                  arguments: '',
                                                  body: `deleteGroup(${pair.id})`,
                                              },
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
            const tableData: String[][][] = Array.from(
                { length: Math.max(students.length, teachers.length) },
                (_) => [
                    ['', ''],
                    ['', ''],
                ]
            );

            for (let i = 0; i < students.length; i++) {
                const tmp = tableData[i];
                tmp[1] = [
                    students[i].name + ' ' + students[i].sureName,
                    students[i].subject,
                ];
                tableData[i] = tmp;
            }

            for (let i = 0; i < teachers.length; i++) {
                const tmp = tableData[i];
                tmp[0] = [
                    teachers[i].name + ' ' + teachers[i].sureName,
                    teachers[i].subject,
                ];
                tableData[i] = tmp;
            }

            edom.fromTemplate(
                [
                    {
                        tag: 'button',
                        text: 'verknüpfen',
                        classes: ['searchButton'],
                        handler: [
                            {
                                type: 'click',
                                id: 'clickOpenPopup',
                                arguments: '',
                                body: 'openConnectPopup()',
                            },
                        ],
                    },
                    {
                        tag: 'table',
                        children: [
                            {
                                tag: 'tr',
                                children: [
                                    {
                                        tag: 'th',
                                        text: 'JT',
                                    },
                                    {
                                        tag: 'th',
                                        text: 'Schüler*innen',
                                    },
                                ],
                            },
                            ...tableData.map((names: String[][]) => {
                                return {
                                    tag: 'tr',
                                    children: [
                                        {
                                            tag: 'td',
                                            text:
                                                names[0][0] === ''
                                                    ? ''
                                                    : `${names[0][0]} (${names[0][1]})`,
                                        },
                                        {
                                            tag: 'td',
                                            text:
                                                names[1][0] === ''
                                                    ? ''
                                                    : `${names[1][0]} (${names[1][1]})`,
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
                reloadPhase2Ui();
            } else {
                alert('Beim Freigeben ist ein Fehler aufgetreten.');
            }
        })
        .catch((e: any) => {
            console.error(e);
            alert('Beim Freigeben ist ein Fehler aufgetreten.');
        });
}

function deleteGroup(id: number) {
    fetch(`${backend}/api/shs/admin/pairs/id/${id}`, {
        method: 'DELETE',
        headers: {
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
        },
    })
        .then((response: Response) => {
            if (response.status === 204) {
                reloadPhase2Ui();
            } else {
                alert('Beim Löschen ist ein Fehler aufgetreten.');
            }
        })
        .catch((e: any) => {
            console.error(e);
            alert('Beim Löschen ist ein Fehler aufgetreten.');
        });
}

function reloadPhase2Ui() {
    const content: edomElement = edom.findById('content')!;
    while (content.children.length > 0) {
        content.children[0].delete();
    }

    renderPhase2Ui();
}

function openConnectPopup() {
    popup('', {
        tag: 'div',
        children: [
            {
                tag: 'div',
                classes: ['popupConnectBody'],
                id: 'actualContent',
                children: [
                    {
                        tag: 'p',
                        text: 'Fach:',
                    },
                    {
                        tag: 'select',
                        classes: ['outputDetails'],
                        id: 'selectSubjectConnect',
                        children: [
                            // TODO use file for constant
                            '',
                            'Deutsch',
                            'Englisch',
                            'Französisch',
                            'Russisch',
                            'naturwissenschaftliche Profil',
                            'künstlerisches Profil',
                            'gesellschaftliches Profil',
                            'Mathe',
                            'Informatik',
                            'Biologie',
                            'Chemie',
                            'Physik',
                            'Geschichte',
                            'Geografie',
                            'Ethik',
                            'Religion',
                            'Kunst',
                            'Musik',
                            'Technik und Computer',
                            'GRW',
                            'Bionik',
                            'Philosophie',
                        ].map((fach: string) => {
                            return {
                                tag: 'option',
                                text: fach,
                            };
                        }),
                        handler: [
                            {
                                type: 'change',
                                id: 'changeLoadStudentsAndTeachers',
                                arguments: 'self',
                                body: 'loadUsers(self)',
                            },
                        ],
                    },
                    {
                        tag: 'p',
                        text: 'JT:',
                    },
                    {
                        tag: 'select',
                        classes: ['outputDetails'],
                        id: 'selectTeacherConnect',
                    },
                    {
                        tag: 'p',
                        text: 'Schüler*in:',
                    },
                    {
                        tag: 'select',
                        classes: ['outputDetails'],
                        id: 'selectStudentConnect',
                    },
                ],
            },
            {
                tag: 'div',
                children: [
                    {
                        tag: 'button',
                        classes: ['searchButton', 'dangerButton'],
                        text: 'abbrechen',
                        handler: [
                            {
                                type: 'click',
                                id: 'clickClosePopup',
                                arguments: 'self',
                                body: 'closePopup(self)',
                            },
                        ],
                    },
                    {
                        tag: 'button',
                        classes: ['searchButton'],
                        text: 'ok',
                        handler: [
                            {
                                type: 'click',
                                id: 'clickSaveGroup',
                                arguments: 'self',
                                body: 'saveGroup(self)',
                            },
                        ],
                    },
                ],
            },
        ],
    });
}

function loadUsers(self: edomElement) {
    const subject: string = (self.element as HTMLSelectElement).value;
    clearSelects();

    loadStudentsForSubject(subject);
    loadTeachersForSubject(subject);
}

function clearSelects() {
    const selTeacher: edomElement = edom.findById('selectTeacherConnect')!;
    const selStudents: edomElement = edom.findById('selectStudentConnect')!;
    (selTeacher.element as HTMLSelectElement).innerHTML = '';
    (selStudents.element as HTMLSelectElement).innerHTML = '';

    (selTeacher.element as HTMLSelectElement).value = '';
    (selStudents.element as HTMLSelectElement).value = '';
}

function loadStudentsForSubject(subject: string) {
    fetch(`${backend}/api/shs/admin/students/by-subject?s=${subject}`, {
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
        .then((students: Student[]) => {
            const selStudents: edomElement = edom.findById(
                'selectStudentConnect'
            )!;

            students.forEach((student: Student) => {
                const option: edomElement = edom.newElement('option');
                option.setText(student.name + ' ' + student.sureName);
                (option.element as HTMLOptionElement).value =
                    student.id.toString();

                selStudents.addChild(option);
            });
        })
        .catch((e: any) => {
            console.error(e);
        });
}

function loadTeachersForSubject(subject: string) {
    fetch(`${backend}/api/shs/admin/teachers/by-subject?s=${subject}`, {
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
        .then((teachers: Student[]) => {
            const selTeachers: edomElement = edom.findById(
                'selectTeacherConnect'
            )!;

            teachers.forEach((student: Student) => {
                const option: edomElement = edom.newElement('option');
                option.setText(student.name + ' ' + student.sureName);
                (option.element as HTMLOptionElement).value =
                    student.id.toString();

                selTeachers.addChild(option);
            });
        })
        .catch((e: any) => {
            console.error(e);
        });
}

function saveGroup(self: edomElement) {
    const selTeacher: edomElement = edom.findById('selectTeacherConnect')!;
    const selStudent: edomElement = edom.findById('selectStudentConnect')!;
    const selSubject: edomElement = edom.findById('selectSubjectConnect')!;

    const subject: string = (selSubject.element as HTMLSelectElement).value;
    const studentId: string = (selStudent.element as HTMLSelectElement).value;
    const teacherId: string = (selTeacher.element as HTMLSelectElement).value;

    fetch(`${backend}/api/shs/admin/pairs`, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            teacherId: parseInt(teacherId),
            studentId: parseInt(studentId),
            subject: subject,
        }),
    })
        .then((response: Response) => {
            closePopup(self);
            if (!response.ok) {
                throw new Error(
                    `HTTP ${response.status} ${
                        response.statusText
                    } - ${response.text()}`
                );
            }
            reloadPhase2Ui();
        })
        .catch((e: any) => {
            console.error(e);
        });
}
