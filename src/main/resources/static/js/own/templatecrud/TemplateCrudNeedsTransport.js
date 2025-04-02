function changePassengerNeedForTransport() {
    const currentElement = $(this);
    const driverForPassengerSelect = currentElement.attr('data-passenger-select');
    const passengerNeedsTransport = currentElement.attr('data-needs-transport');
    const passengerId = currentElement.attr('data-t');
    const dateId = currentElement.attr('data-y');

    const selectDriverForPassengerSelector = '#selectDriverForPassenger_' + passengerId + '_' + dateId;
    const driverForPassengerSelectElement = $(selectDriverForPassengerSelector + ' select');
    const driverId = driverForPassengerSelectElement.val();

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
            const driverForPassengerSelectRowElement = $(selectDriverForPassengerSelector);

            if (passengerNeedsTransport == 1) {
                currentElement.attr('data-needs-transport', 0);
                currentElement.attr('class', 'position-relative fas fa-car text-muted');
                currentElement.attr('title', 'Botón para marcar que no necesita transporte este día');
                driverForPassengerSelectRowElement.attr('class', 'row d-table-cell');
            } else if (passengerNeedsTransport == 0) {
                const driverForPassengerSelectOptionElements = $(selectDriverForPassengerSelector + ' select option');
                driverForPassengerSelectOptionElements.each(
                    function () {
                        $(this).attr('name', 'c');
                    }
                );
                currentElement.attr('data-needs-transport', 1);
                currentElement.attr('class', 'position-relative fas fa-car text-primary');
                currentElement.attr('title', 'Botón para marcar que necesita transporte este día');
                driverForPassengerSelectElement.val('');
                driverForPassengerSelectRowElement.attr('class', 'row d-none');
            }
        },
        error : function(xhr, status, error) {
            window.alert("Ha ocurrido un error.");
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