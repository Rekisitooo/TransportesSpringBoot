export function genericErrorAlert() {
    Swal.fire({
        icon: "error",
        timer: 2000,
        text: "Ha habido un error.",
    });
}

export function temporalErrorAlert(error) {
    Swal.fire({
        icon: "error",
        timer: 2000,
        text: error,
    });
}