import { genericErrorAlert } from './alert/GenericErrorAlert.js';
import { changeElementDisplayNone, changeElementClass } from './TemplateCrudCommons.js';

function changePassengerNeedForTransport() {
    const needTransportIcon = $(this);
    const driverForPassengerSelect = needTransportIcon.attr('data-passenger-select');
    let passengerNeedsTransport = needTransportIcon.attr('data-needs-transport');
    const passengerId = needTransportIcon.attr('data-t');
    const dateId = needTransportIcon.attr('data-y');

    const passengerCellContentSelectSelector = '#selectDriverForPassenger_' + passengerId + '_' + dateId + '_select';
    const passengerCellContentSelect = $(passengerCellContentSelectSelector);
    const driverId = passengerCellContentSelect.val();

    const data = {
        passengerNeedsTransport : passengerNeedsTransport,
        driverId : driverId,
        transportDateId : dateId
    }

    $.ajax({
        type: 'PATCH',
        contentType: 'application/json',
        url: '/involvedAvailability/updateNeedForTransport/' + passengerId,
        data: JSON.stringify(data),
        success : function(response) {
            let needTransportIconClass;

            if (passengerNeedsTransport == 1) {
                passengerNeedsTransport = 0;
                needTransportIconClass = changeElementClass(needTransportIcon, 'text-primary', 'text-muted');
            } else if (passengerNeedsTransport == 0) {
                passengerNeedsTransport = 1;
                const driverForPassengerSelectOptionElements = $(passengerCellContentSelectSelector + ' option');
                driverForPassengerSelectOptionElements.each(
                    function () {
                        $(this).attr('name', 'c');
                    }
                );

                needTransportIconClass = changeElementClass(needTransportIcon, 'text-muted', 'text-primary');
                passengerCellContentSelect.val('');
            }
            needTransportIcon.attr('class', needTransportIconClass);
            needTransportIcon.attr('data-needs-transport', passengerNeedsTransport);

            let passengerCellContentElementClass = changeElementDisplayNone(passengerCellContentSelect);
            passengerCellContentSelect.attr('class', passengerCellContentElementClass);

            const passengerCellDoesNotNeedTransportSpanSelector = '#selectDriverForPassenger_' + passengerId + '_' + dateId + '_doesNotNeedTransportSpan';
            const passengerCellDoesNotNeedTransportSpan = $(passengerCellDoesNotNeedTransportSpanSelector);
            let spanElementClass = changeElementDisplayNone(passengerCellDoesNotNeedTransportSpan);
            passengerCellDoesNotNeedTransportSpan.attr('class', spanElementClass);
        },
        error : function(xhr, status, error) {
            genericErrorAlert();
        }
    })
}

$(document).ready(
    function () {
        $('i[class*="fas fa-car"]').each(
            function () {
                $(this).on('click', changePassengerNeedForTransport);
            }
        );
    }
);