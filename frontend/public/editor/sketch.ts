export {};

function startup() {
    $.get('/blog/getCurrentBlog', (data: { blog: string }) => {
        (
            document.getElementById('hochladen') as HTMLFormElement
        ).action = `/editor/${data.blog}/saveArticle`;
    });
}
