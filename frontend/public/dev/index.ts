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
                        id: 'actualContent',
                        children: muUser(),
                    },
                ],
            },
            {
                tag: 'footer',
                id: 'footer',
            },
        ],
    });
}
