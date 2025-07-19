function renderPhase1Ui() {
    edom.fromTemplate(
        [
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
                        tag: 'p',
                        id: 'output',
                    },
                    // @ts-ignore
                    ...markupUser(),
                ],
            },
        ],
        edom.findById('content')!
    );
    displayStudentCount();
    (edom.findById('inputUserName')?.element as HTMLInputElement).placeholder =
        'Name';
}

function displayStudentCount() {
    fetch(`${backend}/api/shs/admin/students/count`, {
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
            return response.text();
        })
        .then((count: string) => {
            (document.getElementById('counter') as HTMLLabelElement).innerText =
                'angemeldete Schüler*innen: ' + count;
        })
        .catch((e: any) => {
            console.error(e);
            (document.getElementById('counter') as HTMLLabelElement).innerHTML =
                '<i>Die Anzahl der angemeldeten Schüler*innen konnte nicht abgerufen werden</i>';
        });
}

let statusTimer: number;

function startScript() {
    if (confirm('Auswertung starten?')) {
        (document.getElementById('output') as HTMLParagraphElement).innerText =
            'auswertung gestartet...';
        fetch(`${backend}/api/shs/admin/start`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
            },
            method: 'POST',
        })
            .then((response: Response) => {
                if (response.status !== 202) {
                    throw new Error(
                        `HTTP ${response.status} ${
                            response.statusText
                        } - ${response.text()}`
                    );
                }

                statusTimer = setInterval(checkStatus, 1000);
            })
            .catch((e: any) => {
                console.error(e);
                (
                    document.getElementById('output') as HTMLParagraphElement
                ).innerText = 'Fehler beim Starten der Auswertung';
            });
    }
}

let dots = 0;

function clearTimer() {
    clearInterval(statusTimer);
}

function checkStatus() {
    (
        document.getElementById('output') as HTMLParagraphElement
    ).innerText = `auswertung gestartet${'.'.repeat(++dots % 4)}`;
    fetch(`${backend}/api/shs/admin/analysis/running`, {
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
        .then((running: boolean) => {
            if (!running) {
                clearTimer();
                loadAnalysisResult();
            }
        })
        .catch((e: any) => {
            console.error(e);
        });
}

function loadAnalysisResult() {
    fetch(`${backend}/api/shs/admin/analysis/status`, {
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
        .then((status: { first: boolean; second: string }) => {
            if (!status.first) {
                (
                    document.getElementById('output') as HTMLParagraphElement
                ).innerText = status.second;
                return;
            }
            (
                document.getElementById('output') as HTMLParagraphElement
            ).innerText = 'Auswertung erfolgreich abgeschlossen';
            location.reload();
        })
        .catch((e: any) => {
            console.error(e);
        });
}
