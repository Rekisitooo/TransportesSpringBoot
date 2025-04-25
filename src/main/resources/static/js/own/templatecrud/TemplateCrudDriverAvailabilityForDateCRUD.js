import { genericErrorAlert } from './alert/GenericErrorAlert.js';
import { changeElementDisplayNone, changeElementClass } from './TemplateCrudCommons.js';

function deleteDriverAssistance(data, passengerSelectsSelector, offersTransportIcon, driverPassengersDivId) {
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
        },
        error: function(xhr, status, error) {
            genericErrorAlert();
        }
    });
}

function createDriverAssistance(data, driverFullName, passengerSelectsSelector, offersTransportIcon) {
    const newDriverOption = $('<option>',
        {
            value: data.involvedId,
            text: driverFullName
        }
    );

    $.ajax({
        url: '/involvedAvailability',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            //poner el conductor en el combo de conductores los viajeros
            addDriverInPassengerTable(passengerSelectsSelector, newDriverOption);

            //cambiar el iconito de ofrece transporte
            changeTransportOfferingIcon(1, offersTransportIcon, 'text-muted', 'text-primary');
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
            option.remove();
        }
    );
}

function addDriverInPassengerTable(passengerSelectsSelector, newDriverOption) {
    $(passengerSelectsSelector).each(
        function() {
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
    const driverId = offersTransportIcon.attr('data-t');
    const dateId = offersTransportIcon.attr('data-y');
    const data = {
        transportDateId : dateId,
        involvedId : driverId,
    }
    
    const driverPassengersDivId = $('div[id*=driverPassengersOnDate_' + driverId + '_' + dateId + ']');
    const driverFullName = $('#' + driverId + '_th').text();
    const passengerSelectsSelector = 'select[name=driverInTransportSelect][id*=' + data.transportDateId + '_select]';

    const driverTransportOffer = offersTransportIcon.attr('data-driver-assist');
    if (driverTransportOffer === "1") {
        deleteDriverAssistance(data, passengerSelectsSelector, offersTransportIcon, driverPassengersDivId);
    } else if (driverTransportOffer === "0") {
       createDriverAssistance(data, driverFullName, passengerSelectsSelector, offersTransportIcon);
    }
}

$(document).ready(function() {
    $('#driverTransportsTable i[class*=car]').each(
        function () {
            $(this).on('click', changeDriverAssistance);
        }
    );
});