let überschrift: HTMLInputElement = document.getElementById(
    'überschrift'
) as HTMLInputElement;
let inhalt: HTMLTextAreaElement = document.getElementById(
    'inhalt'
) as HTMLTextAreaElement;
let bild: HTMLImageElement = document.getElementById(
    'bild'
) as HTMLImageElement;
let tags: HTMLInputElement = document.getElementById(
    'tags'
) as HTMLInputElement;

(
    document.getElementById('editorButton') as HTMLButtonElement
).style.background = '#16cc68';
(
    document.getElementById('previewButton') as HTMLButtonElement
).style.background = 'white';
(document.getElementById('previewButton') as HTMLButtonElement).style.color =
    '#16cc68';
(document.getElementById('vorschau') as HTMLDivElement).hidden = true;
(document.getElementById('deleteThumbnailButton') as HTMLDivElement).hidden =
    true;

(
    Array.from(
        document.querySelectorAll('.drop-zone__input')
    ) as HTMLInputElement[]
).forEach((inputElement: HTMLInputElement) => {
    const dropZoneElement: HTMLDivElement = inputElement.closest(
        '.drop-zone'
    ) as HTMLDivElement;

    dropZoneElement.addEventListener('click', (e) => {
        inputElement.click();
    });

    inputElement.addEventListener('change', (e) => {
        if (inputElement.files!.length) {
            let file = inputElement.files![0];
            updateThumbnail(dropZoneElement, file);
        }
    });

    dropZoneElement.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropZoneElement.classList.add('drop-zone--over');
    });

    ['dragleave', 'dragend'].forEach((type) => {
        dropZoneElement.addEventListener(type, (e) => {
            dropZoneElement.classList.remove('drop-zone--over');
        });
    });
    dropZoneElement.addEventListener('drop', (e: DragEvent) => {
        e.preventDefault();

        if (e.dataTransfer!.files.length) {
            if (e.dataTransfer!.files[0].type.startsWith('image/')) {
                inputElement.files = e.dataTransfer!.files;
                let file = e.dataTransfer!.files[0];
                e.dataTransfer!.clearData();
                updateThumbnail(dropZoneElement, file);
            }
        }

        dropZoneElement.classList.remove('drop-zone--over');
    });
});

function updateThumbnail(dropZoneElement: HTMLDivElement, file: File) {
    (
        document.getElementById('deleteThumbnailButton') as HTMLDivElement
    ).hidden = false;
    let thumbnailElement: HTMLDivElement = dropZoneElement.querySelector(
        '.drop-zone__thumb'
    ) as HTMLDivElement;

    if (dropZoneElement.querySelector('.drop-zone__prompt')) {
        (
            dropZoneElement.querySelector(
                '.drop-zone__prompt'
            ) as HTMLSpanElement
        ).remove();
    }

    if (!thumbnailElement) {
        thumbnailElement = document.createElement('div');
        thumbnailElement.classList.add('drop-zone__thumb');
        dropZoneElement.appendChild(thumbnailElement);
    }

    let fileName = file.name;
    if (fileName.length > 16) {
        fileName = file.name.substring(0, 13) + '...';
    }
    thumbnailElement.dataset.label = fileName;

    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
        thumbnailElement.style.backgroundImage = `url('${reader.result}')`;
    };
}

function removeThumbnail() {
    (
        document.getElementById('deleteThumbnailButton') as HTMLDivElement
    ).hidden = true;
    (
        Array.from(
            document.querySelectorAll('.drop-zone__input')
        ) as HTMLInputElement[]
    ).forEach((inputElement: HTMLInputElement) => {
        const dropZoneElement: HTMLDivElement = inputElement.closest(
            '.drop-zone'
        ) as HTMLDivElement;
        dropZoneElement.classList.remove('drop-zone--over');
        dropZoneElement.querySelector('.drop-zone__thumb')!.remove();
        const promptElement: HTMLSpanElement = document.createElement('span');
        promptElement.classList.add('drop-zone__prompt');
        promptElement.innerHTML =
            'Lege es hier ab <br>oder klicke zum Auswählen';
        dropZoneElement.appendChild(promptElement);
    });
}

function removeJustThumbnail() {
    const überschriftValue: string = überschrift.value;
    const inhaltValue: string = inhalt.value;
    const tagsValue: string = tags.value;

    (document.getElementById('Abbrechen') as HTMLInputElement).click();

    überschrift.value = überschriftValue;
    inhalt.value = inhaltValue;
    tags.value = tagsValue;
}

function Editor() {
    (document.getElementById('editor') as HTMLDivElement).hidden = false;
    (
        document.getElementById('editorButton') as HTMLButtonElement
    ).style.background = '#16cc68';
    (document.getElementById('editorButton') as HTMLButtonElement).style.color =
        'white';
    (
        document.getElementById('previewButton') as HTMLButtonElement
    ).style.background = 'white';
    (
        document.getElementById('previewButton') as HTMLButtonElement
    ).style.color = '#16cc68';
    (document.getElementById('vorschau') as HTMLDivElement).hidden = true;
}

function Vorschau() {
    (document.getElementById('editor') as HTMLDivElement).hidden = true;
    (document.getElementById('editorButton') as HTMLButtonElement).style.color =
        '#16cc68';
    (
        document.getElementById('editorButton') as HTMLButtonElement
    ).style.backgroundColor = 'white';
    (
        document.getElementById('previewButton') as HTMLButtonElement
    ).style.color = 'white';
    (
        document.getElementById('previewButton') as HTMLButtonElement
    ).style.backgroundColor = '#16cc68';
    (document.getElementById('vorschau') as HTMLDivElement).hidden = false;

    if (!(überschrift.value == '')) {
        (
            document.querySelector('.msg.überschrift') as HTMLHeadingElement
        ).innerHTML = überschrift.value;
    } else {
        (
            document.querySelector('.msg.überschrift') as HTMLHeadingElement
        ).innerHTML = 'Noch keine Überschrift';
    }

    if (!(inhalt.value == '')) {
        (
            document.querySelector('.msg.inhalt') as HTMLParagraphElement
        ).innerHTML = parseMarkdown(inhalt.value);
    } else {
        (
            document.querySelector('.msg.inhalt') as HTMLParagraphElement
        ).innerHTML = 'Noch kein Textinhalt';
    }
    let tagsControlled: string = tags.value.replace(/ /g, '');

    if (!(tagsControlled == '')) {
        (
            document.querySelector('.msg.tags') as HTMLParagraphElement
        ).innerText = '#' + tags.value.replace(/ /g, ' #');
    } else {
        (
            document.querySelector('.msg.tags') as HTMLParagraphElement
        ).innerHTML = 'keine Tags';
    }
    let date: Date = new Date();
    let months = [
        'Januar',
        'Februar',
        'März',
        'April',
        'Mai',
        'Juni',
        'Juli',
        'August',
        'September',
        'Oktober',
        'November',
        'Dezember',
    ];
    (document.querySelector('.msg.datum') as HTMLParagraphElement).innerText =
        String(date.getDate()) +
        '. ' +
        months[date.getMonth()] +
        ' ' +
        String(date.getFullYear());

    (document.getElementById('bild') as HTMLImageElement).src = '';

    if (
        (document.getElementById('uploadedImage') as HTMLInputElement).files![0]
    ) {
        let fr: FileReader = new FileReader();
        fr.onload = function () {
            (document.getElementById('bild') as HTMLImageElement).src =
                fr.result as string;
        };
        fr.readAsDataURL(
            (document.getElementById('uploadedImage') as HTMLInputElement)
                .files![0]
        );
    }
}
