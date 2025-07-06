export {};

function preload() {
    // set title dynamically for all pages
    document.title = 'kantiges Schulleben';

    // create Navbar
    document.getElementById('navbar')!.innerHTML = `
                    ${/*TODO css zentralisieren & style hier entfernen*/ ''}
                    <style>
                    .isLoggedIn {
                        box-shadow: 0 0 1px 1px #16cc68;
                        border-radius: 50px;
                        padding: 10px 30px 10px 30px !important;
                        transform:scale(1.02);
                        transform-origin: center;
                    }

                    .isLoggedIn:hover {
                        box-shadow: none;
                    }

                    .menu-bar ul li {
                        margin-left: 5px;
                        margin-right: 5px;
                    }
                    </style>
                    ${/*TODO css zentralisieren & style hier entfernen*/ ''}
                    <div class='menu-bar'>
                        <ul class='nav-links'>
                            <li class='active'><a href='/'>Startseite</a>
                            </li>
                            <li><a href='/shs/information'>Informationen</a></li>
                            <li><a href='/shs/blog'>Aktuelles</a></li>
                            <li><a href='/shs/anmeldung'>Anmeldung</a></li>
                            <li id="liLnkKonto">
                                <a id="lnkKonto">Konto</a>
                                <div class='sub-menu-1'>
                                    <ul id="submenuDynamicLinks">
                                    </ul>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class='burger'>
                        <div class='line1'></div>
                        <div class='line2'></div>
                        <div class='line3'></div>
                    </div>`;

    // set footer
    document.getElementById('footer')!.innerHTML = `<div class="footer">
                    <ul>
                        <li>
                            <div class="titleR">Rechtliches</div>
                            <div class="buttonsR">
                                <a href="/general/datenschutz"
                                    >Datenschutzerklärung</a
                                ><br />
                                <a href="/general/impressum">Impressum</a
                                ><br />
                            </div>
                        </li>
                        <li>
                            <div class="titleS">Socialmedia</div>
                            <div class="buttonsS">
                                <a href="https://www.kantgym-leipzig.de"
                                    >Webseite unserer Schule</a
                                ><br />
                                <a
                                    href="https://www.instagram.com/kantrat_kantgym/"
                                    >Instagram</a
                                ><br />
                            </div>
                        </li>
                    </ul>
                    <div class="cc">
                        <span class="fa fa-copyright"></span
                        ><span>2022 All rights reserved.</span>
                    </div>
                </div>`;

    // set favicon dynamically for all pages
    const link = document.createElement('link');
    link.rel = 'icon';
    link.href = '/public/favicon.svg';
    document.getElementsByTagName('head')[0].appendChild(link);

    $.get('/kontoSubMenuInfo', (data: obj) => {
        data.links.forEach((link: string) => {
            document.getElementById('submenuDynamicLinks')!.innerHTML += link;
        });

        document.getElementById('lnkKonto')!.innerText = data.username;

        if (data.username !== 'Konto') {
            document.getElementById('liLnkKonto')!.classList.add('isLoggedIn');
        }
    });

    navSlide();
}
