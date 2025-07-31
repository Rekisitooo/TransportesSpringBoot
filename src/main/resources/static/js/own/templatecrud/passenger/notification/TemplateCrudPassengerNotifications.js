import { temporalErrorAlert } from '../../alert/GenericErrorAlert.js';
import { changeElementClass } from '../../TemplateCrudCommons.js';

$(function() {
    $('#passengerTransportsTable i[class*=exclamation-circle]').each(
        function () {
            $(this).on('click', async function() {
                const passengerThId = $(this).attr('data-passenger-th');
                const dataDateTd = $(this).attr('data-date-td');
                const data = {
                    transportDateCode : $('td[id=' + dataDateTd + ']').attr('data-date-id'),
                    notifiedInvolvedId : $('th[id=' + passengerThId + ']').attr('data-t')
                };

                if ($(this).attr('class').includes('text-danger')) {
                    const driverSelectSelector = $(this).attr('data-drivers-selector');
                    const driverSelect = $('#' + driverSelectSelector);
                    const driverSelectedId = driverSelect.val();

                    if (driverSelectedId === undefined || driverSelectedId === '') {
                        await ajaxRequestDeletePassengerNotification(data);
                        $(this).addClass('d-none');

                    } else {
                        notifyTransport(data, $(this), driverSelectedId);
                    }

                } else if ($(this).attr('class').includes('text-primary')) {
                    deletePassengerNotification(data, $(this));
                }
            });
        }
    );
});

async function notifyTransport(data, alertIcon, driverSelectedId) {
    try {
        const response = await $.ajax({
            type: 'GET',
            url: '/involvedNotification/get',
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
            url: '/involvedNotification/createNotification',
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
 * When transports table is loaded, it shows the notification icon to check
 * every whether
 */
function showHideCheckAllNotificationsButton() {
    const passengerTransportsTableRowList = $('#passengerTransportsTable tr');

    for (let i = 0; i < passengerTransportsTableRowList.length; i++) {
        const passengerId = $(passengerTransportsTableRowList[i]);
        const checkAllNotificationsButton = $(checkAllNotificationsButtonList[i]);
        const checkAllNotificationsButtonClass = checkAllNotificationsButton.attr('class');

        if (checkAllNotificationsButtonClass.includes('d-none')) {
            checkAllNotificationsButton.removeClass('d-none');
        } else {
            checkAllNotificationsButton.addClass('d-none');
        }
    }

    const checkAllNotificationsButtonClass = checkAllNotificationsButton.attr('class');
    if (checkAllNotificationsButtonClass.includes('d-none')) {
        checkAllNotificationsButton.removeClass('d-none');
    } else {
        checkAllNotificationsButton.addClass('d-none');
    }
}

/*
Gestionar si se muestran todos los botones al cargar
	for fila pasajeros
		pilla el id del pasajero
		guarda el botón general
		consulta los botones
		si no se muestra ninguno o están todos en azul
			se esconde
		si no
			se muestra
	fin for */