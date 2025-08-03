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
                        tag: 'br',
                    },
                    {
                        tag: 'input',
                        type: 'date',
                        id: 'inputEndDate',
                    },
                    {
                        tag: 'button',
                        text: 'speichern',
                        id: 'bttnUpdateEnddate',
                        classes: ['searchButton'],
                        handler: [
                            {
                                type: 'click',
                                id: 'clickUpdateEndDate',
                                arguments: '',
                                body: 'updateEndDate()',
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
    loadEndDate();
    (edom.findById('inputUserName')?.element as HTMLInputElement).placeholder =
        'Name';
}

function updateEndDate() {
    const newEnddate: string = (
        edom.findById('inputEndDate')!.element as HTMLInputElement
    ).value;

    edom.findById('bttnUpdateEnddate')?.setText('...');
    fetch(`${backend}/api/shs/admin/end-date`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
        },
        body: JSON.stringify({
            enrollEndDate: new Date(newEnddate).toISOString(),
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
            edom.findById('bttnUpdateEnddate')?.setText('speichern');
        })
        .catch((e: any) => {
            console.error(e);
            edom.findById('bttnUpdateEnddate')?.setText('fehler');
        });
}

function loadEndDate() {
    fetch(`${backend}/api/shs/end-date`, {
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
        .then((endDate: string) => {
            console.log(endDate);
            const date: Date = new Date(endDate);
            console.log(date);
            const day = ('0' + date.getDate()).slice(-2);
            const month = ('0' + (date.getMonth() + 1)).slice(-2);
            (
                edom.findById('inputEndDate')?.element as HTMLInputElement
            ).value = `${date.getFullYear()}-${month}-${day}`;
        })
        .catch((e: any) => {
            console.error(e);
            (document.getElementById('counter') as HTMLLabelElement).innerHTML =
                '<i>Das Einschreibungsende konnte nicht abgerufen werden</i>';
        });
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
