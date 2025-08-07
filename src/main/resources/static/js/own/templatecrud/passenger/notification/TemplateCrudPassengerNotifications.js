import { temporalErrorAlert } from '../../alert/GenericErrorAlert.js';
import { changeElementClass } from '../../TemplateCrudCommons.js';
import { getInvolvedNotifications } from '../../../NotificationAJAX.js';
import { getDriverForPassengerByDate } from '../../../TransportAJAX.js';

$(function() {
    $('#passengerTransportsTable i[class*=exclamation-circle]').each(
        function () {
            $(this).on('click', async function() {
                const passengerThId = $(this).attr('data-passenger-th');
                const passengerId = $('td[id=' + passengerThId + ']').attr('data-t');
                const dataDateTd = $(this).attr('data-date-td');
                const data = {
                    transportDateCode : $('td[id=' + dataDateTd + ']').attr('data-date-id'),
                    notifiedInvolvedId : passengerId
                };

                if ($(this).attr('class').includes('text-danger')) {
                    const driverSelectSelector = $(this).attr('data-drivers-selector');
                    const driverSelect = $('#' + driverSelectSelector);
                    const driverSelectedId = driverSelect.val();

                    // if now the passenger does not have driver
                    if (driverSelectedId === undefined || driverSelectedId === '') {
                        await ajaxRequestDeletePassengerNotification(data);

                        const passengerNotification = await getInvolvedNotifications(data);
                        // if it had a notification, button should show in red
                        if (passengerNotification?.data?.length) {
                            $(this).removeClass('text-primary');
                            $(this).addClass('text-danger');

                        } else {
                            $(this).addClass('d-none');
                        }

                    } else {
                        await notifyTransport(data, $(this), driverSelectedId);
                    }

                } else if ($(this).attr('class').includes('text-primary')) {
                    await deletePassengerNotification(data, $(this));
                }

                 // hide or show the button to mark all the month transports have been notified to the passenger
                const templateId = $('#templateTitle').attr('data-template-id');
                await showHidePassengerNotificationsButton(passengerId, templateId);
            });
        }
    );
});

async function notifyTransport(data, alertIcon, driverSelectedId) {
    try {
        const response = await $.ajax({
            type: 'GET',
            url: '/involvedTransportNotification/get',
            data: data
        });

        if (!response?.data?.length) {
            await createPassengerNotification(data, alertIcon, driverSelectedId);
        } else {
            await updatePassengerNotifications(response.data[0], alertIcon, driverSelectedId);
        }
    } catch (error) {
        showNotificationError();
    }
}

async function createPassengerNotification(data, alertIcon, driverSelectedId) {
    try {
        let getDriverForPassengerByDateData = {
            transportDateId: data.transportDateCode,
            passengerId: data.notifiedInvolvedId
        }
        const response = await getDriverForPassengerByDate(getDriverForPassengerByDateData);

        const newData = {
            ...data,
            notificationDate: Date.now(),
            driverCode: driverSelectedId,
            passengerCode: data.notifiedInvolvedId
        };

        const passengerNotificationCreationResponse = await ajaxRequestCreatePassengerNotification(newData, alertIcon);
        if (passengerNotificationCreationResponse) {
            await changeAlertIconToNotified(alertIcon);
        }
    } catch (error) {
        showNotificationError();
    }
}

async function updatePassengerNotifications(data, alertIcon, driverSelectedId) {
    try {
        const isNotificationDeleted = await ajaxRequestDeletePassengerNotification(data);
        if (isNotificationDeleted) {
            const notification = {
                transportDateCode: data.transportDateCode,
                notifiedInvolvedId: data.notifiedInvolvedId
            };
            await createPassengerNotification(notification, alertIcon, driverSelectedId);
        }

    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.");
    }
}

async function deletePassengerNotification(data, alertIcon) {
    if (await ajaxRequestDeletePassengerNotification(data)) {
        changeAlertIconToNotNotified(alertIcon);
    }
}

async function ajaxRequestCreatePassengerNotification(data, alertIcon) {
    try {
        await $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/involvedTransportNotification/createNotification',
            data: JSON.stringify(data),
            dataType: 'json'
        });
        return true;

    } catch (error) {
        showNotificationError();
        return false;
    }
}

async function ajaxRequestDeletePassengerNotification(data) {
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

function showNotificationError() {
    temporalErrorAlert("Ha ocurrido un error al indicar que se ha avisado del transporte al viajero.");
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
 * @param {number} passengerId - Contains the passenger id
 * @param {number} templateId - Contains the template id
 */
export async function showHidePassengerNotificationsButton(passengerId, templateId) {
    try {
        const response = await $.ajax({
            type: 'GET',
            url: '/t/getPassengerTransportsWithoutNotification',
            data: {
                templateId : templateId,
                passengerId : passengerId
            }
        });

        // if the driver has more than 2 transports without notification, the icon shows
        if ((response?.data?.length) < 2) {
            $('#passengerTransportsTable tr td:first-child div[id=markAsNotifiedPassengerButtonDiv_' + passengerId + '] i')
                .addClass('d-none');

        } else {
            $('#passengerTransportsTable tr td:first-child div[id=markAsNotifiedPassengerButtonDiv_' + passengerId + '] i')
                .removeClass('d-none');
        }
    } catch (error) {
        console.error('Error updating passengers whole month notification button');
    }
}

/**
 * Changes the passenger notification icon based on the transport deletion.
 * @param {Object} data - Contains passengerId (t), passengerId (p), and transportDateId (d)
 * @param {jQuery} passengerNotificationIcon - The warning icon element for the passenger
 */
export async function changePassengerNotifIconOnTransportDeletion(data, passengerNotificationIcon) {
    if (passengerNotificationIcon.hasClass("text-danger")) {
        const passengerNotifications = await getInvolvedNotifications(
            {transportDateCode : data.d, notifiedInvolvedId : data.t});

        // if there were no notifications, don't show the icon
        if (!passengerNotifications?.data?.length) {
            passengerNotificationIcon.addClass("d-none");
        }

    } else if (passengerNotificationIcon.hasClass("text-primary")) {
        passengerNotificationIcon.removeClass("text-primary");
        passengerNotificationIcon.addClass("text-danger");
    }

    // hide or show the button to mark all the month transports have been notified to the passenger
    const templateId = $('#templateTitle').attr('data-template-id');
    await showHidePassengerNotificationsButton(data.t, templateId);
}

/**
 * Updates the warning icon visibility for a passenger based on their notification status.
 * Shows the icon if the passenger is not in the passenger's notifications or if there are no notifications.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} passengerNotificationIcon - The warning icon element for the passenger
 */
export async function changePassengerNotifIconOnPassengerSelection(data, passengerNotificationIcon) {
    if (passengerNotificationIcon.hasClass("d-none")) {
        passengerNotificationIcon.removeClass("d-none");
        passengerNotificationIcon.removeClass("text-primary");
        passengerNotificationIcon.addClass("text-danger");

    } else {
        const driver = await getDriverForPassengerByDate({transportDateId : data.d, passengerId : data.t});
        const notifiedDriver = await getInvolvedNotifications(
            {transportDateCode : data.d, notifiedInvolvedId : data.t});

        const isSameDriverNotifiedThanTransport =
            (driver !== undefined) && (driver?.data?.transportKey?.driverId === notifiedDriver?.data[0]?.driverCode);

        if (isSameDriverNotifiedThanTransport) {
            passengerNotificationIcon.removeClass("text-danger");
            passengerNotificationIcon.addClass("text-primary");

        } else {
            passengerNotificationIcon.removeClass("text-primary");
            passengerNotificationIcon.addClass("text-danger");
        }
    }

    // hide or show the button to mark all the month transports have been notified to the passenger
    const templateId = $('#templateTitle').attr('data-template-id');
    await showHidePassengerNotificationsButton(data.t, templateId);
 }