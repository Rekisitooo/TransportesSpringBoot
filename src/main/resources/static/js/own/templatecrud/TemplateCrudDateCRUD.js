import { loadDatePicker } from './TemplateCrudAddNewDateModal.js';

const EVENT_DAY_TYPE = 'event';
const TRANSPORT_DATE_DAY_TYPE = 'transportDate';


function deleteDate() {
    const eventId = $(this).attr("data-b");
    const transportDateId = $(this).attr("data-a");
    let id;

    let dayType = '';
    if (eventId != null && eventId != '') {
        dayType = EVENT_DAY_TYPE;
        id = eventId;
    } else if (transportDateId != null && transportDateId != '') {
        dayType = TRANSPORT_DATE_DAY_TYPE;
        id = transportDateId;
    }

    deleteAjaxRequest(dayType, id, this);
}

function deleteAjaxRequest(dayType, id, element) {
    $.ajax({
        type: 'DELETE',
        url: '/' + dayType + "/" + id,
        success: function(response) {
            deleteSuccess(response, element, dayType);
        },
        error: function(xhr, status, error) {
            deleteError(xhr, status, error);
        }
    });
}

function deleteSuccess(response, element, dayType) {
    if (dayType == EVENT_DAY_TYPE) {
        deleteDateColumns(element);
    } else if (dayType == TRANSPORT_DATE_DAY_TYPE) {
        //T0DO disable inputs on passengers table
    }

    loadDatePicker();

    Swal.fire({
      title: "¡Operación realizada con éxito!",
      icon: "success",
      draggable: false
    });
}

function deleteDateColumns(element) {
    let dataDate = $(element).parent().attr('data-date');
    $('[data-date=' + dataDate + ']').remove();
}

function deleteError(xhr, status, error) {
    Swal.fire({
      icon: "error",
      text: "Ha ocurrido un error, no se ha podido eliminar.",
    });
}

$(document).ready(
    function () {
        $('i[data-name=date-delete-icon]').each(
            function() {
                $(this).on('click', deleteDate);
            }
        );
    }
);