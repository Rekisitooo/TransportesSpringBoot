import { temporalErrorAlert } from './alert/GenericErrorAlert.js';
import { changeElementDisplayNone, changeElementClass } from './TemplateCrudCommons.js';
import { addPassengerInDriverTransportsTable } from './TemplateCrudTransportCRUD.js';

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
    const dataGetPassengers = {
        transportDateId : data[0]['transportDateCode'],
        driverId : data[0]['involvedCommunicatedId'],
    };
    let passengersInserted = 0;

    $.ajax({
        type: 'GET',
        url:'/t/getPassengersForDriverByDate',
        data: dataGetPassengers,
        success: function(response) {
            if (response !== null || response !== undefined || response.data.length !== 0) {
                for (const oData of response.data) {
                    data[0]['passengerCode'] = oData.passengerCode;
                    ajaxRequestCreateInvolvedCommunication(data, alertIcon, driverPassengersForDateDiv);
                    passengersInserted++;
                }
            }
        },
        error : showCommunicationError()
    });

    if (passengersInserted === 0) {
        ajaxRequestCreateInvolvedCommunication(data, alertIcon, driverPassengersForDateDiv);
    }
}

function ajaxRequestCreateInvolvedCommunication(data, alertIcon, driverPassengersForDateDiv) {
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url:'/involvedCommunication/createDriverCommunication',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response) {
            hideCommunicationAlertIcon(alertIcon);
            addPassengerInDriverTransportsTable(
                data.transportDateCode,
                data.driverCode,
                data.passengerCode,
                passengerFullName,
                driverPassengersForDateDiv
            );
        },
        error: showCommunicationError()
    })
}

function updateInvolvedCommunications(data, alertIcon, driverPassengersForDateDiv) {
    $.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url:'/involvedCommunication',
        data: JSON.stringify(data),
        dataType: 'json',
        success: createInvolvedCommunication(data, alertIcon, driverPassengersForDateDiv),
        error: temporalErrorAlert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.")
    })
}

function communicateTransport(data, alertIcon, driverPassengersForDateDiv) {
    $.ajax({
        type: 'GET',
        url:'/involvedCommunication/get',
        data: data,
        success: function(response) {
            if (response === null || response === undefined || response.data.length === 0) {
                createInvolvedCommunication(response.data, alertIcon, driverPassengersForDateDiv);
            } else {
                updateInvolvedCommunications(response.data, alertIcon, driverPassengersForDateDiv);
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
                    communicateTransport(data, $(this), driverPassengersForDateDiv);
                });
            }
        );
    }
);