import { temporalErrorAlert } from '../../alert/GenericErrorAlert.js';
import { changeElementClass } from '../../TemplateCrudCommons.js';

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

                    if (driverSelectedId === undefined || driverSelectedId === '') {
                        await ajaxRequestDeletePassengerNotification(data);
                        $(this).addClass('d-none');

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
        const response = await $.ajax({
            type: 'GET',
            url: '/t/getDriverForPassengerByDate',
            data : {
                transportDateId: data.transportDateCode,
                passengerId: data.notifiedInvolvedId
            }
        });

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

async function deletePassengerNotification(data, alertIcon) {
    if (ajaxRequestDeletePassengerNotification(data)) {
        changeAlertIconToNotNotified(alertIcon);
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
            await createPassengerNotification(notification, alertIcon);
        }

    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.");
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
        if (response?.data?.length < 2) {
            $('#passengerTransportsTable tr td:first-child div[id=markAsNotifiedPassengerButtonDiv_' + passengerId + ']')
                .addClass('d-none');

        } else {
            $('#passengerTransportsTable tr td:first-child div[id=markAsNotifiedPassengerButtonDiv_' + passengerId + ']')
                .removeClass('d-none');
        }
    } catch (error) {
        console.error('Error updating passengers whole month notification button');
    }
}