import { genericErrorAlert } from './alert/GenericErrorAlert.js';
import { changeElementDisplayNone, changeElementClass } from './TemplateCrudCommons.js';

function deletePassengerAssistance(data, assistanceIcon, driverSelectForPassenger, needsTransportIconCol, doesNotNeedTransportSpan, doesNotAssistSpan) {
    const driverId = driverSelectForPassenger.val();

    $.ajax({
        url: '/involvedAvailability',
        type: 'DELETE',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            //Quitar pasajeros de la tabla de conductores
            removePassengerInDriverTable(driverId, data.passengerId, data.transportDateId);

            //poner 'no asiste'
            changeElementDisplay(doesNotAssistSpan);

            //quitar columna icono necesita transporte
            const needsTransportIcon = needsTransportIconCol.find('i[class*=fa-car]');
            needsTransportIcon.attr('data-needs-transport', 0);
            let needsTransportIconClass = changeElementClass(needsTransportIcon, 'text-primary', 'text-muted');
            needsTransportIcon.attr('class', needsTransportIconClass);

            let needsTransportIconColClass = changeElementDisplayNone(needsTransportIconCol);
            needsTransportIconCol.attr('class', needsTransportIconColClass);

            //cambia el icono de asistencia
            changeAssistanceIcon(0, assistanceIcon, 'text-primary', 'text-muted');

            //si está el combo de conductores, lo quitas
            if (!driverSelectForPassenger.attr('class').includes('d-none')) {
                 changeElementDisplay(driverSelectForPassenger);
                 driverSelectForPassenger.val('');
            }

            //si está el 'no necesita transporte' se lo quitas
            if (!doesNotNeedTransportSpan.attr('class').includes('d-none')) {
                changeElementDisplay(doesNotNeedTransportSpan);
            }
        },
        error: function(xhr, status, error) {
            genericErrorAlert();
        }
    });
}

function createPassengerAssistance(data, assistanceIcon, driverSelectForPassenger, needsTransportIconCol, doesNotAssistSpan) {
    $.ajax({
        url: '/involvedAvailability',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            //poner el combo de conductores
            changeElementDisplay(driverSelectForPassenger);
            driverSelectForPassenger.children().attr("name", "c");

            //cambiar el iconito de asistencia
            changeAssistanceIcon(1, assistanceIcon, 'text-muted', 'text-primary');

            //mostrar el icono de necesita transporte
            let needsTransportIconColClass = changeElementDisplayNone(needsTransportIconCol);
            needsTransportIconCol.attr('class', needsTransportIconColClass);

            //pone el texto 'no asiste'
            changeElementDisplay(doesNotAssistSpan);
        },
        error: function(xhr, status, error) {
            genericErrorAlert();
        }
    });
}

function removePassengerInDriverTable(driverId, passengerId, dateId) {
    if (driverId != null && driverId !== "") {
        const passengerSpan = $('span[id=p' + passengerId + 'd' + driverId + 't' + dateId + ']');
        passengerSpan.remove();

        const passengerSpanBr = $('br[id=brp' + passengerId + 'd' + driverId + 't' + dateId + ']');
        passengerSpanBr.remove();
    }
}

function changeElementDisplay(element) {
    let elementClass = changeElementDisplayNone(element);
    element.attr('class', elementClass);
}

function changeAssistanceIcon(dataPassengerAssist, assistanceIcon, classReplace, classToBeReplaced) {
    let assistanceIconClass = changeElementClass(assistanceIcon, classToBeReplaced, classReplace);
    assistanceIcon.attr('class', assistanceIconClass);

    assistanceIcon.attr('data-passenger-assist', dataPassengerAssist);
}

function changePassengerAssistance() {
    const assistanceIcon = $(this);
    const passengerId = assistanceIcon.attr('data-t');
    const dateId = assistanceIcon.attr('data-y');
    const data = {
        transportDateId : dateId,
        passengerId : passengerId,
    }
    
    const driverSelectForPassenger = $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_select');
    const needsTransportIconCol = $('#needsTransportIcon_' + passengerId + '_' + dateId);
    const doesNotNeedTransportSpan = $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_doesNotNeedTransportSpan');
    const doesNotAssistSpan = $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_doesNotAssistSpan');
    
    const passengerAssistance = assistanceIcon.attr('data-passenger-assist');
    if (passengerAssistance === "1") {
        deletePassengerAssistance(data, assistanceIcon, driverSelectForPassenger, needsTransportIconCol, doesNotNeedTransportSpan, doesNotAssistSpan);
    } else if (passengerAssistance === "0") {
       createPassengerAssistance(data, assistanceIcon, driverSelectForPassenger, needsTransportIconCol, doesNotAssistSpan);
    }
}

$(document).ready(function() {
    $('i[class*="fas fa-calendar"]').each(
        function () {
            $(this).on('click', changePassengerAssistance);
        }
    );
});