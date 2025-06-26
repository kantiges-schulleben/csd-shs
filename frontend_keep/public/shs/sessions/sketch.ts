function createSession() {
    $.get('/whiteboard/createSession', (data: {[key: string]: any}) => {
        if (data.success) {
            console.log(data.sessionLink);
            window.location.assign(data.sessionLink);
        } else {
            alert('Leider konnte keine Session erstellt werden. Bitte melde dich vorher bei ShS an.');
        }
    });
}
