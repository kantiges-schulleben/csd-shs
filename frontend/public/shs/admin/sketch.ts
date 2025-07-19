const backend: string = "http://localhost:8080";
function startup() {
    edom.init();
    renderDefaultUi();
    checkPhase();
}

function renderDefaultUi() {
    edom.fromTemplate({
        children: [
            {
                tag: 'nav',
                id: 'navbar',
            },
            {
                tag: 'div',
                classes: ['content'],
                id: "content"
            },

            {
                tag: 'footer',
                id: 'footer',
            },
        ],
    });
}

function checkPhase() {
    fetch(`${backend}/api/shs/admin/is-phase-two`, {
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
    .then((isPhase2: boolean) => {
      if (isPhase2) {
        renderPhase2Ui();
      } else {
        renderPhase1Ui();
      }
    })
    .catch((e: any) => {
      // TODO
      console.error(e);
    });
}
