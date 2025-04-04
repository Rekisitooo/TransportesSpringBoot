export function genericErrorAlert() {
    Swal.fire({
        icon: "error",
        timer: 2000,
        text: "Ha habido un error.",
    });
}

$(document).ready(genericErrorAlert);