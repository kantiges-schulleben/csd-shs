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

const backend: string = "http://localhost:8080";

function displayStudentCount() {
        // (document.getElementById('counter') as HTMLLabelElement).innerText =
    fetch(`${backend}/api/shs/admin/students/count`, {
      headers: {
        "Authorization": `Bearer ${localStorage.getItem("csd_token")}`
      }
    })
    .then((response: Response) => {
      if (!response.ok) {
        throw new Error(`HTTP ${response.status} ${response.statusText} - ${response.text()}`);
      }
      return response.text();
    })
    .then((count: string) => {
      (document.getElementById('counter') as HTMLLabelElement).innerText = "angemeldete Schüler*innen: " + count;
    })
    .catch((e: any) => {
      console.error(e);
      (document.getElementById('counter') as HTMLLabelElement).innerHTML = "<i>Die Anzahl der angemeldeten Schüler*innen konnte nicht abgerufen werden</i>";
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
