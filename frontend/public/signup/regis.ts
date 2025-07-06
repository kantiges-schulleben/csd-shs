const inputs = document.querySelectorAll('.input');
// TODO set type
function addcl(e: any) {
    let parent = e.parentNode.parentNode;
    parent.classList.add('focus');
}
// TODO set type
function remcl(e: any) {
    let parent = e.parentNode.parentNode;
    if (e.value == '') {
        parent.classList.remove('focus');
    }
}

inputs.forEach((input) => {
    input.addEventListener('focus', addcl);
    input.addEventListener('blur', remcl);
});

function signup() {
    $.post(
        '/signup',
        {
            firstname: (
                document.getElementById('firstname') as HTMLInputElement
            ).value,
            lastname: (document.getElementById('lastname') as HTMLInputElement)
                .value,
            username: (
                document.getElementById('txtUsername') as HTMLInputElement
            ).value,
            password: (document.getElementById('password') as HTMLInputElement)
                .value,
            passwdconfirm: (
                document.getElementById('passwdconfirm') as HTMLInputElement
            ).value,
            mail: (document.getElementById('mail') as HTMLInputElement).value,
        },
        (data: { [key: string]: any }) => {
            if (data.success === true) {
                window.location.assign('/');
            }
        }
    );
}

function login() {
    $.post(
        '/login',
        {
            username: (document.getElementById('username') as HTMLInputElement)
                .value,
            password: (document.getElementById('password') as HTMLInputElement)
                .value,
        },
        (data: { [key: string]: any }) => {
            if (data.success === true) {
                window.location.assign('/');
            }
        }
    );
}
