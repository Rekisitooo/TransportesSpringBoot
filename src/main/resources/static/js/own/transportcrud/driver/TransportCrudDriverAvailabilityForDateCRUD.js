import { genericErrorAlert } from '../alert/GenericErrorAlert.js';
import { changeElementClass } from '../TransportCrudCommons.js';

async function deleteDriverAssistance(formData, iconsData) {
    const driverId = formData.involvedId;
    // transport and notification deletions are done through a trigger
    await $.ajax({
        url: '/involvedAvailability',
        type: 'DELETE',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(response) {
            //Quitar conductor de la tabla de viajeros
            changePassengerTableSelects(iconsData.passengerSelectsSelector, driverId);

            //eliminar nombres de pasageros si los tiene
            iconsData.driverPassengersDivId.remove();

            //cambia el icono de ofrece transporte
            changeTransportOfferingIcon(0, iconsData.offersTransportIcon, 'text-primary', 'text-muted');

            // quitar el icono de aviso
            if (!iconsData.notificationIconCol.hasClass('d-none')) {
                iconsData.notificationIconCol.addClass('d-none');
            }
            // ponerlo en rojo si estaba en azul por si vuelve a estar disponible
            if (!iconsData.notificationIconCol.hasClass('text-danger')) {
                iconsData.notificationIconCol.addClass('text-primary');
            }

            // marcar que el pasajero tiene el aviso pendiente

        },
        error: function(xhr, status, error) {
            genericErrorAlert();
        }
    });
}

async function createDriverAssistance(formData, iconsData) {
    await $.ajax({
        url: '/involvedAvailability',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(response) {
            //poner el conductor en el combo de conductores los viajeros
            addDriverInPassengerTable(formData, iconsData);

            //cambiar el iconito de ofrece transporte
            changeTransportOfferingIcon(1, iconsData.offersTransportIcon, 'text-muted', 'text-primary');

            // quitar el icono de aviso
            if (!iconsData.notificationIconCol.hasClass('d-none')) {
                iconsData.notificationIconCol.addClass('d-none');
            }
        },
        error: function(xhr, status, error) {
            genericErrorAlert();
        }
    });
}

/**
 * Remove driver from passenger selects in the passenger transports table
 * and if there are no drivers left, hide the select and show the no drivers available span
 * @param {*} passengerSelectsSelector 
 * @param {*} driverId 
 */
function changePassengerTableSelects(passengerSelectsSelector, driverId) {
    const $driverSelect = $(passengerSelectsSelector);

    $driverSelect.each(
        function() {

            $(this).find('option[value="' + driverId + '"]').remove();

            // if there are no drivers left, hide the select and show the no drivers available span
            if ($(this).find('option').length <= 1) {
                // TODO if the passenger assists and needs transport, otherwise the select is hidden because the passenger doesn't need transport
                $(this).addClass('d-none');
                const noDriversAvailableSpanId = $(this).attr('id') + '_noDriversAvailableSpan';
                noDriversAvailableSpanId.replace('_select', '');
                $('#' + noDriversAvailableSpanId).removeClass('d-none');
            }
        }
    );

}

function addDriverInPassengerTable(data, iconsData) {
    const $driverSelect = $(iconsData.passengerSelectsSelector);

    $driverSelect.each(
        function() {
            
            // if there were no drivers available, show the select and hide the no drivers available span
            if ($(this).hasClass('d-none')) {
                // TODO if the passenger assists and needs transport, otherwise the select is hidden because the passenger doesn't need transport
                $(this).removeClass('d-none');
                const noDriversAvailableSpanId = $(this).attr('id') + '_noDriversAvailableSpan';
                noDriversAvailableSpanId.replace('_select', '');
                $('#' + noDriversAvailableSpanId).addClass('d-none');
            }

            let operationToPerform;
            const $selectedDriverOption = $(this).find('option:selected');
            if ($selectedDriverOption === null || $selectedDriverOption === undefined) {
                operationToPerform = 'c';
            } else {
                operationToPerform = 'd';
            }

            const passengerId = $(this).attr('data-t');
            const passengerTh = iconsData.passengerNameCells.filter('[data-t=' + passengerId + ']');
            const passengerName = passengerTh.text();
            const $newDriverOption = $('<option>',
                {
                    'value'                 :       data.involvedId,
                    'data-d'                :       data.transportDateId,
                    'data-t'                :       passengerId,
                    'text'                  :       iconsData.driverFullName,
                    'name'                  :       operationToPerform,
                    'data-passenger-name'   :       passengerName
                }
            );

            $(this).append($newDriverOption);
        }
    );
}

function changeTransportOfferingIcon(dataDriverAssist, offersTransportIcon, classReplace, classToBeReplaced) {
    let offersTransportIconClass = changeElementClass(offersTransportIcon, classToBeReplaced, classReplace);
    offersTransportIcon.attr('class', offersTransportIconClass);

    offersTransportIcon.attr('data-driver-assist', dataDriverAssist);
}

async function changeDriverAssistance() {
    const offersTransportIcon = $(this);
    const driverId = offersTransportIcon.attr('data-d');
    const dateId = offersTransportIcon.attr('data-y');
    const formData = {
        transportDateId : dateId,
        involvedId : driverId,
    }

    const passengerSelectsSelector = 'select[name=driverInTransportSelect][id*=' + formData.transportDateId + '_select]';
    const notificationIconCol = $('#notificationIcon_' + driverId + '_' + dateId);

    const driverTransportOffer = offersTransportIcon.attr('data-driver-assist');
    if (driverTransportOffer === "1") {
        const driverPassengersDivId = $('div[id*=driverPassengersOnDate_' + driverId + '_' + dateId + ']');
        await deleteDriverAssistance(formData, 
            {
                passengerSelectsSelector : passengerSelectsSelector, 
                offersTransportIcon : offersTransportIcon,
                driverPassengersDivId : driverPassengersDivId, 
                notificationIconCol : notificationIconCol
            });

    } else if (driverTransportOffer === "0") {
        const driverFullName = $('#' + driverId + '_th').text();
        const passengerNameCells = $('th[id*=passengerNameCell_]');
        await createDriverAssistance(formData, 
            {
                driverFullName : driverFullName,
                passengerSelectsSelector : passengerSelectsSelector,
                offersTransportIcon : offersTransportIcon,
                passengerNameCells : passengerNameCells,
                notificationIconCol : notificationIconCol
            });
    }
}

$(function() {
    $('#driverTransportsTable i[class*=car]').each(
        async function () {
            $(this).on('click', changeDriverAssistance);
        }
    );
});