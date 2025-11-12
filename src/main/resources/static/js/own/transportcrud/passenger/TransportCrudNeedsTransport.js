import { genericErrorAlert } from '../alert/GenericErrorAlert.js';
import { updatePassengerNeedForTransport } from '../../NeedTransportAJAX.js';

$(function() {
    $('#passengerTransportsTable i[class*=car]').each(
        function () {
            $(this).on('click', async function() {
                await changePassengerNeedForTransport($(this));
            });
        }
    );
});

/**
 * 
 * @param {Object} needTransportIcon 
 * @returns 
 */
async function changePassengerNeedForTransport(needTransportIcon) {
    let passengerNeedsTransport = needTransportIcon.attr('data-needs-transport');
    const passengerId = needTransportIcon.attr('data-t');
    const dateId = needTransportIcon.attr('data-y');

    const driverSelectForPassenger = $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_select');
    const driverId = driverSelectForPassenger.val();
    const notificationIconCol = $('#notificationIcon_' + passengerId + '_' + dateId);

    const data = {
        passengerNeedsTransport : passengerNeedsTransport,
        driverId : driverId,
        transportDateId : dateId
    }

    if (await updatePassengerNeedForTransport(data, passengerId)) {

        // show passenger needs transport
        if (passengerNeedsTransport == 1) {
            // needs transport icon turns blue
            $(needTransportIcon).removeClass('text-muted');
            $(needTransportIcon).addClass('text-primary');

            // show the available drivers combo
            $(driverSelectForPassenger).removeClass('d-none');

            // show the notification icon
            $(notificationIconCol).removeClass('d-none');

            // hide the "does not need transport" span
            const passengerCellDoesNotNeedTransportSpan = $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_doesNotNeedTransportSpan');
            $(passengerCellDoesNotNeedTransportSpan).addClass('d-none');

            passengerNeedsTransport = 0;

        // show passenger does not need transport
        } else if (passengerNeedsTransport == 0) {

            // needs transport icon turns gray
            $(needTransportIcon).removeClass('text-primary');
            $(needTransportIcon).addClass('text-muted');
        
            // hide the available drivers combo and reset its value
            resetAvailableDriversForPassengerCombo(driverSelectForPassenger);

            // hide the notification icon
            $(notificationIconCol).addClass('d-none');

            // show the "does not need transport" span
            const passengerCellDoesNotNeedTransportSpan = $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_doesNotNeedTransportSpan');
            $(passengerCellDoesNotNeedTransportSpan).removeClass('d-none');

            passengerNeedsTransport = 1;
        }
        
        needTransportIcon.attr('data-needs-transport', passengerNeedsTransport);

    } else {
        genericErrorAlert();
    }    
}

/**
 * Resets the driver combo by setting all options' name attribute to 'c' (create) except the default one (delete).
 * and hides it.
 * 
 * @param {jQuery} driverSelectForPassenger - The jQuery object for the passenger cell content select element
 */
function resetAvailableDriversForPassengerCombo(driverSelectForPassenger) {
    driverSelectForPassenger.children('option:not(:first)').attr('name', 'c');
    
    // reset the chosen value of the combo, setting it to the default one 
    driverSelectForPassenger.val('');

    // hide the combo
    driverSelectForPassenger.addClass('d-none');
}
