// markup
function muUser(): obj {
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
                                                    body: 'search()',
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
                                                                    text: 'Username:',
                                                                },
                                                            ],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            children: [
                                                                {
                                                                    tag: 'lable',
                                                                    id: 'outputUsername',
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
                                                                    text: 'ID:',
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
                                                                    text: 'Mail:',
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
                                                            id: 'berechtigungen0',
                                                            children: [],
                                                        },
                                                        {
                                                            tag: 'td',
                                                            id: 'berechtigungen1',
                                                            children: [],
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

// =======================================================================================

// code
function search() {
    let username: string = (edom.findById('inputUserName') as edomInputElement)
        .value;

    $.post(
        '/searchUser',
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
                            text: 'Benutzer*innenname',
                        },
                        {
                            tag: 'td',
                            text: 'Name',
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
                            text: user.benutzername,
                        },
                        {
                            tag: 'td',
                            text: user.name,
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
    const outputs: string[] = ['outputName', 'outputUsername', 'outputID'];
    const inputs: string[] = ['inputMail'];
    $.get('/userdata/' + userID, (data: obj) => {
        (edom.findById('inputName') as edomInputElement).setContent(
            data.userdata.name
        );
        edom.findById('outputUsername')?.setText(data.userdata.benutzername);
        edom.findById('outputID')?.setText(data.userdata.id);
        (edom.findById('inputMail') as edomInputElement).setContent(
            data.userdata.mail
        );

        edom.findById('berechtigungen0')?.clear();
        edom.findById('berechtigungen1')?.clear();

        const berechtigungen: string[] =
            data.userdata.berechtigung !== null
                ? data.userdata.berechtigung.split(',')
                : ''.split(',');

        let columns: obj[][] = [
            [], // left
            [], // right
        ];

        let counter: number = 0;

        Object.keys(data.berechtigungen).forEach((index: string) => {
            /*
            <label>
                <input type="checkbox" name="checkbox" value="value">
                Text
            </label>
            */
            columns[counter % 2].push(
                {
                    tag: 'input',
                    type: 'checkbox',
                    id: `cb${data.berechtigungen[index]}`,
                    state: berechtigungen.includes(index),
                },
                {
                    tag: 'label',
                    for: `cb${data.berechtigungen[index]}`,
                    text: data.berechtigungen[index],
                },
                {
                    tag: 'br',
                }
            );

            counter++;
        });

        edom.fromTemplate(columns[0], edom.findById('berechtigungen0'));
        edom.fromTemplate(columns[1], edom.findById('berechtigungen1'));

        edom.findById('bttnSave')?.deleteClick('clickSave');
        edom.findById('bttnSave')?.addClick(
            'clickSave',
            (self: edomElement) => {
                saveDetails(data.berechtigungen, userID);
            }
        );

        edom.findById('bttnDelete')?.deleteClick('clickDelete');
        edom.findById('bttnDelete')?.addClick(
            'clickDelete',
            (self: edomElement) => {
                deleteUser(userID, data.userdata.benutzername);
            }
        );
    });
    window.scrollTo(0, 0);
}

function saveDetails(berechtigungen: obj, ID: string) {
    const mail: string = (edom.findById('inputMail') as edomInputElement).value;
    const name: string = (edom.findById('inputName') as edomInputElement).value;
    let userBerechtigungen: string = '';

    Object.keys(berechtigungen).forEach((index: string) => {
        if (
            (edom.findById(`cb${berechtigungen[index]}`) as edomInputElement)
                .state
        ) {
            userBerechtigungen += index.toString() + ',';
        }
    });

    console.log('ID', ID);
    console.log('mail', mail);
    console.log('name', name);
    console.log('berechtigungen:', berechtigungen);
    console.log('userBerechtigungen:', userBerechtigungen);

    edom.findById('bttnSave')?.applyStyle('fa', 'fa-spinner');
    edom.findById('bttnSave')?.setText('');

    $.post(
        `/updateuser/${ID}`,
        {
            mail: mail,
            name: name,
            berechtigungen: userBerechtigungen,
        },
        (data: obj) => {
            if (data.success) {
                edom.findById('bttnSave')?.removeStyle('fa', 'fa-spinner');
                edom.findById('bttnSave')?.setText('speichern');
                clearDetails();
            } else {
                edom.findById('bttnSave')?.swapStyle('fa-spinner', 'fa-times');
            }
        }
    );
}

function deleteUser(ID: string, username: string) {
    if (confirm(`Soll "${username}" wirklich gelöscht werden?`)) {
        edom.findById('bttnDelete')?.applyStyle('fa', 'fa-spinner');
        edom.findById('bttnDelete')?.setText('');

        $.get(`/deleteUser/${ID}`, (data: obj) => {
            if (data.success) {
                edom.findById('bttnDelete')?.removeStyle('fa', 'fa-spinner');
                edom.findById('bttnDelete')?.setText('löschen');
                clearDetails();
                search();
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
    (edom.findById('inputName') as edomInputElement).setContent('');
    edom.findById('outputUsername')?.setText('');
    edom.findById('outputID')?.setText('');
    (edom.findById('inputMail') as edomInputElement).setContent('');

    edom.findById('berechtigungen0')?.clear();
    edom.findById('berechtigungen1')?.clear();

    edom.findById('bttnSave')?.deleteClick('clickSave');
    edom.findById('bttnDelete')?.deleteClick('clickDelete');
}
