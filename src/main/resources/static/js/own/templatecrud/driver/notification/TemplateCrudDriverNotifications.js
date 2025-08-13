import { temporalErrorAlert } from '../../alert/GenericErrorAlert.js';
import { changeElementClass } from '../../TemplateCrudCommons.js';
import { getInvolvedNotifications } from '../../../NotificationAJAX.js';
import { getDriverPassengersForDate } from '../../../TransportAJAX.js';

$(function() {
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

                // if icon is red, the driver has not been notified of its current transport
                if ($(this).attr('class').includes('text-danger')) {
                    // get if driver had notifications
                    const driverNotifications = await getInvolvedNotifications(data);
                    if (driverNotifications?.data?.length) {
                        // deletes driver notifications
                        await ajaxRequestDeleteDriverNotification(data);
                    }
                    await notifyTransport(data, $(this));

                // if icon is blue, the driver has been notified of its current transport
                } else if ($(this).attr('class').includes('text-primary')) {
                    await deleteDriverNotification(data, $(this));
                }

                // hide or show the button to mark all the month transports have been notified to the driver
                const templateId = $('#templateTitle').attr('data-template-id');
                await showHideDriverNotificationsButton(driverId, templateId);
            });
        }
    );
});

async function createDriverNotifications(data, alertIcon) {
    try {
        // gets the transports already assigned to the driver in that date
        const response = await getDriverPassengersForDate(data);

        // creates a notification for each passenger and changes the icon to blue
        if (response?.data?.length > 0) {
            await Promise.all(
                response.data.map(async (transport) => {
                    const newData = {
                        ...data,
                        notificationDate: Date.now(),
                        driverCode: transport.transport.transportKey.driverId,
                        passengerCode: transport.transport.transportKey.passengerId
                    };
                return createDriverNotification(newData, alertIcon);
                })
           );

        // creates a notification without passenger and changes the icon to blue
        } else {
            const newData = {
                ...data,
                notificationDate: Date.now(),
                driverCode: data.notifiedInvolvedId,
                passengerCode: null
            };
            await createDriverNotification(newData, alertIcon);
        }

    } catch (error) {
        showNotificationError();
    }
}

/**
 * Creates driver notification in db and changes the icon
 */
async function createDriverNotification(data, alertIcon) {
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

/**
 * Deletes the driver notification in db and changes icon to red
 */
async function deleteDriverNotification(data, alertIcon) {
    if (await ajaxRequestDeleteDriverNotification(data)) {
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
        const response = await getInvolvedNotifications(data);

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

/**
 * Shows the notification icon to indicate the
 * driver has been notified the transports for the whole month if there are
 * two or more transports without notification. If not, it remains hidden.
 * @param {number} driverId - Contains the drivers id
 * @param {number} templateId - Contains the template id
 */
export async function showHideDriverNotificationsButton(driverId, templateId) {
    try {
        const response = await $.ajax({
            type: 'GET',
            url: '/t/getDriverTransportsWithoutNotification',
            data: {
                templateId : templateId,
                driverId : driverId
            }
        });

        // if the driver has more than 2 transports without notification, the icon shows
        if (response?.data?.length < 2) {
            $('#driverTransportsTable tr td:first-child div[id=markAsNotifiedDriverButtonDiv_' + driverId + '] i')
                .addClass('d-none');

        } else {
            $('#driverTransportsTable tr td:first-child div[id=markAsNotifiedDriverButtonDiv_' + driverId + '] i')
                .removeClass('d-none');
        }

    } catch (error) {
        console.error('Error updating drivers whole month notification button');
    }
}

/**
 * Changes the driver notification icon based on the transport deletion.
 * @param {Object} data - Contains transportDateCode and driverId
 * @param {jQuery} driverNotificationIcon - The warning icon element for the driver
 */
export async function changeDriverNotifIconOnTransportDeletion(data, driverNotificationIcon) {

    if (driverNotificationIcon.hasClass("text-danger")) {
        const driverNotifications = await getInvolvedNotifications(
            {transportDateCode : data.transportDateCode, notifiedInvolvedId : data.driverId});

        // if there were not previous notifications, icon should be red
        if (driverNotifications?.data?.length) {
            const getInvolvedNotificationsData = {
                transportDateCode : data.transportDateCode,
                notifiedInvolvedId : data.driverId
            }

            const driverNotifications = await getInvolvedNotifications(getInvolvedNotificationsData).data;
            const driverTransports = await getDriverPassengersForDate(data).data;

            if (isNotificationEqualToDriversActualTransport(driverNotifications, driverTransports, data)) {
                driverNotificationIcon.removeClass("text-danger");
                driverNotificationIcon.addClass("text-primary");
            }
        }

    } else if (driverNotificationIcon.hasClass("text-primary")) {
        driverNotificationIcon.removeClass("text-primary");
        driverNotificationIcon.addClass("text-danger");
    }

}

/**
 * Updates the warning icon visibility for a driver based on their notification status.
 * Shows the icon if the passenger is not in the driver's notifications or if there are no notifications.
 * @param {Object} data - Contains transportDateCode and driverId
 * @param {jQuery} driverNotificationIcon - The warning icon element for the driver
 */
export async function changeDriverNotifIconOnDriverSelection(data, driverNotificationIcon) {

    const getInvolvedNotificationsData = {
        transportDateCode : data.transportDateCode,
        notifiedInvolvedId : data.driverId
    }

    const driverNotifications = await getInvolvedNotifications(getInvolvedNotificationsData).data;
    const driverTransports = await getDriverPassengersForDate(data).data;

    if (isNotificationEqualToDriversActualTransport(driverNotifications, driverTransports, data)) {
        driverNotificationIcon.removeClass("text-danger");
        driverNotificationIcon.addClass("text-primary");
    } else {
        driverNotificationIcon.removeClass("text-primary");
        driverNotificationIcon.addClass("text-danger");
    }
 }

/**
 * Gets if the driver has the same passengers assigned to transport than in the current notification .
 * @param {Array} driverNotifications - Contains the driver's notifications (InvolvedTransportNotification
 * @param {Array} driverTransports - Contains the driver's transports (DtoGetPassengersForDriverByDate)
 * @param {Object} data - Contains driverId and transportDateId
 */
function isNotificationEqualToDriversActualTransport(driverNotifications, driverTransports, data) {
    if (driverNotifications === undefined || driverTransports === undefined) {
        return false;
    }

    if (driverNotifications?.length !== driverTransports?.length) {
        return false;
    }

    const driverNotificationValues  = driverNotifications   .map(obj => obj['passengerCode']).sort();
    const driverTransportsValues    = driverTransports      .map(obj => obj['transport.transportKey.passengerId']).sort();

    return driverNotificationValues.every((value, index) => value === driverTransportsValues[index]);
}