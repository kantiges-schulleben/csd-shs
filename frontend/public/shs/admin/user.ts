export {};
function markupUser(): obj {
    return [
        {
            tag: 'table',
            children: [
                {
                    tag: 'tr',
                    children: [
                        {
                            tag: 'td',
                            id: 'search',
                            children: [
                                {
                                    tag: 'div',
                                    children: [
                                        {
                                            tag: 'input',
                                            id: 'inputUserName',
                                        },
                                        {
                                            tag: 'button',
                                            text: 'suchen',
                                            classes: ['searchButton'],
                                            handler: [
                                                {
                                                    type: 'click',
                                                    id: 'clickStartSearch',
                                                    arguments: '',
                                                    body: 'searchStudents()',
                                                },
                                            ],
                                        },
                                        {
                                            tag: 'div',
                                            id: 'outputSearch',
                                        },
                                    ],
                                },
                            ],
                        },
                        {
                            tag: 'td',
                            id: 'details',
                            children: [
                                {
                                    tag: 'div',
                                    classes: ['detailsContainer'],
                                    children: [
                                        {
                                            tag: 'table',
                                            children: [
                                                {
                                                    tag: 'tr',
                                                    children: [
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'lable',
                                                                    text: 'Name:',
                                                                },
                                                            ],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'input',
                                                                    id: 'inputName',
                                                                    classes: [
                                                                        'outputDetails',
                                                                    ],
                                                                },
                                                            ],
                                                        },
                                                    ],
                                                },
                                                {
                                                    tag: 'tr',
                                                    children: [
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'lable',
                                                                    text: 'Nachname:',
                                                                },
                                                            ],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'input',
                                                                    id: 'inputSureName',
                                                                    classes: [
                                                                        'outputDetails',
                                                                    ],
                                                                },
                                                            ],
                                                        },
                                                    ],
                                                },
                                                {
                                                    tag: 'tr',
                                                    children: [
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'lable',
                                                                    text: 'Klasse:',
                                                                },
                                                            ],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'input',
                                                                    id: 'inputGrade',
                                                                    type: 'number',
                                                                    classes: [
                                                                        'outputDetails',
                                                                    ],
                                                                },
                                                            ],
                                                        },
                                                    ],
                                                },
                                                {
                                                    tag: 'tr',
                                                    children: [
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'lable',
                                                                    text: 'E-Mail:',
                                                                },
                                                            ],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'input',
                                                                    id: 'inputMail',
                                                                    classes: [
                                                                        'outputDetails',
                                                                    ],
                                                                },
                                                            ],
                                                        },
                                                    ],
                                                },
                                                {
                                                    tag: 'tr',
                                                    children: [
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'lable',
                                                                    text: 'Telefon:',
                                                                },
                                                            ],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'input',
                                                                    id: 'inputPhone',
                                                                    classes: [
                                                                        'outputDetails',
                                                                    ],
                                                                },
                                                            ],
                                                        },
                                                    ],
                                                },
                                                {
                                                    tag: 'tr',
                                                    children: [
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'lable',
                                                                    text: 'Fach:',
                                                                },
                                                            ],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'select',
                                                                    id: 'selectSubject',
                                                                    classes: [
                                                                        'outputDetails',
                                                                    ],
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
                                                                    ].map(
                                                                        (
                                                                            fach: string
                                                                        ) => {
                                                                            return {
                                                                                tag: 'option',
                                                                                text: fach,
                                                                            };
                                                                        }
                                                                    ),
                                                                },
                                                            ],
                                                        },
                                                    ],
                                                },
                                                {
                                                    tag: 'tr',
                                                    children: [
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'lable',
                                                                    text: 'Art:',
                                                                },
                                                            ],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'select',
                                                                    id: 'selectIsGroup',
                                                                    classes: [
                                                                        'outputDetails',
                                                                    ],
                                                                    children: [
                                                                        '',
                                                                        'Einzelnachhilfe',
                                                                        'Gruppennachhilfe',
                                                                    ].map(
                                                                        (
                                                                            art: string
                                                                        ) => {
                                                                            return {
                                                                                tag: 'option',
                                                                                text: art,
                                                                            };
                                                                        }
                                                                    ),
                                                                },
                                                            ],
                                                        },
                                                    ],
                                                },
                                                {
                                                    tag: 'tr',
                                                    children: [
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'lable',
                                                                    text: 'Typ:',
                                                                },
                                                            ],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'select',
                                                                    id: 'selectIsTeacher',
                                                                    classes: [
                                                                        'outputDetails',
                                                                    ],
                                                                    children: [
                                                                        '',
                                                                        'Lehrer*in',
                                                                        'Schüler*in',
                                                                    ].map(
                                                                        (
                                                                            typ: string
                                                                        ) => {
                                                                            return {
                                                                                tag: 'option',
                                                                                text: typ,
                                                                            };
                                                                        }
                                                                    ),
                                                                },
                                                            ],
                                                        },
                                                    ],
                                                },
                                                {
                                                    tag: 'tr',
                                                    children: [
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'lable',
                                                                    text: 'geben für:',
                                                                },
                                                            ],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'input',
                                                                    id: 'inputTarget',
                                                                    type: 'number',
                                                                    classes: [
                                                                        'outputDetails',
                                                                    ],
                                                                },
                                                            ],
                                                        },
                                                    ],
                                                },
                                            ],
                                        },
                                        {
                                            tag: 'button',
                                            classes: ['saveButton'],
                                            text: 'speichern',
                                            id: 'bttnSave',
                                        },
                                        {
                                            tag: 'button',
                                            classes: ['deleteButton'],
                                            text: 'löschen',
                                            id: 'bttnDelete',
                                        },
                                    ],
                                },
                            ],
                        },
                    ],
                },
            ],
        },
    ];
}

interface EnrolledStudent {
  id: number;
  name: string;
  sureName: string;
  mail: string;
  targetGrade: number;
  subject: string;
  grade: number;
  phoneNumber: string;
  group: boolean;
  teacher: boolean;
};

function searchStudents() {
    let username: string = (edom.findById('inputUserName') as edomInputElement)
        .value;

    fetch(`${backend}/api/shs/admin/students/search?q=${username}`, {
      headers: {
        "Authorization": `Bearer ${localStorage.getItem("csd_token")}`
      }
    })
    .then((response: Response) => {
      if (!response.ok) {
        throw new Error(`HTTP ${response.status} ${response.statusText} - ${response.text()}`);
      }
      return response.json();
    })
    .then((data: EnrolledStudent[]) => {
      edom.findById('outputSearch')?.clear();

      let rows: obj[] = [
          {
              tag: 'tr',
              classes: ['tableHead'],
              children: [
                  {
                      tag: 'td',
                      text: '',
                  },
                  {
                      tag: 'td',
                      text: 'Name',
                  },
                  {
                      tag: 'td',
                      text: 'Fach',
                  },
              ],
          },
      ];

      let counter: number = 0;

      data.forEach((user: EnrolledStudent) => {
          rows.push({
              tag: 'tr',
              classes: ['outputTable', `line${counter % 2}`],
              children: [
                  {
                      tag: 'td',
                      text: user.teacher ? "Lehrer*in" : "Schüler*in",
                  },
                  {
                      tag: 'td',
                      text: user.name + " " + user.sureName,
                  },
                  {
                      tag: 'td',
                      text: user.subject,
                  },
              ],
              handler: [
                  {
                      type: 'click',
                      id: 'clickOpenDetails',
                      arguments: '',
                      body: `populateDetails(${JSON.stringify(user)})`,
                  },
              ],
          });
          counter++;
      });

      edom.fromTemplate(
          [
              {
                  tag: 'table',
                  children: rows,
              },
          ],
          edom.findById('outputSearch')
      );
    })
    .catch((e: any) => {
      console.error(e);
      (document.getElementById('counter') as HTMLLabelElement).innerHTML = "<i>Die Anzahl der angemeldeten Schüler*innen konnte nicht abgerufen werden</i>";
    });
}

function populateDetails(student: EnrolledStudent | obj) {
  const inputs: string[] = [
      'inputName',
      'inputSureName',
      'inputGrade',
      'inputMail',
      'inputPhone',
      'inputTarget',
  ];
  const keys: string[] = ['name', 'sureName', 'grade', 'mail', 'phoneNumber', 'targetGrade'];

  inputs.forEach((val: string, index: number) => {
      (edom.findById(val) as edomInputElement).setContent(
          (student as obj)[keys[index]]
      );
  });

  (edom.findById('selectSubject')?.element as HTMLSelectElement).value =
      student.subject;

  (edom.findById('selectIsGroup')?.element as HTMLSelectElement).value =
      student.group
          ? 'Gruppennachhilfe'
          : 'Einzelnachhilfe';

  (edom.findById('selectIsTeacher')?.element as HTMLSelectElement).value =
      student.teacher ? 'Lehrer*in' : "Schüler*in";

  edom.findById('bttnSave')?.deleteClick('clickSave');
  edom.findById('bttnSave')?.addClick(
      'clickSave',
      (self: edomElement) => {
          saveDetails(student.id);
      }
  );

  edom.findById('bttnDelete')?.deleteClick('clickDelete');
  edom.findById('bttnDelete')?.addClick(
      'clickDelete',
      (self: edomElement) => {
          deleteUser(student.id, `${student.name} ${student.sureName}`);
      }
  );
    window.scrollTo(0, 0);
}

function saveDetails(userID: string) {
    const userdata: obj = {};

    const inputs: string[] = [
        'inputName',
        'inputSureName',
        'inputGrade',
        'inputMail',
        'inputPhone',
        'inputTarget',
    ];
    const keys: string[] = ['name', 'sureName', 'grade', 'mail', 'phoneNumber', 'targetGrade'];


    inputs.forEach((val: string, index: number) => {
        userdata[keys[index]] = (edom.findById(val) as edomInputElement).value;
    });

    userdata['subject'] = (
        edom.findById('selectSubject')?.element as HTMLSelectElement
    ).value;

    userdata['isGroup'] =
        (edom.findById('selectIsGroup')?.element as HTMLSelectElement).value === 'Gruppennachhilfe';

    userdata['teacher'] =
        (edom.findById('selectIsTeacher')?.element as HTMLSelectElement).value === 'Lehrer*in';

    edom.findById('bttnSave')?.applyStyle('fa', 'fa-spinner');
    edom.findById('bttnSave')?.setText('');

    fetch(`${backend}/api/shs/admin/${userdata["teacher"] ? "teachers" : "students"}/id/${userID}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${localStorage.getItem("csd_token")}`
      },
      body: JSON.stringify(userdata)
    })
    .then((response: Response) => {
      if (!response.ok) {
        throw new Error(`HTTP ${response.status} ${response.statusText} - ${response.text()}`);
      }
      edom.findById('bttnSave')?.removeStyle('fa', 'fa-spinner');
      edom.findById('bttnSave')?.setText('speichern');
      clearDetails();
      searchStudents();
    })
    .catch((e: any) => {
      console.error(e);
      edom.findById('bttnSave')?.swapStyle('fa-spinner', 'fa-times');
    });
}

function deleteUser(userID: string, name: string) {
    if (confirm(`Soll "${name}" wirklich gelöscht werden?`)) {
        edom.findById('bttnDelete')?.applyStyle('fa', 'fa-spinner');
        edom.findById('bttnDelete')?.setText('');

        fetch(`${backend}/api/shs/admin/students/id/${userID}`, {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("csd_token")}`
          }
        })
        .then((response: Response) => {
          if (response.status !== 204) {
            throw new Error(`HTTP ${response.status} ${response.statusText} - ${response.text()}`);
          }
          edom.findById('bttnDelete')?.removeStyle('fa', 'fa-spinner');
          edom.findById('bttnDelete')?.setText('löschen');
          clearDetails();
          searchStudents();
        })
        .catch((e: any) => {
          console.error(e);
          edom.findById('bttnDelete')?.swapStyle(
              'fa-spinner',
              'fa-times'
          );
        });
    }
}

function clearDetails() {
    const inputs: string[] = [
        'inputName',
        'inputSureName',
        'inputGrade',
        'inputMail',
        'inputPhone',
        'inputTarget',
    ];
    edom.findById('outputID')?.setText('');

    inputs.forEach((val: string, index: number) => {
        (edom.findById(val) as edomInputElement).setContent('');
    });

    (edom.findById('selectSubject')?.element as HTMLSelectElement).value = '';

    (edom.findById('selectIsGroup')?.element as HTMLSelectElement).value = '';

    (edom.findById('selectIsTeacher')?.element as HTMLSelectElement).value = '';
    edom.findById('bttnSave')?.deleteClick('clickSave');
    edom.findById('bttnDelete')?.deleteClick('clickDelete');
}
