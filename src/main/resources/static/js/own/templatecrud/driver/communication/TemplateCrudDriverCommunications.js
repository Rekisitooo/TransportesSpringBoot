import { temporalErrorAlert } from '../../alert/GenericErrorAlert.js';
import { changeElementClass } from '../../TemplateCrudCommons.js';

$(function() {
    $('#driverTransportsTable i[class*=exclamation-circle]').each(
        function () {
            $(this).on('click', async function() {
                const driverThId = $(this).attr('data-driver-th');
                const dataDateTd = $(this).attr('data-date-td');
                const data = {
                    transportDateCode : $('td[id=' + dataDateTd + ']').attr('data-date-id'),
                    involvedCommunicatedId : $('th[id=' + driverThId + ']').attr('data-d')
                };

                if ($(this).attr('class').includes('text-danger')) {
                    const driverCommunications = await getDriverCommunications(data);
                    if (!driverCommunications?.data?.length) {
                        await ajaxRequestDeleteDriverCommunication(data);
                        $(this).addClass('d-none');

                    } else {
                        communicateTransport(data, $(this));
                    }

                } else if ($(this).attr('class').includes('text-muted')) {
                    deleteDriverCommunication(data, $(this));
                }
            });
        }
    );
});

async function createDriverCommunications(data, alertIcon) {
    try {
        const response = await $.ajax({
            type: 'GET',
            url: '/t/getDriverForPassengerByDate',
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
                return ajaxRequestCreateDriverCommunication(newData, alertIcon);
            }));
        } else {
            const newData = {
                ...data,
                communicationDate: Date.now(),
                driverCode: data.involvedCommunicatedId,
                passengerCode: null
            };
            await ajaxRequestCreateDriverCommunication(newData, alertIcon);
        }
    } catch (error) {
        showCommunicationError();
    }
}

async function ajaxRequestCreateDriverCommunication(data, alertIcon) {
    try {
        await $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/involvedCommunication/createCommunication',
            data: JSON.stringify(data),
            dataType: 'json'
        });
        changeAlertIconToCommunicated(alertIcon);

    } catch (error) {
        showCommunicationError();
    }
}

async function deleteDriverCommunication(data, alertIcon) {
    if (ajaxRequestDeleteDriverCommunication(data)) {
        changeAlertIconToNotCommunicated(alertIcon);
    }
}

async function ajaxRequestDeleteDriverCommunication(data) {
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

async function updateDriverCommunications(data, alertIcon) {
    try {
        const isCommunicationDeleted = await ajaxRequestDeleteDriverCommunication(data);
        if (isCommunicationDeleted) {
            const communication = {
                transportDateCode: data.transportDateCode,
                involvedCommunicatedId: data.involvedCommunicatedId
            };
            await createDriverCommunications(communication, alertIcon);
        }
        
    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.");
    }
}

async function communicateTransport(data, alertIcon) {
    try {
        const response = await getDriverCommunications(data);

        if (!response?.data?.length) {
            await createDriverCommunications(data, alertIcon);
        } else {
            await updateDriverCommunications(response.data[0], alertIcon);
        }
    } catch (error) {
        showCommunicationError();
    }
}

function showCommunicationError() {
    temporalErrorAlert("Ha ocurrido un error al indicar que se ha avisado del transporte al conductor.");
}

function changeAlertIconToCommunicated(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-muted', 'text-danger');
    alertIcon.attr('class', communicateTransportIconClass);
}

function changeAlertIconToNotCommunicated(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-danger', 'text-muted');
    alertIcon.attr('class', communicateTransportIconClass);
}

async function getDriverCommunications(data) {
    return await $.ajax({
        type: 'GET',
        url: '/involvedCommunication/get',
        data: data
    });
}