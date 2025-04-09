import { genericErrorAlert } from './alert/GenericErrorAlert.js';
import { changeElementDisplayNone, changeElementClass } from './TemplateCrudCommons.js';

function removeDriverPassengersInDriverTable(driverId) {
    if (driverId != null && driverId !== "") {
        const passengerSpans = $('span[data-span-id=' + driverId + ']');
        passengerSpans.remove();
    }
}

function deletePassengerAssistance(data, assistanceIcon, driverSelectForPassenger) {
    const driverId = driverSelectForPassenger.val();

    $.ajax({
        url: '/involvedAvailability',
        type: 'DELETE',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            removeDriverPassengersInDriverTable(driverId);
            changeDriverSelectForPassenger(driverSelectForPassenger);
            driverSelectForPassenger.val('');
            changeAssistanceIcon(0, assistanceIcon, 'text-primary', 'text-muted');
        },
        error: function(xhr, status, error) {
            genericErrorAlert();
        }
    });
}

function createPassengerAssistance(data, assistanceIcon, driverSelectForPassenger) {
    $.ajax({
        url: '/involvedAvailability',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            changeDriverSelectForPassenger(driverSelectForPassenger);
            changeAssistanceIcon(1, assistanceIcon, 'text-muted', 'text-primary');
        },
        error: function(xhr, status, error) {
            genericErrorAlert();
        }
    });
}

function changeDriverSelectForPassenger(driverSelectForPassenger) {
    let cellContentClass = changeElementDisplayNone(driverSelectForPassenger);
    driverSelectForPassenger.attr('class', cellContentClass);
}

function changeAssistanceIcon(dataPassengerAssist, assistanceIcon, classReplace, classToBeReplaced) {
    let assistanceIconClass = changeElementClass(assistanceIcon, classToBeReplaced, classReplace);
    assistanceIcon.attr('data-passenger-assist', dataPassengerAssist);
    assistanceIcon.attr('class', assistanceIconClass);
}

function changePassengerAssistance() {
    const assistanceIcon = $(this);
    const passengerId = assistanceIcon.attr('data-t');
    const dateId = assistanceIcon.attr('data-y');
    const data = {
        transportDateId : dateId,
        passengerId : passengerId,
    }
    const driverSelectForPassengerSelector = '#selectDriverForPassenger_' + passengerId + '_' + dateId + '_select';
    const driverSelectForPassenger = $(driverSelectForPassengerSelector);

    const passengerAssistance = assistanceIcon.attr('data-passenger-assist');
    if (passengerAssistance === "1") {
        deletePassengerAssistance(data, assistanceIcon, driverSelectForPassenger);
    } else if (passengerAssistance === "0") {
       createPassengerAssistance(data, assistanceIcon, driverSelectForPassenger);
    }
}

$(document).ready(function() {
    $('i[class*="fas fa-calendar"]').each(
        function () {
            $(this).on('click', changePassengerAssistance);
        }
    );
});