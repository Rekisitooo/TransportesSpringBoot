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
                    involvedCommunicatedId : $('th[id=' + passengerThId + ']').attr('data-t')
                };

                if ($(this).attr('class').includes('text-danger')) {
                    const driverSelectSelector = $(this).attr('data-drivers-selector');
                    const driverSelect = $('#' + driverSelectSelector);
                    const driverSelectedId = driverSelect.val();

                    if (driverSelectedId === undefined || driverSelectedId === '') {
                        await ajaxRequestDeletePassengerCommunication(data);
                        $(this).addClass('d-none');

                    } else {
                        communicateTransport(data, $(this), driverSelectedId);
                    }

                } else if ($(this).attr('class').includes('text-primary')) {
                    deletePassengerCommunication(data, $(this));
                }
            });
        }
    );
});

async function communicateTransport(data, alertIcon, driverSelectedId) {
    try {
        const response = await $.ajax({
            type: 'GET',
            url: '/involvedCommunication/get',
            data: data
        });

        if (!response?.data?.length) {
            await createPassengerCommunication(data, alertIcon, driverSelectedId);
        } else {
            await updatePassengerNotifications(response.data[0], alertIcon, driverSelectedId);
        }
    } catch (error) {
        showCommunicationError();
    }
}

async function createPassengerCommunication(data, alertIcon, driverSelectedId) {
    try {
        const response = await $.ajax({
            type: 'GET',
            url: '/t/getDriverForPassengerByDate',
            data : {
                transportDateId: data.transportDateCode,
                passengerId: data.involvedCommunicatedId
            }
        });

        const newData = {
            ...data,
            notificationDate: Date.now(),
            driverCode: driverSelectedId,
            passengerCode: data.involvedCommunicatedId
        };

        const passengerCommunicationCreationResponse = await ajaxRequestCreatePassengerCommunication(newData, alertIcon);
        if (passengerCommunicationCreationResponse) {
            await changeAlertIconToCommunicated(alertIcon);
        }
    } catch (error) {
        showCommunicationError();
    }
}

async function ajaxRequestCreatePassengerCommunication(data, alertIcon) {
    try {
        await $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/involvedCommunication/createCommunication',
            data: JSON.stringify(data),
            dataType: 'json'
        });
        return true;

    } catch (error) {
        showCommunicationError();
        return false;
    }
}

async function ajaxRequestDeletePassengerCommunication(data) {
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

async function deletePassengerCommunication(data, alertIcon) {
    if (ajaxRequestDeletePassengerCommunication(data)) {
        changeAlertIconToNotCommunicated(alertIcon);
    }
}

async function updatePassengerNotifications(data, alertIcon, driverSelectedId) {
    try {
        const isCommunicationDeleted = await ajaxRequestDeletePassengerCommunication(data);
        if (isCommunicationDeleted) {
            const notification = {
                transportDateCode: data.transportDateCode,
                involvedCommunicatedId: data.involvedCommunicatedId
            };
            await createPassengerCommunication(notification, alertIcon);
        }

    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al indicar que el transporte no se ha comunicado.");
    }
}

function showCommunicationError() {
    temporalErrorAlert("Ha ocurrido un error al indicar que se ha avisado del transporte al viajero.");
}

function changeAlertIconToCommunicated(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-primary', 'text-danger');
    alertIcon.attr('class', communicateTransportIconClass);
}

function changeAlertIconToNotCommunicated(alertIcon) {
    let communicateTransportIconClass = changeElementClass(alertIcon, 'text-danger', 'text-primary');
    alertIcon.attr('class', communicateTransportIconClass);
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