var nachhilfe = 1;
var vorname;
var nachname;
var mail;
var handy;
var klasse;
var fach;
var klassenstufen;
var anzahl;
var warnung1 = [];
var warnung2 = [];
var warnung3 = [];
var warnung4 = [];
var warnung5 = [];
var warnung6 = [];
var warnung7 = [];
var warnung8 = [];
var warnung9 = [];
var warnung10 = [];
var warnung11 = [];
var warnung12 = [];
var warnung13 = [];
var warnung14 = [];
var warnung15 = [];

window.addEventListener('load', () => {
    vorname = document.getElementById('vorname1');
    nachname = document.getElementById('nachname1');
    mail = document.getElementById('mail1');
    handy = document.getElementById('handy1');
    klasse = document.getElementById('klasse1');
    fach = document.getElementById('fach1');
    klassenstufen = document.getElementById('klassenstufen1');
    var abschicken = document.getElementById('save1');

    //definiert welche Zeichen im Namen der Mail und der Telefonnummer vorkommen dürfen
    let regexName = /^([ \u00c0-\u01ffa-zA-Z\.' \-]{3,})+$/;
    let regexPhone = /^([0-9\.-\/ ()]{7,})$/;
    let regexEmail =
        /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    let errorname = true;
    let erroremail = true;
    let errorhandy = true;

    vorname.onfocus = function () {
        this.setAttribute('style', 'background: white');
    };
    //wenn man das Feld verlässt wird geprüft ob der Name die richtigen Zeichen enhält
    vorname.onblur = function () {
        if (this.value.match(regexName)) {
            warnung2 = [];
            this.setAttribute('style', 'background: white');
            errorname = false;
        } else {
            warnung2.splice(0, 1, 'Bitte geben sie ihren Vornamen ein');
            this.setAttribute('style', 'background:seashell');
            errorname = true;
        }
    };

    //prüft ob Name nur aus Buchstaben besteht

    nachname.onfocus = function () {
        this.setAttribute('style', 'background: white');
    };
    nachname.onblur = function () {
        if (this.value.match(regexName)) {
            warnung3 = [];
            this.setAttribute('style', 'background: white');
            errorname = false;
        } else {
            warnung3.splice(0, 1, 'Bitte geben sie ihren Nachnamen ein!');
            this.setAttribute('style', 'background:seashell');
            errorname = true;
        }
    };

    //prüft ob mail richtiges Format hat

    mail.onfocus = function () {
        this.setAttribute('style', 'background: white');
    };
    mail.onblur = function () {
        if (this.value.match(regexEmail)) {
            warnung4 = [];
            this.setAttribute('style', 'background: white');
            erroremail = false;
        } else {
            warnung4.splice(0, 1, 'Bitte geben sie ihre Emailadresse ein!');
            this.setAttribute('style', 'background:seashell');
            erroremail = true;
        }
    };

    //prüft ob handynummer aus Zahlen besteht

    handy.onfocus = function () {
        this.setAttribute('style', 'background: white');
    };
    handy.onblur = function () {
        if (this.value.match(regexPhone)) {
            warnung5 = [];
            this.setAttribute('style', 'background: white');
            errorhandy = false;
        } else {
            warnung5.splice(0, 1, 'Bitte geben sie ihre Telefonnummer ein!');
            this.setAttribute('style', 'background:seashell');
            errorhandy = true;
        }

        abschicken.onclick = function () {
            if (vorname.value == '') {
                warnung1.splice(0, 1, 'Alle Felder müssen ausgefüllt werden');
            } else if (nachname.value == '') {
                warnung1.splice(0, 1, 'Alle Felder müssen ausgefüllt werden');
            } else if (mail.value == '') {
                warnung1.splice(0, 1, 'Alle Felder müssen ausgefüllt werden');
            } else if (handy.value == '') {
                warnung1.splice(0, 1, 'Alle Felder müssen ausgefüllt werden');
            } else if (
                document.getElementById('one1').checked == false &&
                document.getElementById('multiple1').checked == false
            ) {
                warnung1.splice(0, 1, 'Alle Felder müssen ausgefüllt werden');
            } else if (klasse.innerText == 'Such deine Klasse aus') {
                warnung1.splice(0, 1, 'Alle Felder müssen ausgefüllt werden');
            } else if (fach.innerText == 'In welchen Fach Nachhilfe geben:') {
                warnung1.splice(0, 1, 'Alle Felder müssen ausgefüllt werden');
            } else if (
                klassenstufen.innerText == 'Welche Klassenstufe unterrichten:'
            ) {
                warnung1.splice(0, 1, 'Alle Felder müssen ausgefüllt werden');
            } else if (
                document.getElementById('datenschutz1').checked == false
            ) {
                warnung1.splice(0, 1, 'Alle Felder müssen ausgefüllt werden');
            } else {
                warnung1 = [];
            }

            warnung1.toString();
            warnung2.toString();
            warnung3.toString();
            warnung4.toString();
            warnung5.toString();
            warnungen = warnung1 + warnung2 + warnung3 + warnung4 + warnung5;

            if (document.getElementById('one1').checked == false) {
                anzahl = '1';
            } else {
                anzahl = '0';
            }

            if (warnungen.length > 0) {
                //wenn es warnungen gibt wird das dokument nicht abgeschickt
                //alert("Einige der angegebenen Daten scheinen nicht zu stimmen");
                console.log(warnungen);
            } else {
                console.log('success');
                $.post(
                    '/shs/enroll',
                    {
                        name: vorname.value + ' ' + nachname1.value,
                        klasse: klasse.innerText,
                        mail: mail.value,
                        telefon: handy.value,
                        nachhilfe: nachhilfe,
                        fach: fach.innerText,
                        einzelnachhilfe: anzahl,
                        ziel: klassenstufen.innerText,
                    },
                    (data) => {
                        if (data.success == true) {
                            window.location = './bestätigung.html';
                        } else {
                            alert(
                                "Anscheinend hat etwas nicht geklappt. Bitte versuch's nochmal."
                            );
                        }
                    }
                );
            }
        };
    };

    document.getElementById('x').addEventListener('click', () => {
        nachhilfe = 1;
        //püft ob man beim Nachhilfe geben mindestens in der 8.ten Klasse ist

        vorname = document.getElementById('vorname1');
        nachname = document.getElementById('nachname1');
        mail = document.getElementById('mail1');
        handy = document.getElementById('handy1');
        klasse = document.getElementById('klasse1');
        fach = document.getElementById('fach1');
        klassenstufen = document.getElementById('klassenstufen1');
        var abschicken = document.getElementById('save1');

        //definiert welche Zeichen im Namen der Mail und der Telefonnummer vorkommen dürfen
        let regexName = /^([ \u00c0-\u01ffa-zA-Z\.' \-]{3,})+$/;
        let regexPhone = /^([0-9\.-\/ ()]{7,})$/;
        let regexEmail =
            /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

        let errorname = true;
        let erroremail = true;
        let errorhandy = true;

        vorname.onfocus = function () {
            this.setAttribute('style', 'background: white');
        };
        //wenn man das Feld verlässt wird geprüft ob der Name die richtigen Zeichen enhält
        vorname.onblur = function () {
            if (this.value.match(regexName)) {
                warnung2 = [];
                this.setAttribute('style', 'background: white');
                errorname = false;
            } else {
                warnung2.splice(0, 1, 'Bitte geben sie ihren Vornamen ein');
                this.setAttribute('style', 'background:seashell');
                errorname = true;
            }
        };

        //prüft ob Name nur aus Buchstaben besteht

        nachname.onfocus = function () {
            this.setAttribute('style', 'background: white');
        };
        nachname.onblur = function () {
            if (this.value.match(regexName)) {
                warnung3 = [];
                this.setAttribute('style', 'background: white');
                errorname = false;
            } else {
                warnung3.splice(0, 1, 'Bitte geben sie ihren Nachnamen ein!');
                this.setAttribute('style', 'background:seashell');
                errorname = true;
            }
        };

        //prüft ob mail richtiges Format hat

        mail.onfocus = function () {
            this.setAttribute('style', 'background: white');
        };
        mail.onblur = function () {
            if (this.value.match(regexEmail)) {
                warnung4 = [];
                this.setAttribute('style', 'background: white');
                erroremail = false;
            } else {
                warnung4.splice(0, 1, 'Bitte geben sie ihre Emailadresse ein!');
                this.setAttribute('style', 'background:seashell');
                erroremail = true;
            }
        };

        //prüft ob handynummer aus Zahlen besteht

        handy.onfocus = function () {
            this.setAttribute('style', 'background: white');
        };
        handy.onblur = function () {
            if (this.value.match(regexPhone)) {
                warnung5 = [];
                this.setAttribute('style', 'background: white');
                errorhandy = false;
            } else {
                warnung5.splice(
                    0,
                    1,
                    'Bitte geben sie ihre Telefonnummer ein!'
                );
                this.setAttribute('style', 'background:seashell');
                errorhandy = true;
            }

            abschicken.onclick = function () {
                if (vorname.value == '') {
                    warnung1.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (nachname.value == '') {
                    warnung1.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (mail.value == '') {
                    warnung1.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (handy.value == '') {
                    warnung1.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (
                    document.getElementById('one1').checked == false &&
                    document.getElementById('multiple1').checked == false
                ) {
                    warnung1.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (klasse.innerText == 'Such deine Klasse aus') {
                    warnung1.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (
                    fach.innerText == 'In welchen Fach Nachhilfe geben:'
                ) {
                    warnung1.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (
                    klassenstufen.innerText ==
                    'Welche Klassenstufe unterrichten:'
                ) {
                    warnung1.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (
                    document.getElementById('datenschutz1').checked == false
                ) {
                    warnung1.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else {
                    warnung1 = [];
                }

                warnung1.toString();
                warnung2.toString();
                warnung3.toString();
                warnung4.toString();
                warnung5.toString();
                warnungen =
                    warnung1 + warnung2 + warnung3 + warnung4 + warnung5;

                if (document.getElementById('one1').checked == false) {
                    anzahl = '1';
                } else {
                    anzahl = '0';
                }

                if (warnungen.length > 0) {
                    //wenn es warnungen gibt wird das dokument nicht abgeschickt
                    //alert("Einige der angegebenen Daten scheinen nicht zu stimmen");
                    console.log(warnungen);
                } else {
                    console.log('success');
                    $.post(
                        '/shs/enroll',
                        {
                            name: vorname.value + ' ' + nachname1.value,
                            klasse: klasse.innerText,
                            mail: mail.value,
                            telefon: handy.value,
                            nachhilfe: nachhilfe,
                            fach: fach.innerText,
                            einzelnachhilfe: anzahl,
                            ziel: klassenstufen.innerText,
                        },
                        (data) => {
                            if (data.success == true) {
                                window.location = './bestätigung.html';
                            } else {
                                alert(
                                    "Anscheinend hat etwas nichtgeklappt. Bitte versuch's nochmal."
                                );
                            }
                        }
                    );
                }
            };
        };
    });

    document.getElementById('y').addEventListener('click', () => {
        nachhilfe = 0;

        vorname = document.getElementById('vorname2');
        nachname = document.getElementById('nachname2');
        mail = document.getElementById('mail2');
        handy = document.getElementById('handy2');
        klasse = document.getElementById('klasse2');
        fach = document.getElementById('fach2');
        var abschicken2 = document.getElementById('save2');

        //definiert welche Zeichen im Namen der Mail und der Telefonnummer vorkommen dürfen
        let regexName = /^([ \u00c0-\u01ffa-zA-Z\.' \-]{3,})+$/;
        let regexPhone = /^([0-9\.-\/ ()]{7,})$/;
        let regexEmail =
            /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

        let errorname = true;
        let erroremail = true;
        let errorhandy = true;

        vorname.onfocus = function () {
            this.setAttribute('style', 'background: white');
        };
        //wenn man das Feld verlässt wird geprüft ob der Name die richtigen Zeichen enhält
        vorname.onblur = function () {
            if (this.value.match(regexName)) {
                warnung12 = [];
                this.setAttribute('style', 'background: white');
                errorname = false;
            } else {
                warnung12.splice(0, 1, 'Bitte geben sie ihren Vornamen ein');
                this.setAttribute('style', 'background:seashell');
                errorname = true;
            }
        };

        //prüft ob Name nur aus Buchstaben besteht

        nachname.onfocus = function () {
            this.setAttribute('style', 'background: white');
        };
        nachname.onblur = function () {
            if (this.value.match(regexName)) {
                warnung13 = [];
                this.setAttribute('style', 'background: white');
                errorname = false;
            } else {
                warnung13.splice(0, 1, 'Bitte geben sie ihren Nachnamen ein!');
                this.setAttribute('style', 'background:seashell');
                errorname = true;
            }
        };

        //prüft ob mail richtiges Format hat

        mail.onfocus = function () {
            this.setAttribute('style', 'background: white');
        };
        mail.onblur = function () {
            if (this.value.match(regexEmail)) {
                warnung14 = [];
                this.setAttribute('style', 'background: white');
                erroremail = false;
            } else {
                warnung14.splice(
                    0,
                    1,
                    'Bitte geben sie ihre Emailadresse ein!'
                );
                this.setAttribute('style', 'background:seashell');
                erroremail = true;
            }
        };

        //prüft ob handynummer aus Zahlen besteht

        handy.onfocus = function () {
            this.setAttribute('style', 'background: white');
        };
        handy.onblur = function () {
            if (this.value.match(regexPhone)) {
                warnung15 = [];
                this.setAttribute('style', 'background: white');
                errorhandy = false;
            } else {
                warnung15.splice(
                    0,
                    1,
                    'Bitte geben sie ihre Telefonnummer ein!'
                );
                this.setAttribute('style', 'background:seashell');
                errorhandy = true;
            }

            abschicken2.onclick = function () {
                if (vorname.value == '') {
                    warnung11.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (nachname.value == '') {
                    warnung11.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (mail.value == '') {
                    warnung11.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (handy.value == '') {
                    warnung11.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (
                    document.getElementById('one2').checked == false &&
                    document.getElementById('multiple2').checked == false
                ) {
                    warnung11.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (klasse.innerText == 'Such deine Klasse aus') {
                    warnung11.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (
                    fach.innerText == 'In welchem Fach Nachhilfe nehmen:'
                ) {
                    warnung11.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else if (
                    document.getElementById('datenschutz2').checked == false
                ) {
                    warnung11.splice(
                        0,
                        1,
                        'Alle Felder müssen ausgefüllt werden'
                    );
                } else {
                    warnung11 = [];
                }

                if (document.getElementById('one2').checked == true) {
                    anzahl = '0';
                } else if (
                    document.getElementById('multiple2').checked == true
                ) {
                    anzahl = '0';
                }

                warnung11.toString();
                warnung12.toString();
                warnung13.toString();
                warnung14.toString();
                warnung15.toString();
                warnungen2 =
                    warnung11 + warnung12 + warnung13 + warnung14 + warnung15;

                if (warnungen2.length > 0) {
                    //wenn es warnungen gibt wird das dokument nicht abgeschickt
                    console.log(warnungen2);
                    //alert("Einige der angegebenen Daten scheinen nicht zu stimmen");
                } else {
                    console.log('success');
                    $.post(
                        '/shs/enroll',
                        {
                            name: vorname.value + ' ' + nachname.value,
                            klasse: klasse.innerText,
                            mail: mail.value,
                            telefon: handy.value,
                            nachhilfe: nachhilfe,
                            fach: fach.innerText,
                            einzelnachhilfe: anzahl,
                            ziel: klassenstufen.innerText,
                        },
                        (data) => {
                            if (data.success == true) {
                                window.location = './bestätigung.html';
                            } else {
                                alert(
                                    "Anscheinend hat etwas nicht geklappt. Bitte versuch's nochmal."
                                );
                            }
                        }
                    );
                }
            };
        };
    });
});
