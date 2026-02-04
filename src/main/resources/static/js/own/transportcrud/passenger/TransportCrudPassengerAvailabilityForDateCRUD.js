import { genericErrorAlert } from '../alert/GenericErrorAlert.js';
import { deleteInvolvedAssistance, createInvolvedAssistance } from '../../InvolvedAvailabilityAJAX.js';

$(function() {
    $('#passengerTransportsTable i[class*=fa-calendar]').each(
        function () {
            $(this).on('click', async function(){
                await changePassengerAssistance($(this));
            });
        }
    );
});

/**
 * Change passenger assistance for transport
 * and updates all the icons and combos in the row accordingly
 * 
 * @param {Object} assistanceIcon
 */
async function changePassengerAssistance(assistanceIcon) {
    const passengerId = assistanceIcon.attr('data-t');
    const dateId = assistanceIcon.attr('data-y');
    const ajaxData = {
        needsTransport : 1,
        transportDateId : dateId,
        involvedId : passengerId,
    };

    const passengerDateIcons = {
        assistanceIcon: assistanceIcon,
        driverSelectForPassenger: $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_select'),
        needsTransportIconCol: $('#needsTransportIcon_' + passengerId + '_' + dateId),
        doesNotNeedTransportSpan: $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_doesNotNeedTransportSpan'),
        doesNotAssistSpan: $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_doesNotAssistSpan'),
        notificationIconCol: $('#notificationIcon_' + passengerId + '_' + dateId)
    };

    const passengerAssistance = assistanceIcon.attr('data-passenger-assist');
    if (passengerAssistance === "1") {
        await deletePassengerAssistance(ajaxData, passengerDateIcons);

    } else if (passengerAssistance === "0") {
       await createPassengerAssistance(ajaxData, passengerDateIcons);

    } else {
        genericErrorAlert();
    }
}


/**
 * Delete passenger assistance and changes all the icons and texts for the date
 * @param {Object} ajaxData with transportDateId and involvedId
 * @param {Object} passengerDateIcons with all the jQuery objects for the icons and combos in the passenger date row
 */
async function deletePassengerAssistance(ajaxData, passengerDateIcons) {
    const driverId = passengerDateIcons.driverSelectForPassenger.val();

    if (await deleteInvolvedAssistance(ajaxData)) {
        
        // remove passengers from the driver table
        removePassengerInDriverTable(driverId, ajaxData.involvedId, ajaxData.transportDateId);

        // show 'does not assist' span
        passengerDateIcons.doesNotAssistSpan.removeClass('d-none');

        // change needs transport icon data-needs-transport to 0
        passengerDateIcons.needsTransportIconCol.find('i[class*=fa-car]').attr('data-needs-transport', 0);

        // hide needs transport icon (div)
        passengerDateIcons.needsTransportIconCol.addClass('d-none');

        // hide transport notification icon
        passengerDateIcons.notificationIconCol.addClass('d-none');

        // the transport notification icon should be red, because when passenger does not assist, it is understood that they are notified
        const notificationIcon = passengerDateIcons.notificationIconCol.find('i[class*=fa-exclamation-circle]');
        notificationIcon.addClass('text-danger');
        notificationIcon.removeClass('text-primary');

        // TODO mark driver needs to be notified = icon in red

        // change assistance icon to gray
        passengerDateIcons.assistanceIcon.removeClass('text-primary');
        passengerDateIcons.assistanceIcon.addClass('text-muted');
        passengerDateIcons.assistanceIcon.attr('data-passenger-assist', 0);

        // if the driver's combo is shown, it is hidden and names are changed to order a create
        if (!passengerDateIcons.driverSelectForPassenger.hasClass('d-none')) {
            passengerDateIcons.driverSelectForPassenger.children('option:not(:first)').attr('name', 'c');
            passengerDateIcons.driverSelectForPassenger.val('');
            passengerDateIcons.driverSelectForPassenger.addClass('d-none');
        }

        // hide does not need transport span if shown
        if (!passengerDateIcons.doesNotNeedTransportSpan.hasClass('d-none')) {
            passengerDateIcons.doesNotNeedTransportSpan.addClass('d-none');
        }

    } else {
        genericErrorAlert();
    }
}

/**
 * Creates the passenger assistance and changes all the icons and texts for the date
 * @param {Object} ajaxData with transportDateId and involvedId
 * @param {Object} passengerDateIcons with all the jQuery objects for the icons and combos in the passenger date r
 */
async function createPassengerAssistance(ajaxData, passengerDateIcons) {

    if (await createInvolvedAssistance(ajaxData)) {

        // show drivers combo
        passengerDateIcons.driverSelectForPassenger.removeClass('d-none');
        passengerDateIcons.driverSelectForPassenger.first().attr("name", "d");
        passengerDateIcons.driverSelectForPassenger.not(':first').attr("name", "c");

        // show assistance icon in blue
        passengerDateIcons.assistanceIcon.removeClass('text-muted');
        passengerDateIcons.assistanceIcon.addClass('text-primary');
        passengerDateIcons.assistanceIcon.attr('data-passenger-assist', 1);

        // turn needs transport icon to blue, as need for transport is assumed when passenger assists
        passengerDateIcons.needsTransportIconCol.removeClass('d-none');
        const needsTransportIcon = passengerDateIcons.needsTransportIconCol.find('i[class*=fa-car]');
        needsTransportIcon.addClass('text-primary');
        needsTransportIcon.removeClass('text-muted');

        // show transport notification icon
        passengerDateIcons.notificationIconCol.removeClass('d-none');

        // turn transport notification red
        const notificationIcon = passengerDateIcons.notificationIconCol.find('i[class*=fa-exclamation-circle]');
        notificationIcon.addClass('text-danger');
        notificationIcon.removeClass('text-primary');

        // hide does not assist span
        passengerDateIcons.doesNotAssistSpan.addClass('d-none');
        
    } else {
        genericErrorAlert();
    }
}

/**
 * Remove a passenger from the driver's table for a specific date.
 * 
 * @param {number} driverId 
 * @param {number} passengerId 
 * @param {number} dateId 
 */
function removePassengerInDriverTable(driverId, passengerId, dateId) {
    if (driverId != null && driverId !== "") {
        const passengerNameDiv = $('div[id=driverPassengersOnDate_' + driverId + '_' + dateId + '_' + passengerId + ']');
        passengerNameDiv.remove();
    }
}
