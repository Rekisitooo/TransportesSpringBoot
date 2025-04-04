import { genericErrorAlert } from './alert/GenericErrorAlert.js';
import { changeElementDisplayNone } from './TemplateCrudCommons.js';

function removeDriverPassengersInDriverTable(driverId) {
    const passengerSpans = $('span[data-span-id=' + driverId + ']');
    passengerSpans.remove();
}

function deletePassengerAssistance(data, currentElement, passengerCellContentElementSelector) {
    const driverForPassengerSelectElement = $(passengerCellContentElementSelector + ' select');
    const driverId = driverForPassengerSelectElement.val();

    $.ajax({
        url: '/involvedAvailability',
        type: 'DELETE',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            removeDriverPassengersInDriverTable(driverId);
            const driverForPassengerSelectRowElement = $(passengerCellContentElementSelector);
            let elementClass = changeElementDisplayNone(driverForPassengerSelectRowElement);
            driverForPassengerSelectRowElement.attr('class', elementClass);
            currentElement.attr('data-passenger-assist', 0);
            currentElement.attr('class', 'fas fa-calendar-check text-muted');
        },
        error: function(xhr, status, error) {
            genericErrorAlert();
        }
    });
}

function createPassengerAssistance(data, currentElement, passengerCellContentElementSelector) {
    $.ajax({
        url: '/involvedAvailability',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            const driverForPassengerSelectRowElement = $(passengerCellContentElementSelector);
            let elementClass = changeElementDisplayNone(driverForPassengerSelectRowElement);
            driverForPassengerSelectRowElement.attr('class', elementClass);
            currentElement.attr('data-passenger-assist', 1);
            currentElement.attr('class', 'fas fa-calendar-check text-primary');
        },
        error: function(xhr, status, error) {
            genericErrorAlert();
        }
    });
}

function changePassengerAssistance() {
    const currentElement = $(this);
    const passengerId = currentElement.attr('data-t');
    const dateId = currentElement.attr('data-y');
    const data = {
        transportDateId : dateId,
        passengerId : passengerId,
    }
    const passengerCellContentElementSelector = '#selectDriverForPassenger_' + passengerId + '_' + dateId;

    const passengerAssistance = currentElement.attr('data-passenger-assist');
    if (passengerAssistance == 1) {
        deletePassengerAssistance(data, currentElement, passengerCellContentElementSelector);
    } else if (passengerAssistance == 0) {
       createPassengerAssistance(data, currentElement, passengerCellContentElementSelector);
    }
}

$(document).ready(function() {
    $('i[class*="fas fa-calendar"]').each(
        function () {
            $(this).on('click', changePassengerAssistance);
        }
    );
});