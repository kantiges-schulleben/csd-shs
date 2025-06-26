export {};

interface obj extends Object {
    [key: string]: any;
}

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
                        children: muTabs(),
                    },
                    {
                        tag: 'div',
                        id: 'actualContent',
                        children: muModules(),
                    },
                ],
            },
            {
                tag: 'footer',
                id: 'footer',
            },
        ],
    });

    loadModules();
}

function muTabs(): obj {
    return [
        {
            tag: 'button',
            id: 'tabModules',
            text: 'modules',
            classes: ['tab', 'active'],
            handler: [
                {
                    type: 'click',
                    id: 'clickSetFocus',
                    arguments: '',
                    body: 'setTabFocus("tabModules");',
                },
            ],
        },
        {
            tag: 'button',
            id: 'tabBerechtigungen',
            text: 'Berechtigungen',
            classes: ['tab'],
            handler: [
                {
                    type: 'click',
                    id: 'clickSetFocus',
                    arguments: '',
                    body: 'setTabFocus("tabBerechtigungen");',
                },
            ],
        },
        {
            tag: 'button',
            id: 'tabUser',
            text: 'Benutzer*innen',
            classes: ['tab'],
            handler: [
                {
                    type: 'click',
                    id: 'clickSetFocus',
                    arguments: '',
                    body: 'setTabFocus("tabUser");',
                },
            ],
        },
    ];
}

function setTabFocus(newFocusID: string) {
    (
        Array.from(document.getElementsByClassName('active')) as HTMLElement[]
    ).forEach((element: HTMLElement) => {
        if (element.tagName === 'BUTTON') {
            element.classList.remove('active');
        }
    });

    edom.findById(newFocusID)?.applyStyle('active');

    switchContent(newFocusID);
}

function switchContent(contentID: string) {
    edom.findById('actualContent')?.clear();

    switch (contentID) {
        case 'tabModules':
            edom.fromTemplate(muModules(), edom.findById('actualContent'));
            loadModules();
            break;
        case 'tabUser':
            edom.fromTemplate(muUser(), edom.findById('actualContent'));
            (
                edom.findById('inputUserName')?.element as HTMLInputElement
            ).placeholder = 'Benutzer*innenname';

            break;
        case 'tabBerechtigungen':
            edom.fromTemplate(
                muBerechtigungen(),
                edom.findById('actualContent')
            );
            getBerechtigungen();
            break;
    }
}
