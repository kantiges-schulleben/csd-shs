// imports ================================================================================
import * as types from './types';
import { Application } from 'express';
import express from 'express';
import * as fs from 'fs';
import * as path from 'path';
import { config as dotenvConfig } from 'dotenv';
import session from 'express-session';
import { Server } from 'http';
import { engine as handlebars } from 'express-handlebars';
// imports ================================================================================
dotenvConfig();
// vars ===================================================================================
const app: Application = express();

const server: Server = new Server(app);

const port: number = parseInt(process.env.PORT || '9000');

var modules: types.obj = {};
// vars ===================================================================================

// code ===================================================================================
app.use(express.static('./src/public'));
app.use(express.urlencoded());
app.use(
    session({
        secret: [process.env.SESSION_SECRET || randomToken(128)],
        resave: false,
        saveUninitialized: true,
    })
);
app.engine('handlebars', handlebars());
app.set('view engine', 'handlebars');

const directoryPath: string = path.join(__dirname, 'modules');
let moduleBlacklist: string[] = ['tmp'];

const moduleSettings: types.obj = JSON.parse(
    fs.readFileSync('./src/config.json').toString()
);

Object.keys(moduleSettings['modules']).forEach((key: string) => {
    if (moduleSettings['modules'][key] === false) {
        moduleBlacklist.push(key);
    }
});

fs.readdir(
    directoryPath,
    function (err: NodeJS.ErrnoException | null, files: string[]) {
        if (err) {
            return console.log('Unable to scan directory: ' + err);
        }

        files.forEach(function (file: string) {
            if (!moduleBlacklist.includes(file)) {
                modules[file] = require(`./modules/${file}/index`);
                modules[file].config(app, modules, directoryPath, server);
            }
        });
    }
);

server.listen(port, '0.0.0.0', () => {
    console.log(`go on port ${port}`);
});

function randomToken(length: number = 64): string {
    var token: string = '';
    const chars: Array<string> = [
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
        'G',
        'H',
        'I',
        'J',
        'K',
        'L',
        'M',
        'N',
        'O',
        'P',
        'Q',
        'R',
        'S',
        'T',
        'U',
        'V',
        'W',
        'X',
        'Y',
        'Z',
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h',
        'i',
        'j',
        'k',
        'l',
        'm',
        'n',
        'o',
        'p',
        'q',
        'r',
        's',
        't',
        'u',
        'v',
        'w',
        'x',
        'y',
        'z',
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
    ];
    for (let i = 0; i < length; i++) {
        token += chars[Math.floor(Math.random() * chars.length)];
    }
    return token;
}
// code ===================================================================================
