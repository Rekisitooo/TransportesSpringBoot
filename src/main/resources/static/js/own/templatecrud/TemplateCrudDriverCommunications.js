import { temporalErrorAlert } from './alert/GenericErrorAlert.js';
import { changeElementDisplayNone, changeElementClass } from './TemplateCrudCommons.js';

function showCommunicationError() {
    //TODO configurar la alerta para que se muestre en un idioma u otro
    temporalErrorAlert("Ha ocurrido un error al indicar que se ha avisado del transporte al conductor.");
}

function hideCommunicationAlertIcon(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-danger', 'd-none');
    alertIcon.attr('class', communicateTransportIconClass);
}

function showCommunicationAlertIcon(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'd-none', 'text-danger');
    alertIcon.attr('class', communicateTransportIconClass);
}

function createInvolvedCommunication(data, alertIcon, driverPassengersForDateDiv) {
    let passengersInserted = 0;
    data = {
        key : {
            transportDateCode : data.transportDateCode,
            involvedCommunicatedId : data.involvedCommunicatedId
        },
        driverCode : data.involvedCommunicatedId,
        passengerCode : null,
        communicationDate : Date.now(),
        involvedCommunicatedId : data.involvedCommunicatedId,
        transportDateCode : data.transportDateCode
    }

    $(driverPassengersForDateDiv).find('div[class=row] span').each(
        function() {
            data['passengerCode'] = $(this).attr('data-p-span-id');
            ajaxRequestCreateInvolvedCommunication(data, alertIcon);
            passengersInserted++;
        }
    );

    if (passengersInserted === 0) {
        ajaxRequestCreateInvolvedCommunication(data, alertIcon);
    }
}

function ajaxRequestCreateInvolvedCommunication(data, alertIcon) {
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url:'/involvedCommunication/createDriverCommunication',
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

function ajaxRequestDeleteCommunication(data, alertIcon) {
    $.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url:'/involvedCommunication',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response){
           showCommunicationAlertIcon(alertIcon);
        },
        error: function(e) {
            //TODO configurar la alerta para que se muestre en un idioma u otro
            temporalErrorAlert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.");
        }
    })
}

function communicateTransport(data, alertIcon, driverPassengersForDateDiv) {
    $.ajax({
        type: 'GET',
        url:'/involvedCommunication/get',
        data: data,
        success: function(response) {
            if (response === null || response === undefined || response.data.length === 0) {
                createInvolvedCommunication(data, alertIcon, driverPassengersForDateDiv);
            } else {
                ajaxRequestDeleteCommunication(data, alertIcon);
                createInvolvedCommunication(data, alertIcon, driverPassengersForDateDiv);
            }
        },
        error : showCommunicationError()
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
                        transportDateCode : $('td[id=' + dataDateTd + ']').attr('data-date-id'),
                        involvedCommunicatedId : $('th[id=' + driverThId + ']').attr('data-d')
                    };

                    const driverPassengersForDateDiv = $('#' + $(this).attr('data-passengers-div'));
                    communicateTransport(data, this, driverPassengersForDateDiv);
                });
            }
        );
    }
);