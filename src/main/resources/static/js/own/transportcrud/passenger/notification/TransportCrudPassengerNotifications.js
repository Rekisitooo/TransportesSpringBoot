import { temporalErrorAlert } from '../../alert/GenericErrorAlert.js';
import { changeElementClass } from '../../TransportCrudCommons.js';
import { getInvolvedNotifications, updatePassengerNotification, createPassengerNotification, deletePassengerNotification } from '../../../NotificationAJAX.js';
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
                        
                    await notifyTransport(data, $(this), driverSelectedId);

                } else if ($(this).attr('class').includes('text-primary')) {
                    await deleteNotification(data, $(this));
                }

                 // hide or show the button to mark all the month transports have been notified to the passenger
                const templateId = $('#templateTitle').attr('data-template-id');
                await showHidePassengerNotificationsButton(passengerId, templateId);
            });
        }
    );
});

/**
 * Checks if a notification exists for the passenger.
 * If it exists, updates it with the new driver.
 * If it does not exist, creates a new one.
 * 
 * The icon is changed if the operation is successful.
 * 
 * @param {Object} data 
 * @param {Object} alertIcon 
 * @param {number} driverSelectedId 
 */
async function notifyTransport(data, alertIcon, driverSelectedId) {
    try {
        const response = await getInvolvedNotifications(data);

        if (!response?.data?.length) {
            await createNotification(data, alertIcon, driverSelectedId);
        } else {
            await updatePassengerNotifications(response.data[0], alertIcon, driverSelectedId);
        }

    } catch (error) {
        showNotificationError();
    }
}

/**
 * Creates a new passenger notification in db
 * and changes the alert icon if successful.
 * 
 * @param {Object} data 
 * @param {Object} alertIcon 
 * @param {number} driverSelectedId 
 */
async function createNotification(data, alertIcon, driverSelectedId) {
    try {
        const newData = {
            ...data,
            notificationDate: new Date().toJSON(),
            driverCode: driverSelectedId,
            passengerCode: data.notifiedInvolvedId
        };

        if (await createPassengerNotification(newData, alertIcon) == null) {
            showNotificationError();
        } else {
            changeAlertIconToNotified(alertIcon);
        }

    } catch (error) {
        showNotificationError();
    }
}

/**
 * Updates the passenger notification with a new driver in db
 * and changes the alert icon if successful.
 * 
 * @param {*} data 
 * @param {*} alertIcon 
 * @param {*} driverSelectedId 
 */
async function updatePassengerNotifications(data, alertIcon, driverSelectedId) {
    try {
        const isNotificationUpdated = await updatePassengerNotification({
            transportDateCode: data.transportDateCode,
            notifiedInvolvedId: data.notifiedInvolvedId,
            driverCode: driverSelectedId,
            passengerCode: data.passengerCode
        });
        
        if (isNotificationUpdated) {
            changeAlertIconToNotified(alertIcon);
        }

    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.");
    }
}

/**
 * Deletes the passenger notification from db
 * and changes the alert icon if successful.
 * 
 * @param {Object} data 
 * @param {*} alertIcon 
 */
async function deleteNotification(data, alertIcon) {
    if (await deletePassengerNotification(data)) {
        changeAlertIconToNotNotified(alertIcon);
    } else {
        showNotificationError();
    }
}

/**
 * Shows an error alert when notification actions fail.
 */
function showNotificationError() {
    temporalErrorAlert("Ha ocurrido un error al indicar que se ha avisado del transporte al viajero.");
}

/**
 * Changes the alert icon to indicate the transport has been notified.
 * @param {Object} alertIcon 
 */
function changeAlertIconToNotified(alertIcon) {
    let notifyTransportIconClass = changeElementClass(alertIcon, 'text-primary', 'text-danger');
    alertIcon.attr('class', notifyTransportIconClass);
}

/**
 * Changes the alert icon to indicate the transport has not been notified.
 * @param {Object} alertIcon 
 */
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
            url: '/t/getGeneralPassengerNotificationIconStatus',
            data: {
                templateId : templateId,
                passengerId : passengerId
            }
        });

        // icon shows
        if (response?.data?.showIcon) {
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
    if (passengerNotificationIcon.hasClass("text-primary")) {
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

    // hide or show the button to mark all the month transports have been notified to the passenger
    const templateId = $('#templateTitle').attr('data-template-id');
    await showHidePassengerNotificationsButton(data.t, templateId);
 }