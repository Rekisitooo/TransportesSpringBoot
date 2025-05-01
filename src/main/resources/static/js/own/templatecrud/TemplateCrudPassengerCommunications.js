import { genericErrorAlert } from './alert/GenericErrorAlert.js';
import { changeElementDisplayNone, changeElementClass } from './TemplateCrudCommons.js';

function showCommunicationError() {
    //TODO configurar la alerta para que se muestre en un idioma u otro
    window.alert("Ha ocurrido un error al indicar que se ha avisado del transporte al conductor.");
}

function hideCommunicationAlertIcon(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-danger', 'd-none');
    alertIcon.attr('class', communicateTransportIconClass);
}

function updateInvolvedCommunication(communication, alertIcon) {
    $.ajax({
        type: 'PATCH',
        contentType: 'application/json',
        url: '/involvedCommunication/updateDriver',
        data: JSON.stringify(communication),
        dataType: 'json',
        success : function(response) {
            if (response.status === 'ok') {
                hideCommunicationAlertIcon(alertIcon);
            } else {
                showCommunicationError();
            }
        },
        error : showCommunicationError()
    })
}

function createInvolvedCommunication(data, alertIcon) {
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url:'/t',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response){
            if (response.status === 'ok') {
                hideCommunicationAlertIcon(alertIcon);
            } else {
                showCommunicationError();
            }
        },
        error : showCommunicationError()
    })
}

function deleteCommunication(data, actualSelect, driverPassengersDivId) {
    $.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url:'/t',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response){
           //TODO complete
        },
        error: function(e) {
            //TODO configurar la alerta para que se muestre en un idioma u otro
            window.alert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.");
        }
    })
}

function communicateTransport(data, alertIcon) {
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url:'/involvedCommunication/get',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response){
            //recoger los pasajeros que tiene
            if (response === null || response === undefined) {
                //por cada pasajero, crear un aviso
                createInvolvedCommunication(data, alertIcon);
            } else {
                //borrar todos los avisos que tenga el conductor para esta fecha
                //por cada pasajero, crear un aviso
                updateInvolvedCommunication(response, alertIcon);
            }
        },
        error: function(e) {}
    });
}

$(document).ready(
    function () {
        $('#driverTransportsTable i[class*=exclamation-circle]').each(
            function () {
                $(this).on('click', function() {
                    const driverThId = $(this).attr('data-driver-th');
                    const dataDateTd = $(this).attr('data-date-td');
                    const data = {
                        transportDateCode : $('td[id=' + dataDateTd + ']').attr('data-date'),
                        involvedCommunicatedId : $('th[id=' + driverThId + ']').attr('data-d'),
                    };

                    communicateTransport(data, this);
                });
            }
        );
    }
);