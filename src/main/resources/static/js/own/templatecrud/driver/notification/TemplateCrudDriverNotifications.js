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
                    notifiedInvolvedId : $('th[id=' + driverThId + ']').attr('data-d')
                };

                if ($(this).attr('class').includes('text-danger')) {
                    const driverNotifications = await getDriverNotifications(data);
                    if (driverNotifications?.data?.length) {
                        await ajaxRequestDeleteDriverNotification(data);
                        $(this).addClass('d-none');

                    } else {
                        notifyTransport(data, $(this));
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
                driverCode: data.notifiedInvolvedId,
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
        changeAlertIconToNotified(alertIcon);

    } catch (error) {
        showNotificationError();
    }
}

async function deleteDriverNotification(data, alertIcon) {
    if (ajaxRequestDeleteDriverNotification(data)) {
        changeAlertIconToNotNotified(alertIcon);
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
        if (isNotificationDeleted) {
            const notification = {
                transportDateCode: data.transportDateCode,
                notifiedInvolvedId: data.notifiedInvolvedId
            };
            await createDriverNotifications(notification, alertIcon);
        }
        
    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.");
    }
}

async function notifyTransport(data, alertIcon) {
    try {
        const response = await getDriverNotifications(data);

        if (!response?.data?.length) {
            await createDriverNotifications(data, alertIcon);
        } else {
            await updateDriverNotifications(response.data[0], alertIcon);
        }
    } catch (error) {
        showNotificationError();
    }
}

function showNotificationError() {
    temporalErrorAlert("Ha ocurrido un error al indicar que se ha avisado del transporte al conductor.");
}

function changeAlertIconToNotified(alertIcon) {
    let notifyTransportIconClass = changeElementClass(alertIcon, 'text-primary', 'text-danger');
    alertIcon.attr('class', notifyTransportIconClass);
}

function changeAlertIconToNotNotified(alertIcon) {
    let notifyTransportIconClass = changeElementClass(alertIcon, 'text-danger', 'text-primary');
    alertIcon.attr('class', notifyTransportIconClass);
}

async function getDriverNotifications(data) {
    return await $.ajax({
        type: 'GET',
        url: '/involvedNotification/get',
        data: data
    });
}