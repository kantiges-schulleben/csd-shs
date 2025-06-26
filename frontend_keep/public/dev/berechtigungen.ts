// markup
function muBerechtigungen(): obj {
    return [
        {
            tag: 'button',
            classes: ['fa', 'fa-plus'],
            handler: [
                {
                    type: 'click',
                    id: 'clickAddNew',
                    arguments: '',
                    body: 'newBerechtigung()',
                },
            ],
        },
        {
            tag: 'ul',
            id: 'listBerechtigungen',
        },
    ];
}

// =======================================================================================

// code
function getBerechtigungen() {
    $.get('/berechtigungen/getall', (data: obj) => {
        data['berechtigungen'].forEach((berechtigung: obj) => {
            (edom.findById('listBerechtigungen') as edomListElement).addEntry(
                berechtigung.id + ' - ' + berechtigung.name
            );
        });
    });
}

function newBerechtigung() {
    let name: string | null = prompt('Name der neuen Berechtigung:');

    if (name !== null) {
        name = name.replace(' ', '');

        $.get('/berechtigungen/getall', (data: obj) => {
            let doesExist: boolean = false;
            data['berechtigungen'].forEach((berechtigung: obj) => {
                if (berechtigung.name === name!.toLowerCase()) {
                    doesExist = true;
                }
            });

            if (!doesExist) {
                $.get(`/berechtigungen/create/${name}`, (data: obj) => {
                    if (data.success) {
                        edom.findById('tabBerechtigungen')?.doClick();
                    } else {
                        alert('Vorgang abgebrochen.');
                    }
                });
            } else {
                alert('Eine Berechtigung mit diesem Namen existiert bereits.');
            }
        });
    } else {
        alert('Vorgang abgebrochen.');
    }
}
