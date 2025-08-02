let permissions: { [key: number]: boolean } = {};
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
                                                                    tag: 'label',
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

interface User {
    id: number;
    name: string;
    idpID: string;
}

// code
function search() {
    let username: string = (edom.findById('inputUserName') as edomInputElement)
        .value;
    let backend = 'http://localhost:8080';

    fetch(`${backend}/api/users/search?q=${username}`, {
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
        .then((users: User[]) => {
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
                    ],
                },
            ];

            let counter: number = 0;

            users.forEach((user: User) => {
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
        });
}

interface Permission {
    id: number;
    name: string;
    internalName: string;
}

function populateDetails(userID: string) {
    let backend = 'http://localhost:8080';
    fetch(`${backend}/api/users/${userID}`, {
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
        .then((data: User) => {
            (edom.findById('inputName') as edomInputElement).setText(data.name);
            edom.findById('outputID')?.setText(data.id.toString());

            edom.findById('berechtigungen0')?.clear();
            edom.findById('berechtigungen1')?.clear();

            permissions = {};

            let columns: obj[][] = [
                [], // left
                [], // right
            ];

            Promise.all([
                loadAllPermissions(),
                loadUserPermissions(data.id),
            ]).then(([allPermissions, userPermissions]: Permission[][]) => {
                allPermissions.forEach(
                    (permission: Permission, index: number) => {
                        const hasPermission: boolean =
                            userPermissions.find(
                                (userPermission: Permission) =>
                                    permission.id === userPermission.id
                            ) !== undefined;
                        permissions[permission.id] = hasPermission;
                        columns[index % 2].push(
                            {
                                tag: 'input',
                                type: 'checkbox',
                                id: `cb${permission.id}`,
                                state: hasPermission,
                                handler: [
                                    {
                                        type: 'click',
                                        id: 'clickChangeState',
                                        arguments: '',
                                        body: `permissions[${permission.id}] = !permissions[${permission.id}]`,
                                    },
                                ],
                            },
                            {
                                tag: 'label',
                                for: `cb${permission.id}`,
                                text: permission.name,
                            },
                            {
                                tag: 'br',
                            }
                        );
                    }
                );
                edom.fromTemplate(columns[0], edom.findById('berechtigungen0'));
                edom.fromTemplate(columns[1], edom.findById('berechtigungen1'));
            });

            edom.findById('bttnSave')?.deleteClick('clickSave');
            edom.findById('bttnSave')?.addClick(
                'clickSave',
                (_self: edomElement) => {
                    saveDetails(data.id);
                }
            );

            edom.findById('bttnDelete')?.deleteClick('clickDelete');
            edom.findById('bttnDelete')?.addClick(
                'clickDelete',
                (self: edomElement) => {
                    // deleteUser(userID, data.userdata.benutzername);
                }
            );
        });
    window.scrollTo(0, 0);
}

function loadAllPermissions(): Promise<Permission[]> {
    let backend = 'http://localhost:8080';
    return new Promise((resolve, reject) => {
        fetch(`${backend}/api/permissions/`, {
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
            .then((permissions: Permission[]) => resolve(permissions))
            .catch((error: any) => {
                console.error('Error fetching student:', error);
                reject(error);
            });
    });
}

function loadUserPermissions(userId: number): Promise<Permission[]> {
    let backend = 'http://localhost:8080';
    return new Promise((resolve, reject) => {
        fetch(`${backend}/api/permissions/user/${userId}`, {
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
            .then((permissions: Permission[]) => resolve(permissions))
            .catch((error: any) => {
                console.error('Error fetching student:', error);
                reject(error);
            });
    });
}

function saveDetails(ID: number) {
    let backend = 'http://localhost:8080';
    fetch(`${backend}/api/users/${ID}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
        },
        body: JSON.stringify({
            permissionIds: Object.keys(permissions)
                .filter((key: string) => permissions[parseInt(key)])
                .map((key: string) => parseInt(key)),
        }),
    })
        .then((response: Response) => {
            if (!response.ok) {
                throw new Error(
                    `HTTP ${response.status} ${
                        response.statusText
                    } - ${response.text()}`
                );
            }
            edom.findById('bttnSave')?.removeStyle('fa', 'fa-spinner');
            edom.findById('bttnSave')?.setText('speichern');
            clearDetails();
        })
        .catch((error: any) => {
            console.error('Error fetching student:', error);
            edom.findById('bttnSave')?.swapStyle('fa-spinner', 'fa-times');
        });
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
    (edom.findById('inputName') as edomInputElement).setText('');
    edom.findById('outputUsername')?.setText('');
    edom.findById('outputID')?.setText('');

    edom.findById('berechtigungen0')?.clear();
    edom.findById('berechtigungen1')?.clear();

    edom.findById('bttnSave')?.deleteClick('clickSave');
    edom.findById('bttnDelete')?.deleteClick('clickDelete');
}
