export {};
interface menuItem {
  name: string;
  location: string;
};

function preload() {
  tokenPresent();
    // set title dynamically for all pages
    document.title = 'DEMO';

    // create Navbar
    document.getElementById('navbar')!.innerHTML = `
                    ${/*TODO css zentralisieren & style hier entfernen*/ ''}
                    <style>
                    .isLoggedIn {
                        box-shadow: 0 0 1px 1px var(--secondary-color);
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
                        ><span>${new Date().getFullYear()} All rights reserved.</span>
                    </div>
                </div>`;

    // set favicon dynamically for all pages
    const link = document.createElement('link');
    link.rel = 'icon';
    link.href = '/public/favicon.svg';
    document.getElementsByTagName('head')[0].appendChild(link);

    fetch(`${backend}/api/users/menu`, {
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
    .then((data: menuItem[]) => {
        redirectToLoginIfNeeded(data);
        data.forEach((item: menuItem) => {
          const li: HTMLLIElement = document.createElement("li");
          const link: HTMLAnchorElement = document.createElement("a");
          link.textContent = item.name;
          link.href = item.location;
          if (item.location === "/api/users/logout") {
            link.href = backend + item.location;
            link.addEventListener("click", (e: Event) => {
              e.preventDefault();
              localStorage.removeItem("csd_token");
              location.assign(`${backend}${item.location}`);
            });
          } else if (item.location.startsWith("/oauth2/authorization/")) {
            link.href = backend + item.location;
          }

          li.appendChild(link);

          document.getElementById('submenuDynamicLinks')!.appendChild(li);

        });
        const jwtUserNameClaim: string | null = getJwtClaim("username");
        if (jwtUserNameClaim !== null) {
          document.getElementById('lnkKonto')!.textContent = jwtUserNameClaim;
          document.getElementById('liLnkKonto')!.classList.add('isLoggedIn');
        }
    })
    .catch((e: any) => {
      console.error(e);
      const errorLabel: HTMLLabelElement = document.createElement("label");
      errorLabel.innerText = "Fehler beim Laden des Menüs";
      document.getElementById('submenuDynamicLinks')!.appendChild(errorLabel);
    });

    navSlide();
}

function redirectToLoginIfNeeded(menu: menuItem[]) {
  const loginNeeded: string[] = [
    "/shs/anmeldung",
    "/dev",
    "/shs/admin"
  ];

  if (menu.length > 1) {
    return;
  }
  if (menu[0].name !== "Login") {
    return;
  }

  const location: string = window.location.pathname;

  if (loginNeeded.some(loc => location.startsWith(loc))) {
    window.location.assign(backend + menu[0].location);
  }
}

function tokenPresent() {
  const params = new URLSearchParams(document.location.search);
  const token = params.get("token");

  if (token !== null) {
    localStorage.setItem("csd_token", token);
    location.assign("/");
  }
}
