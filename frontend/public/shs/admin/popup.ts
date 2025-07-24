function popup(title: string, body: edomObj, preRender: () => void = () => {}) {
    edom.fromTemplate(
        [
            {
                tag: 'div',
                classes: ['popupBackground'],
                children: [
                    {
                        tag: 'div',
                        classes: ['popupBody'],
                        children: [
                            {
                                tag: 'div',
                                classes: ['popupHeader'],
                                children: [
                                    {
                                        tag: 'h1',
                                        classes: ['popupTitle'],
                                        text: title,
                                    },
                                    {
                                        tag: 'button',
                                        text: 'x',
                                        handler: [
                                            {
                                                type: 'click',
                                                id: 'clickClosePopup',
                                                arguments: 'self',
                                                body: 'closePopup(self)',
                                            },
                                        ],
                                    },
                                ],
                            },
                            ...(body !== null ? [body] : []),
                        ],
                    },
                ],
            },
        ],
        edom.body
    );
}

function closePopup(self: edomElement) {
    if (self.tag.toLowerCase() === 'body' || self.parent === undefined) {
        return;
    }

    if (self.parent.classes.includes('popupBackground')) {
        self.parent.delete();
        return;
    }

    closePopup(self.parent);
}
