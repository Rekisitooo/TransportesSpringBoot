import { genericErrorAlert } from '../alert/GenericErrorAlert.js';
import { changeElementDisplayNone, changeElementClass } from '../TemplateCrudCommons.js';

function deleteDriverAssistance(data, passengerSelectsSelector, offersTransportIcon, driverPassengersDivId, communicationIconCol) {
    const driverId = data.involvedId;
    $.ajax({
        url: '/involvedAvailability',
        type: 'DELETE',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            //Quitar conductor de la tabla de viajeros
            removeDriverInPassengerTableSelects(passengerSelectsSelector, driverId);

            //eliminar nombres de pasageros si los tiene
            driverPassengersDivId.remove();

            //cambia el icono de ofrece transporte
            changeTransportOfferingIcon(0, offersTransportIcon, 'text-primary', 'text-muted');

            // quitar el icono de aviso
            let communicationIconColClass = changeElementDisplayNone(communicationIconCol);
            communicationIconCol.attr('class', communicationIconColClass);
        },
        error: function(xhr, status, error) {
            genericErrorAlert();
        }
    });
}

function createDriverAssistance(data, driverFullName, passengerSelectsSelector, offersTransportIcon, passengerNameCells, communicationIconCol) {
    $.ajax({
        url: '/involvedAvailability',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            //poner el conductor en el combo de conductores los viajeros
            addDriverInPassengerTable(passengerSelectsSelector, data, driverFullName, passengerNameCells);

            //cambiar el iconito de ofrece transporte
            changeTransportOfferingIcon(1, offersTransportIcon, 'text-muted', 'text-primary');

            // quitar el icono de aviso
            let communicationIconColClass = changeElementDisplayNone(communicationIconCol);
            communicationIconCol.attr('class', communicationIconColClass);
        },
        error: function(xhr, status, error) {
            genericErrorAlert();
        }
    });
}

function removeDriverInPassengerTableSelects(passengerSelectsSelector, driverId) {
    $(passengerSelectsSelector).each(
        function() {
            const option = $(this).find('option[value="' + driverId + '"]');
            if (option.is(':selected')) {
                $(this).find('option:not([name=d])').attr('name', 'c');
            }
            option.remove();
        }
    );
}

function addDriverInPassengerTable(passengerSelectsSelector, data, driverFullName, passengerNameCells) {
    $(passengerSelectsSelector).each(
        function() {
            const firstDriverOption = $(this).eq(1);

            let operationToPerform;
            if (firstDriverOption === null || firstDriverOption === undefined) {
                operationToPerform = 'c';
            } else {
                operationToPerform = firstDriverOption.attr('name');
            }

            const passengerId = $(this).attr('data-t');
            const passengerTh = passengerNameCells.filter('[data-t=' + passengerId + ']');
            const passengerName = passengerTh.text();
            const newDriverOption = $('<option>',
                {
                    'value'                 :       data.involvedId,
                    'data-d'                :       data.transportDateId,
                    'data-t'                :       passengerId,
                    'text'                  :       driverFullName,
                    'name'                  :       operationToPerform,
                    'data-passenger-name'   :       passengerName
                }
            );

            $(this).append(newDriverOption);
        }
    );
}

function changeElementDisplay(element) {
    let elementClass = changeElementDisplayNone(element);
    element.attr('class', elementClass);
}

function changeTransportOfferingIcon(dataDriverAssist, offersTransportIcon, classReplace, classToBeReplaced) {
    let offersTransportIconClass = changeElementClass(offersTransportIcon, classToBeReplaced, classReplace);
    offersTransportIcon.attr('class', offersTransportIconClass);

    offersTransportIcon.attr('data-driver-assist', dataDriverAssist);
}

function changeDriverAssistance() {
    const offersTransportIcon = $(this);
    const driverId = offersTransportIcon.attr('data-d');
    const dateId = offersTransportIcon.attr('data-y');
    const data = {
        transportDateId : dateId,
        involvedId : driverId,
    }
    
    const passengerSelectsSelector = 'select[name=driverInTransportSelect][id*=' + data.transportDateId + '_select]';
    const communicationIconCol = $('#communicationIcon_' + driverId + '_' + dateId);

    const driverTransportOffer = offersTransportIcon.attr('data-driver-assist');
    if (driverTransportOffer === "1") {
        const driverPassengersDivId = $('div[id*=driverPassengersOnDate_' + driverId + '_' + dateId + ']');
        deleteDriverAssistance(data, passengerSelectsSelector, offersTransportIcon, driverPassengersDivId, communicationIconCol);

    } else if (driverTransportOffer === "0") {
        const driverFullName = $('#' + driverId + '_th').text();
        const passengerNameCells = $('th[id*=passengerNameCell_]');
        createDriverAssistance(data, driverFullName, passengerSelectsSelector, offersTransportIcon, passengerNameCells, communicationIconCol);
    }
}

$(document).ready(function() {
    $('#driverTransportsTable i[class*=car]').each(
        function () {
            $(this).on('click', changeDriverAssistance);
        }
    );
});