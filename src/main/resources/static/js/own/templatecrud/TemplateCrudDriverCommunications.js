import { temporalErrorAlert } from './alert/GenericErrorAlert.js';
import { changeElementClass } from './TemplateCrudCommons.js';

$(function() {
    $('#driverTransportsTable i[class*=exclamation-circle]').each(
        function () {
            $(this).on('click', function() {
                if ($(this).attr('class').includes('text-danger')) {
                    const driverThId = $(this).attr('data-driver-th');
                    const dataDateTd = $(this).attr('data-date-td');
                    const data = {
                        transportDateCode : $('td[id=' + dataDateTd + ']').attr('data-date-id'),
                        involvedCommunicatedId : $('th[id=' + driverThId + ']').attr('data-d')
                    };

                    const driverPassengersForDateDiv = $('#' + $(this).attr('data-passengers-div'));
                    communicateTransport(data, $(this), driverPassengersForDateDiv);
                }
            });
        }
    );
});

async function createInvolvedCommunications(data, alertIcon, driverPassengersForDateDiv) {
    try {
        const response = await $.ajax({
            type: 'GET',
            url: '/t/getPassengersForDriverByDate',
            data: data
        });

        if (response?.data?.length > 0) {
            await Promise.all(response.data.map(async (transport) => {
                const newData = {
                    ...data,
                    communicationDate: Date.now(),
                    driverCode: transport.transport.transportKey.driverId,
                    passengerCode: transport.transport.transportKey.passengerId
                };
                return ajaxRequestCreateInvolvedCommunication(newData, alertIcon, driverPassengersForDateDiv, transport.passengerFullName);
            }));
        } else {
            const newData = {
                ...data,
                communicationDate: Date.now(),
                driverCode: data.involvedCommunicatedId,
                passengerCode: null
            };
            await ajaxRequestCreateInvolvedCommunication(newData, alertIcon, driverPassengersForDateDiv, null);
        }
    } catch (error) {
        showCommunicationError();
    }
}

async function ajaxRequestCreateInvolvedCommunication(data, alertIcon, driverPassengersForDateDiv, passengerFullName) {
    try {
        await $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/involvedCommunication/createDriverCommunication',
            data: JSON.stringify(data),
            dataType: 'json'
        });
        changeAlertIconToCommunicated(alertIcon);

    } catch (error) {
        showCommunicationError();
    }
}

async function updateInvolvedCommunications(data, alertIcon, driverPassengersForDateDiv) {
    try {
        const communication = {
            transportDateCode: data.transportDateCode,
            involvedCommunicatedId: data.involvedCommunicatedId
        };

        await $.ajax({
            type: 'DELETE',
            contentType: 'application/json',
            url: '/involvedCommunication',
            data: JSON.stringify(data),
            dataType: 'json'
        });
        
        await createInvolvedCommunications(communication, alertIcon, driverPassengersForDateDiv);
        
    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.");
    }
}

async function communicateTransport(data, alertIcon, driverPassengersForDateDiv) {
    try {
        const response = await $.ajax({
            type: 'GET',
            url: '/involvedCommunication/get',
            data: data
        });

        if (!response?.data?.length) {
            await createInvolvedCommunications(data, alertIcon, driverPassengersForDateDiv);
        } else {
            await updateInvolvedCommunications(response.data[0], alertIcon, driverPassengersForDateDiv);
        }
    } catch (error) {
        showCommunicationError();
    }
}

function showCommunicationError() {
    temporalErrorAlert("Ha ocurrido un error al indicar que se ha avisado del transporte al conductor.");
}

function changeAlertIconToCommunicated(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-primary', 'text-danger');
    alertIcon.attr('class', communicateTransportIconClass);
}

function changeAlertIconToNotCommunicated(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-danger', 'text-primary');
    alertIcon.attr('class', communicateTransportIconClass);
}