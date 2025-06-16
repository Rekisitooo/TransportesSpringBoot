import { temporalErrorAlert } from '../alert/GenericErrorAlert.js';
import { changeElementClass } from '../TemplateCrudCommons.js';

$(function() {
    $('#passengerTransportsTable i[class*=exclamation-circle]').each(
        function () {
            $(this).on('click', function() {
                const passengerThId = $(this).attr('data-passenger-th');
                const dataDateTd = $(this).attr('data-date-td');
                const data = {
                    transportDateCode : $('td[id=' + dataDateTd + ']').attr('data-date-id'),
                    involvedCommunicatedId : $('th[id=' + passengerThId + ']').attr('data-t')
                };

                if ($(this).attr('class').includes('text-danger')) {
                    const driverSelectSelector = $(this).attr('data-drivers-selector');
                    const driverSelect = $('#' + driverSelectSelector);
                    const driverSelectedId = driverSelect.val();

                    communicateTransport(data, $(this), driverSelectedId);
                } else if ($(this).attr('class').includes('text-muted')) {
                    deletePassengerCommunication(data, $(this));
                }
            });
        }
    );
});

async function createPassengerCommunication(data, alertIcon, driverSelectedId) {
    try {
        const response = await $.ajax({
            type: 'GET',
            url: '/t/getPassengersForDriverByDate',
            data: data
        });

        const newData = {
            ...data,
            communicationDate: Date.now(),
            driverCode: driverSelectedId,
            passengerCode: data.involvedCommunicatedId
        };
        const passengerCommunicationCreationResponse = await ajaxRequestCreatePassengerCommunication(newData, alertIcon);
        if (passengerCommunicationCreationResponse) {
            await changeAlertIconToCommunicated(alertIcon);
            await addPassengerCommunicationToCommunicationsTable(response.data, alertIcon);
        }
    } catch (error) {
        showCommunicationError();
    }
}

async function ajaxRequestCreatePassengerCommunication(data, alertIcon) {
    try {
        await $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/involvedCommunication/createCommunication',
            data: JSON.stringify(data),
            dataType: 'json'
        });
        return true;

    } catch (error) {
        showCommunicationError();
        return false;
    }
}

async function ajaxRequestDeletePassengerCommunication(data) {
    try {
        await $.ajax({
            type: 'DELETE',
            contentType: 'application/json',
            url: '/involvedCommunication',
            data: JSON.stringify(data),
            dataType: 'json'
        });
        return true;

    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al indicar que el transporte se ha comunicado.");
        return false;
    }
}

async function deletePassengerCommunication(data, alertIcon) {
    if (ajaxRequestDeletePassengerCommunication(data)) {
        changeAlertIconToNotCommunicated(alertIcon);
    }
}

async function updatePassengerCommunications(data, alertIcon, driverSelectedId) {
    try {
        const isCommunicationDeleted = await ajaxRequestDeletePassengerCommunication(data);
        if (isCommunicationDeleted) {
            const communication = {
                transportDateCode: data.transportDateCode,
                involvedCommunicatedId: data.involvedCommunicatedId
            };
            await createPassengerCommunication(communication, alertIcon);
        }

    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.");
    }
}

async function communicateTransport(data, alertIcon, driverSelectedId) {
    try {
        const response = await $.ajax({
            type: 'GET',
            url: '/involvedCommunication/get',
            data: data
        });

        if (!response?.data?.length) {
            await createPassengerCommunication(data, alertIcon, driverSelectedId);
        } else {
            await updatePassengerCommunications(response.data[0], alertIcon, driverSelectedId);
        }
    } catch (error) {
        showCommunicationError();
    }
}

function showCommunicationError() {
    temporalErrorAlert("Ha ocurrido un error al indicar que se ha avisado del transporte al viajero.");
}

function changeAlertIconToCommunicated(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-muted', 'text-danger');
    alertIcon.attr('class', communicateTransportIconClass);
}

function changeAlertIconToNotCommunicated(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-danger', 'text-muted');
    alertIcon.attr('class', communicateTransportIconClass);
}