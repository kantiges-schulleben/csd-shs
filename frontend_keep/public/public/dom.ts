function createStatement(
    _title: string = '',
    _content: string = '',
    _image: string = '',
    _target: string = ''
) {
    /*
    <div class="item2">
        <div class="placeholder"></div>
        <img src="../../assets/email.png">
        <div class="f">Titel</div><br>
        <div class="d">
            Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung
            stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung
            stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen! Hier könnte ihre Werbung stehen!
        </div>
        <a href="#Protokolle" class="button1">Beitrag anzeigen</a>
    </div>
    */

    const container: HTMLDivElement = document.createElement(
        'div'
    ) as HTMLDivElement;
    container.classList.add('item2');

    const placeholder: HTMLDivElement = document.createElement(
        'div'
    ) as HTMLDivElement;
    placeholder.classList.add('placeholder');

    const image: HTMLImageElement = document.createElement(
        'img'
    ) as HTMLImageElement;
    image.src = _image;

    const title: HTMLDivElement = document.createElement(
        'div'
    ) as HTMLDivElement;
    title.classList.add('f');
    title.innerText = _title;

    const content: HTMLDivElement = document.createElement(
        'div'
    ) as HTMLDivElement;
    content.classList.add('d');
    content.innerHTML = _content;

    const linkToArticle: HTMLAnchorElement = document.createElement(
        'a'
    ) as HTMLAnchorElement;
    linkToArticle.classList.add('button1');
    linkToArticle.href = _target;
    linkToArticle.innerText = 'Beitrag anzeigen';

    // ===========================================================
    container.appendChild(placeholder);
    container.appendChild(image);
    container.appendChild(title);
    container.appendChild(document.createElement('br'));
    container.appendChild(content);
    container.appendChild(linkToArticle);
    // ===========================================================

    return container;
}

function createDownloadProtokoll(
    _title: string = '',
    _date: string = '',
    _downloadLocation: string = ''
) {
    /*
    <div class="item">
        Protokol 1 <br>
        <div class="d">12.04.2021</div>
        <a href="#Protokolle" class="button">download</a>
    </div>
    */

    const container: HTMLDivElement = document.createElement(
        'div'
    ) as HTMLDivElement;
    container.classList.add('item');
    container.innerText = _title;

    const date: HTMLDivElement = document.createElement(
        'div'
    ) as HTMLDivElement;
    date.classList.add('d');
    date.innerText = _date;

    const download: HTMLAnchorElement = document.createElement(
        'a'
    ) as HTMLAnchorElement;
    download.classList.add('button');
    download.href = _downloadLocation;
    download.innerText = 'download';

    // ===========================================================
    container.appendChild(document.createElement('br'));
    container.appendChild(date);
    container.appendChild(download);
    // ===========================================================

    return container;
}

function createNoContent(_title: string = '') {
    const container: HTMLDivElement = document.createElement(
        'div'
    ) as HTMLDivElement;
    // container.classList.add("item2");

    const placeholder: HTMLDivElement = document.createElement(
        'div'
    ) as HTMLDivElement;
    placeholder.classList.add('placeholder');

    const title: HTMLDivElement = document.createElement(
        'div'
    ) as HTMLDivElement;
    title.classList.add('f');
    title.innerText = _title;
    title.style.fontStyle = 'italic';

    // ===========================================================
    container.appendChild(placeholder);
    container.appendChild(title);
    // ===========================================================

    return container;
}
