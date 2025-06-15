function startup() {
    edom.init();
    edom.fromTemplate({
        children: [
            {
                tag: 'nav',
                id: 'navbar',
            },
            {
                tag: 'div',
                classes: ['content'],
                children: [
                    {
                        tag: 'div',
                        classes: ['tabs'],
                    },

                    {
                        tag: 'div',
                        id: 'actualContent',
                        classes: ['content'],
                        children: [
                            {
                                tag: 'lable',
                                id: 'counter',
                                text: 'amgemeldete Schüler*innnen:',
                            },
                            {
                                tag: 'button',
                                text: 'Auswertung starten',
                                classes: ['searchButton'],
                                handler: [
                                    {
                                        type: 'click',
                                        id: 'clickStartScript',
                                        arguments: '',
                                        body: 'startScript()',
                                    },
                                ],
                            },
                            {
                              tag: "p",
                              id: "output"
                            },
                            // @ts-ignore
                            ...markupUser(),
                        ],
                    },
                ],
            },

            {
                tag: 'footer',
                id: 'footer',
            },
        ],
    });
    displayStudentCount();
    (edom.findById('inputUserName')?.element as HTMLInputElement).placeholder =
        'Name';
}

function displayStudentCount() {
    $.get('/shs/admin/count', function (data) {
        (document.getElementById('counter') as HTMLLabelElement).innerText =
            'angemeldete Schüler*innen: ' + data.count;
    });
}

function startScript() {
    if (confirm('Auswertung starten?')) {
        (document.getElementById('output') as HTMLParagraphElement).innerText =
            'auswertung gestartet...';
        $.get('/shs/admin/start', function (data: {[key: string]: any}) {
            if (!data["success"]) {
              (document.getElementById('output') as HTMLParagraphElement).innerText = "Es ist ein Fehler bei der Auswertung aufgetreten";
            } else {
              (document.getElementById('output') as HTMLParagraphElement).innerText = "Auswertung erfolreich abgeschlossen";
            }
        });
    }
}
