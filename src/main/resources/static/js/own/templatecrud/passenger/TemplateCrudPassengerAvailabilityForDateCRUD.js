import { genericErrorAlert } from '../alert/GenericErrorAlert.js';
import { changeElementDisplayNone, changeElementClass } from '../TemplateCrudCommons.js';

function deletePassengerAssistance(data, assistanceIcon, driverSelectForPassenger, needsTransportIconCol, doesNotNeedTransportSpan, doesNotAssistSpan, communicationIconCol) {
    const driverId = driverSelectForPassenger.val();

    $.ajax({
        url: '/involvedAvailability',
        type: 'DELETE',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            //Quitar pasajeros de la tabla de conductores
            removePassengerInDriverTable(driverId, data.involvedId, data.transportDateId);

            //poner 'no asiste'
            changeElementDisplay(doesNotAssistSpan);

            //quitar columna icono necesita transporte
            const needsTransportIcon = needsTransportIconCol.find('i[class*=fa-car]');
            needsTransportIcon.attr('data-needs-transport', 0);
            let needsTransportIconClass = changeElementClass(needsTransportIcon, 'text-primary', 'text-muted');
            needsTransportIcon.attr('class', needsTransportIconClass);

            // quitar el icono de asistencia
            let needsTransportIconColClass = changeElementDisplayNone(needsTransportIconCol);
            needsTransportIconCol.attr('class', needsTransportIconColClass);

            // quitar el icono de aviso
            let communicationIconColClass = changeElementDisplayNone(communicationIconCol);
            communicationIconCol.attr('class', communicationIconColClass);

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

function createPassengerAssistance(data, assistanceIcon, driverSelectForPassenger, needsTransportIconCol, doesNotAssistSpan, communicationIconCol) {
    $.ajax({
        url: '/involvedAvailability',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            //poner el combo de conductores
            changeElementDisplay(driverSelectForPassenger);
            driverSelectForPassenger.first().attr("name", "d");
            driverSelectForPassenger.not(':first').attr("name", "c");

            //cambiar el iconito de asistencia
            changeAssistanceIcon(1, assistanceIcon, 'text-muted', 'text-primary');

            //mostrar el icono de necesita transporte
            let needsTransportIconColClass = changeElementDisplayNone(needsTransportIconCol);
            needsTransportIconCol.attr('class', needsTransportIconColClass);

            //mostrar el icono de avisos
            let communicationIconColClass = changeElementDisplayNone(communicationIconCol);
            communicationIconCol.attr('class', communicationIconColClass);

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
        const passengerNameDiv = $('div[id=driverPassengersOnDate_' + driverId + '_' + dateId + '_' + passengerId + ']');
        passengerNameDiv.remove();
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
        involvedId : passengerId,
    }
    
    const driverSelectForPassenger = $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_select');
    const needsTransportIconCol = $('#needsTransportIcon_' + passengerId + '_' + dateId);
    const communicationIconCol = $('#communicationIcon_' + passengerId + '_' + dateId);
    const doesNotNeedTransportSpan = $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_doesNotNeedTransportSpan');
    const doesNotAssistSpan = $('#selectDriverForPassenger_' + passengerId + '_' + dateId + '_doesNotAssistSpan');
    
    const passengerAssistance = assistanceIcon.attr('data-passenger-assist');
    if (passengerAssistance === "1") {
        deletePassengerAssistance(data, assistanceIcon, driverSelectForPassenger, needsTransportIconCol, doesNotNeedTransportSpan, doesNotAssistSpan, communicationIconCol);
    } else if (passengerAssistance === "0") {
       createPassengerAssistance(data, assistanceIcon, driverSelectForPassenger, needsTransportIconCol, doesNotAssistSpan, communicationIconCol);
    }
}

$(document).ready(function() {
    $('#passengerTransportsTable i[class*="fas fa-calendar"]').each(
        function () {
            $(this).on('click', changePassengerAssistance);
        }
    );
});