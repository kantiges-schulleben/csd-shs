// markup
function muModules(): obj {
    return [
        {
            tag: 'button',
            classes: ['fa', 'fa-refresh'],
            handler: [
                {
                    type: 'click',
                    id: 'reladModules',
                    arguments: '',
                    body: 'reload();',
                },
            ],
        },
        {
            tag: 'div',
            id: 'container',
        },
    ];
}

// =======================================================================================

// code
function loadModules() {
    $.get('/modules/getall', (data: obj) => {
        console.log(data);
        var rowCounter: number = 0;

        Object.keys(data).forEach((module: string) => {
            edom.fromTemplate(
                [
                    {
                        tag: 'div',
                        classes: [rowCounter % 2 === 0 ? 'line0' : 'line1'],
                        children: [
                            {
                                tag: 'h3',
                                text: module,
                            },
                            {
                                tag: 'label',
                                text: 'status: ',
                            },
                            {
                                tag: 'label',
                                text: data[module].running
                                    ? 'running'
                                    : 'stopped',
                                classes: [
                                    data[module].running
                                        ? 'moduleRunning'
                                        : 'moduleStopped',
                                ],
                            },
                            {
                                tag: 'br',
                            },
                            {
                                tag: 'button',
                                classes: [
                                    'fa',
                                    data[module].running
                                        ? 'fa-stop'
                                        : 'fa-play',
                                ],
                                handler: [
                                    {
                                        type: 'click',
                                        id: data[module].running
                                            ? 'stopModule'
                                            : 'startModule',
                                        arguments: 'self',
                                        body: `${
                                            data[module].running
                                                ? 'stop'
                                                : 'start'
                                        }("${module}", self)`,
                                    },
                                ],
                            },
                            {
                                tag: 'a',
                                text: 'info',
                                classes: ['lnkMoreInfo'],
                                id: module,
                                handler: [
                                    {
                                        type: 'click',
                                        id: 'getMoreInfo',
                                        arguments: 'self',
                                        body: `getMoreInfoAboutModule(self)`,
                                    },
                                ],
                            },
                            {
                                tag: 'div',
                                id: `info${module}`,
                            },
                        ],
                    },
                ],
                edom.findById('container')
            );
            rowCounter++;
        });
    });
}

function stop(module: string, sender: edomElement) {
    if (confirm(`Soll folgendes Modul wirklich gestoppt werden? '${module}'`)) {
        sender.swapStyle('fa-stop', 'fa-spinner');
        $.get(`/modules/kill/${module}`, (data: obj) => {
            console.log(data);
            sender.swapStyle('fa-spinner', 'fa-play');

            sender.parent?.children[2].setText('stopped');
            sender.parent?.children[2].swapStyle(
                'moduleRunning',
                'moduleStopped'
            );

            sender.deleteClick('stopModule');
            sender.addClick('startModule', (self: edomElement) => {
                start(module, self);
            });
        });
    }
}

function start(module: string, sender: edomElement) {
    sender.swapStyle('fa-start', 'fa-spinner');
    $.get(`/modules/start/${module}`, (data: obj) => {
        console.log(data);
        sender.swapStyle('fa-spinner', 'fa-stop');

        sender.parent?.children[2].setText('running');
        sender.parent?.children[2].swapStyle('moduleStopped', 'moduleRunning');

        sender.deleteClick('startModule');
        sender.addClick('stopModule', (self: edomElement) => {
            stop(module, self);
        });
    });
}

function reload() {
    edom.findById('container')?.clear();
    loadModules();
}

function getMoreInfoAboutModule(sender: edomElement) {
    console.log(sender.id);
    if (edom.findById(`info${sender.id}`)!.children.length > 0) {
        edom.findById(`info${sender.id}`)?.clear();
        return;
    }
    $.get(`/modules/info/${sender.id}`, (data: obj) => {
        if (data.success) {
            edom.findById(`info${sender.id}`)?.clear();
            edom.fromTemplate(
                [
                    {
                        tag: 'ul',
                        classes: ['moduleInfo'],
                        children: [
                            {
                                tag: 'li',
                                text: `Name: ${data.info.name}`,
                            },
                            {
                                tag: 'li',
                                text: `Authoren: ${data.info.authors.join(
                                    ', '
                                )}`,
                            },
                            {
                                tag: 'li',
                                text: `Version: ${data.info.version}`,
                            },
                            {
                                tag: 'li',
                                text: `Dateien: ${data.info.files
                                    .map((file: string) => {
                                        return file.concat('.ts');
                                    })
                                    .join(', ')}`,
                            },
                            {
                                tag: 'li',
                                text: `Routen:`,
                                children: [
                                    {
                                        tag: 'ul',
                                        children: data.info.routes.map(
                                            (route: obj) => {
                                                return {
                                                    tag: 'li',
                                                    classes: ['moduleInfoLv2'],
                                                    text:
                                                        route.type.toUpperCase() +
                                                        ' ' +
                                                        route.path,
                                                };
                                            }
                                        ),
                                    },
                                ],
                            },
                            {
                                tag: 'li',
                                text: `Beschreibung: ${data.info.description}`,
                            },
                        ],
                    },
                ],
                edom.findById(`info${sender.id}`)
            );
        }
    });
}
