function check() {
    $.get(
        `/checkUsernameExists/${
            (document.getElementById('txtUsername') as HTMLInputElement).value
        }`,
        (result: { [key: string]: any }) => {
            (document.getElementById('submit') as HTMLInputElement).disabled =
                result.exists;
            (
                document.getElementById('submit') as HTMLInputElement
            ).classList.replace(
                result.exists == false ? 'btn_disable' : 'btn',
                result.exists == true ? 'btn_disable' : 'btn'
            );
        }
    );
}
