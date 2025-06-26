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
                                                                    text: 'accountID:',
                                                                },
                                                            ],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'lable',
                                                                    id: 'outputID',
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
                                                                    id: 'inputKlasse',
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
                                                                    id: 'inputTelefon',
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
                                                                    id: 'selectFach',
                                                                    classes: [
                                                                        'outputDetails',
                                                                    ],
                                                                    children: [
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
                                                                    id: 'selectArt',
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
                                                                    id: 'selectTyp',
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

function searchStudents() {
    let username: string = (edom.findById('inputUserName') as edomInputElement)
        .value;

    $.post(
        '/shs/admin/searchUser',
        {
            username: username,
        },
        (data: obj) => {
            edom.findById('outputSearch')?.clear();

            let rows: obj[] = [
                {
                    tag: 'tr',
                    classes: ['tableHead'],
                    children: [
                        {
                            tag: 'td',
                            text: 'ID',
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

            data.userlist.forEach((user: obj) => {
                rows.push({
                    tag: 'tr',
                    classes: ['outputTable', `line${counter % 2}`],
                    children: [
                        {
                            tag: 'td',
                            text: user.id,
                        },
                        {
                            tag: 'td',
                            text: user.name,
                        },
                        {
                            tag: 'td',
                            text: user.fach,
                        },
                    ],
                    handler: [
                        {
                            type: 'click',
                            id: 'clickOpenDetails',
                            arguments: '',
                            body: `populateDetails("${user.id}")`,
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
        }
    );
}

function populateDetails(userID: string) {
    $.get('/shs/admin/userdata/' + userID, (data: obj) => {
        const inputs: string[] = [
            'inputName',
            'inputKlasse',
            'inputMail',
            'inputTelefon',
        ];
        const keys: string[] = ['name', 'klasse', 'mail', 'telefon'];
        edom.findById('outputID')?.setText(data.userdata.accountID);

        inputs.forEach((val: string, index: number) => {
            (edom.findById(val) as edomInputElement).setContent(
                data.userdata[keys[index]]
            );
        });

        (edom.findById('selectFach')?.element as HTMLSelectElement).value =
            data.userdata.fach;

        (edom.findById('selectArt')?.element as HTMLSelectElement).value =
            data.userdata.einzelnachhilfe == '0'
                ? 'Einzelnachhilfe'
                : 'Gruppennachhilfe';

        (edom.findById('selectTyp')?.element as HTMLSelectElement).value =
            data.userdata.nachhilfe == '0' ? 'Schüler*in' : 'Lehrer*in';

        edom.findById('bttnSave')?.deleteClick('clickSave');
        edom.findById('bttnSave')?.addClick(
            'clickSave',
            (self: edomElement) => {
                saveDetails(userID);
            }
        );

        edom.findById('bttnDelete')?.deleteClick('clickDelete');
        edom.findById('bttnDelete')?.addClick(
            'clickDelete',
            (self: edomElement) => {
                deleteUser(userID, data.userdata.name);
            }
        );
    });
    window.scrollTo(0, 0);
}

function saveDetails(userID: string) {
    const userdata: obj = {};

    const inputs: string[] = [
        'inputName',
        'inputKlasse',
        'inputMail',
        'inputTelefon',
    ];
    const keys: string[] = ['name', 'klasse', 'mail', 'telefon'];

    inputs.forEach((val: string, index: number) => {
        userdata[keys[index]] = (edom.findById(val) as edomInputElement).value;
    });

    userdata['fach'] = (
        edom.findById('selectFach')?.element as HTMLSelectElement
    ).value;

    userdata['einzelnachhilfe'] =
        (edom.findById('selectArt')?.element as HTMLSelectElement).value ==
        'Einzelnachhilfe'
            ? '0'
            : '1';

    userdata['nachhilfe'] =
        (edom.findById('selectTyp')?.element as HTMLSelectElement).value ==
        'Schüler*in'
            ? '0'
            : '1';

    edom.findById('bttnSave')?.applyStyle('fa', 'fa-spinner');
    edom.findById('bttnSave')?.setText('');

    $.post(`/shs/admin/updateuser/${userID}`, userdata, (data: obj) => {
        if (data.success) {
            edom.findById('bttnSave')?.removeStyle('fa', 'fa-spinner');
            edom.findById('bttnSave')?.setText('speichern');
            clearDetails();
            searchStudents();
        } else {
            edom.findById('bttnSave')?.swapStyle('fa-spinner', 'fa-times');
        }
    });
}

function deleteUser(userID: string, name: string) {
    if (confirm(`Soll "${name}" wirklich gelöscht werden?`)) {
        edom.findById('bttnDelete')?.applyStyle('fa', 'fa-spinner');
        edom.findById('bttnDelete')?.setText('');

        $.get(`/shs/admin/deleteUser/${userID}`, (data: obj) => {
            if (data.success) {
                edom.findById('bttnDelete')?.removeStyle('fa', 'fa-spinner');
                edom.findById('bttnDelete')?.setText('löschen');
                clearDetails();
                searchStudents();
            } else {
                edom.findById('bttnDelete')?.swapStyle(
                    'fa-spinner',
                    'fa-times'
                );
            }
        });
    }
}

function clearDetails() {
    const inputs: string[] = [
        'inputName',
        'inputKlasse',
        'inputMail',
        'inputTelefon',
    ];
    edom.findById('outputID')?.setText('');

    inputs.forEach((val: string, index: number) => {
        (edom.findById(val) as edomInputElement).setContent('');
    });

    (edom.findById('selectFach')?.element as HTMLSelectElement).value = '';

    (edom.findById('selectArt')?.element as HTMLSelectElement).value = '';

    (edom.findById('selectTyp')?.element as HTMLSelectElement).value = '';
    edom.findById('bttnSave')?.deleteClick('clickSave');
    edom.findById('bttnDelete')?.deleteClick('clickDelete');
}
