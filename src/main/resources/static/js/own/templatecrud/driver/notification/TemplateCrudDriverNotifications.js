import { temporalErrorAlert } from '../../alert/GenericErrorAlert.js';
import { changeElementClass } from '../../TemplateCrudCommons.js';

$(function() {
    // on load, hide or show all the buttons to mark all the month transports have been notified to the driver
    showHideCheckAllNotificationsButton();

    $('#driverTransportsTable i[class*=exclamation-circle]').each(
        function () {
            $(this).on('click', async function() {
                const driverThId = $(this).attr('data-driver-th');
                const driverId = $('td[id=' + driverThId + ']').attr('data-d');
                const dataDateTd = $(this).attr('data-date-td');
                const data = {
                    transportDateCode : $('td[id=' + dataDateTd + ']').attr('data-date-id'),
                    notifiedInvolvedId : driverId
                };

                if ($(this).attr('class').includes('text-danger')) {
                    const driverNotifications = await getDriverNotifications(data);
                    if (driverNotifications?.data?.length) {
                        await ajaxRequestDeleteDriverNotification(data);
                        $(this).addClass('d-none');

                    } else {
                        await notifyTransport(data, $(this));
                    }

                } else if ($(this).attr('class').includes('text-primary')) {
                    await deleteDriverNotification(data, $(this));
                }

                // hide or show the button to mark all the month transports have been notified to the driver
                await showHideDriverNotificationsButton(driverId);
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
            url: '/involvedTransportNotification/createNotification',
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
            url: '/involvedTransportNotification',
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
        url: '/involvedTransportNotification/get',
        data: data
    });
}

/**
 * Shows the notification icon to indicate the
 * driver has been notified the transports for the whole month if there are
 * two or more icons in red. If not, it remains hidden.
 */
async function showHideCheckAllNotificationsButton() {
    const driverTransportsTableCellsList = $('#driverTransportsTable tr td:first-child');

    // for each passenger the table
    for (let i = 0; i < driverTransportsTableCellsList.length; i++) {
        const driverId = $(driverTransportsTableCellsList[i]).attr('data-d');

        showHideDriverNotificationsButton(driverId);
    }

}

/**
 * Shows the notification icon to indicate the
 * driver has been notified the transports for the whole month if there are
 * two or more icons in red. If not, it remains hidden.
 * @driverId
 */
async function showHideDriverNotificationsButton(driverId) {
    const driverTransportsNotificationIconDivList = document.querySelectorAll('#driverTransportsTable tr td div[id*=notificationIcon_' + driverId + '_]');
    const driverTransportsNotificationIconList = document.querySelectorAll('#driverTransportsTable tr td div[id*=notificationIcon_' + driverId + '_] i');
    let driverRedNotifIconCount = 0;

    // count the red notification icons
    for (let j = 0; j < driverTransportsNotificationIconDivList.length; j++) {
        const transportNotifIconDiv = driverTransportsNotificationIconDivList[j];
        const transportNotifIcon = driverTransportsNotificationIconList[j];

        // driver assist and needs transport
        const isIconDivShown = !transportNotifIconDiv.classList.contains('d-none');
        // driver does not have a transport and notification
        const isIconShown = !transportNotifIcon.classList.contains('d-none')
        const isIconRed = transportNotifIcon.classList.contains('text-danger');
        if (isIconDivShown && isIconShown && isIconRed) {
            driverRedNotifIconCount++;
        }
    }

    // if the driver has less than two red notification icons, the notification button is not shown
    if (driverRedNotifIconCount < 2) {
        $('#driverTransportsTable tr td:first-child div[id*=markAsNotifiedDriverButtonDiv_' + driverId + ']')
            .addClass('d-none');

    } else {
        $('#driverTransportsTable tr td:first-child div[id*=markAsNotifiedDriverButtonDiv_' + driverId + ']')
            .removeClass('d-none');
    }
}