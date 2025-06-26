function loadContent() {
    return;
    $.get('/blog/readArticle', (data) => {
        (document.getElementById('blogName') as HTMLHeadingElement).innerText =
            decodeURIComponent(capitalizeFirstLetter(data.name));
        (document.getElementById('title0') as HTMLHeadingElement).innerText =
            decodeURIComponent(data.title);
        (document.getElementById('title1') as HTMLHeadingElement).innerText =
            decodeURIComponent(data.title);
        (document.getElementById('title2') as HTMLHeadingElement).innerText =
            decodeURIComponent(data.title);
        (document.getElementById('picture') as HTMLImageElement).src =
            decodeURIComponent(data.image);
        (document.getElementById('content') as HTMLDivElement).innerHTML =
            parseMarkdown(decodeURIComponent(data.content));
    });
}

function capitalizeFirstLetter(string: string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}
