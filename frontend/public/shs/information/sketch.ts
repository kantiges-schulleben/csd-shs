export {};
function loadEndDate() {
    fetch(`${backend}/api/shs/end-date`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
        },
    })
        .then((response: Response) => {
            if (!response.ok) {
                throw new Error(
                    `HTTP ${response.status} ${
                        response.statusText
                    } - ${response.text()}`
                );
            }
            return response.text();
        })
        .then((endDate: string) => {
            console.log(endDate);
            const date: Date = new Date(endDate);
            console.log(date);
            const day = ('0' + date.getDate()).slice(-2);
            const month = ('0' + (date.getMonth() + 1)).slice(-2);
            (
                document.getElementById('lblEnddate') as HTMLLabelElement
            ).innerText = `${day}.${month}.${date.getFullYear()}`;
        })
        .catch((e: any) => {
            console.error(e);
            (document.getElementById('counter') as HTMLLabelElement).innerHTML =
                '<i>Das Einschreibungsende konnte nicht abgerufen werden</i>';
        });
}
