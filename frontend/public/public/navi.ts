const navSlide = () => {
    const burger: Element = document.querySelector('.burger')!;
    const nav: Element = document.querySelector('.nav-links')!;
    const navLinks: NodeListOf<Element> =
        document.querySelectorAll('.nav-links li')!;

    burger.addEventListener('click', () => {
        nav.classList.toggle('nav-active');

        navLinks.forEach((link: Element) => {
            (
                link as HTMLLIElement
            ).style.animation = `navLinkFade 0.5s ease forwards`;
        });

        burger.classList.toggle('toggle');
    });
};
