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
                    const driverNotifications = await getDriverNotifications(data);
                    if (driverNotifications?.data?.length) {
                        await ajaxRequestDeleteDriverNotification(data);
                        $(this).addClass('d-none');

                    } else {
                        communicateTransport(data, $(this));
                    }

                } else if ($(this).attr('class').includes('text-primary')) {
                    deleteDriverNotification(data, $(this));
                }
            });
        }
    );
});

async function createDriverNotifications(data, alertIcon) {
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
                    notificationDate: Date.now(),
                    driverCode: transport.transport.transportKey.driverId,
                    passengerCode: transport.transport.transportKey.passengerId
                };
                return ajaxRequestCreateDriverNotification(newData, alertIcon);
            }));
        } else {
            const newData = {
                ...data,
                notificationDate: Date.now(),
                driverCode: data.involvedCommunicatedId,
                passengerCode: null
            };
            await ajaxRequestCreateDriverNotification(newData, alertIcon);
        }
    } catch (error) {
        showNotificationError();
    }
}

async function ajaxRequestCreateDriverNotification(data, alertIcon) {
    try {
        await $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/involvedNotification/createNotification',
            data: JSON.stringify(data),
            dataType: 'json'
        });
        changeAlertIconToCommunicated(alertIcon);

    } catch (error) {
        showNotificationError();
    }
}

async function deleteDriverNotification(data, alertIcon) {
    if (ajaxRequestDeleteDriverNotification(data)) {
        changeAlertIconToNotCommunicated(alertIcon);
    }
}

async function ajaxRequestDeleteDriverNotification(data) {
    try {
        await $.ajax({
            type: 'DELETE',
            contentType: 'application/json',
            url: '/involvedNotification',
            data: JSON.stringify(data),
            dataType: 'json'
        });
        return true;

    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al indicar que el transporte se ha comunicado.");
        return false;
    }
}

async function updateDriverNotifications(data, alertIcon) {
    try {
        const isNotificationDeleted = await ajaxRequestDeleteDriverNotification(data);
        if (isCommunicationDeleted) {
            const notification = {
                transportDateCode: data.transportDateCode,
                involvedCommunicatedId: data.involvedCommunicatedId
            };
            await createDriverCommunications(notification, alertIcon);
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
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-primary', 'text-danger');
    alertIcon.attr('class', communicateTransportIconClass);
}

function changeAlertIconToNotCommunicated(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-danger', 'text-primary');
    alertIcon.attr('class', communicateTransportIconClass);
}

async function getDriverCommunications(data) {
    return await $.ajax({
        type: 'GET',
        url: '/involvedCommunication/get',
        data: data
    });
}