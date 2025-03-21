function deleteDate() {
    const eventId = $(this).attr("data-b");
    const transportDateId = $(this).attr("data-a");

    let dayType = '';
    if (eventId != null && eventId != '') {
        dayType = 'event';
    } else if (transportDateId != null && transportDateId != '') {
        dayType = 'transportDate';
    }

    $.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url: '/' + dayType,
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response) {
            Swal.fire({
              title: "¡Evento eliminado con éxito!",
              icon: "success",
              draggable: false
            });
        },
        error: function(xhr, status, error) {
            Swal.fire({
              icon: "error",
              text: "Ha habido un error al eliminar el evento.",
            });
        }
    });
}

$(document).ready(
    function () {
        $('#delete-icon').on('click', deleteDate);
    }
);