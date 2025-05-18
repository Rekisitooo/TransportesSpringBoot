import { temporalErrorAlert } from './alert/GenericErrorAlert.js';
import { changeElementClass } from './TemplateCrudCommons.js';

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
                    communicateTransport(data, $(this), driverPassengersForDateDiv);
                });
            }
        );
    }
);

function createInvolvedCommunications(data, alertIcon, driverPassengersForDateDiv) {
    // data = { transportDateCode, involvedCommunicatedId }
    $.ajax({
        type: 'GET',
        url:'/t/getPassengersForDriverByDate',
        data: data,
        success: function(response) {
            if (response !== null && response !== undefined && (response.data !== null && response.data.length > 0) ) {
                for (const transport of response.data) {
                    data['communicationDate'] = Date.now();
                    data['driverCode'] = transport.transport.transportKey.driverId;
                    data['passengerCode'] = transport.transport.transportKey.passengerId;
                    ajaxRequestCreateInvolvedCommunication(data, alertIcon, driverPassengersForDateDiv, transport.passengerFullName);
                }
            } else {
                data['communicationDate'] = Date.now();
                data['driverCode'] = data.involvedCommunicatedId;
                data['passengerCode'] = null;
                ajaxRequestCreateInvolvedCommunication(data, alertIcon, driverPassengersForDateDiv, null);
            }
        },
        error : showCommunicationError()
    });
}

function ajaxRequestCreateInvolvedCommunication(data, alertIcon, driverPassengersForDateDiv, passengerFullName) {
    //data is full
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url:'/involvedCommunication/createDriverCommunication',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response) {
            hideCommunicationAlertIcon(alertIcon);
        },
        error: showCommunicationError()
    })
}

function updateInvolvedCommunications(data, alertIcon, driverPassengersForDateDiv) {
    //data is full
    const communication = {
        transportDateCode : data['transportDateCode'],
        involvedCommunicatedId : data['involvedCommunicatedId']
    };

    $.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url:'/involvedCommunication',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response) {
            createInvolvedCommunications(communication, alertIcon, driverPassengersForDateDiv);
        },
        error: temporalErrorAlert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.")
    });
}

function communicateTransport(data, alertIcon, driverPassengersForDateDiv) {
    // data = { transportDateCode, involvedCommunicatedId }
    $.ajax({
        type: 'GET',
        url:'/involvedCommunication/get',
        data: data,
        success: function(response) {
            if (response === null || response === undefined || !response.data || response.data.length === 0) {
                createInvolvedCommunications(data, alertIcon, driverPassengersForDateDiv);
            } else {
                updateInvolvedCommunications(response.data[0], alertIcon, driverPassengersForDateDiv);
            }
        },
        error : showCommunicationError()
    });
}

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

