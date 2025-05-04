function updateDriverInTransportOption(data, passengerFullName, driverPassengersDivId, driverTransportsTablePassengerTdToAddSpan) {
    $.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: '/t/updateDriver',
        data: JSON.stringify(data),
        dataType: 'json',
        success : function(response) {
            if (response.status === 'ok') {
               addPassengerInDriverTransportsTable(data.d, data.p, data.t, passengerFullName, driverTransportsTablePassengerTdToAddSpan);
               deletePassengerInDriverTransportsTable(data.d, data.t, response.data.p, driverPassengersDivId);
            } else {
                //TODO configurar la alerta para que se muestre en un idioma u otro
                window.alert("Ha ocurrido un error al actualizar el conductor.");
            }
        },
        error : function(e) {
            //TODO configurar la alerta para que se muestre en un idioma u otro
            window.alert("Ha ocurrido un error al actualizar el conductor.");
        }
    })
}

function createTransportOption(data, passengerFullName, actualSelect, driverTransportsTablePassengerTdToAddSpan) {
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url:'/t',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response){
           addPassengerInDriverTransportsTable(data.d, data.p, data.t, passengerFullName, driverTransportsTablePassengerTdToAddSpan);
           actualSelect.children().attr("name", "u");
           actualSelect.children(":first").attr("name", "d");
        },
        error: function(e) {
            //TODO configurar la alerta para que se muestre en un idioma u otro
            window.alert("Ha ocurrido un error al asignar el conductor.");
        }
    })
}

function deleteTransportOption(data, actualSelect, driverPassengersDivId) {
    $.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url:'/t',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response){
            deletePassengerInDriverTransportsTable(data.d, data.t, response.data.p, driverPassengersDivId);
            actualSelect.children().attr("name", "c");
            actualSelect.children(":first").attr("name", "d");
        },
        error: function(e) {
            //TODO configurar la alerta para que se muestre en un idioma u otro
            window.alert("Ha ocurrido un error al desasignar el conductor.");
        }
    })
}

export function addPassengerInDriverTransportsTable(transportDateId, driverId, passengerId, passengerFullName, driverTransportsTablePassengerTdToAddSpan) {
    //TODO when UUIDs are implemented, change the id and remove the p, d, t...
    const newPassengerSpanNode = document.createElement("span");
    const newPassengerSpanTextNode = document.createTextNode(passengerFullName);
    newPassengerSpanNode.appendChild(newPassengerSpanTextNode);
    newPassengerSpanNode.setAttribute('id', 'p' + passengerId + 'd' + driverId + 't' + transportDateId);
    newPassengerSpanNode.setAttribute('data-span-id', driverId);

    const newDivRowNode = document.createElement("div");
    newDivRowNode.setAttribute('class', 'row');
    newDivRowNode.setAttribute('id', 'driverPassengersOnDate_' + driverId + '_' + transportDateId + '_' + passengerId);
    newDivRowNode.append(newPassengerSpanNode);

    driverTransportsTablePassengerTdToAddSpan.append(newDivRowNode);
}

function deletePassengerInDriverTransportsTable(transportDateId, passengerId, oldDriverId, driverPassengersDivId) {
    //TODO when UUIDs are implemented, change the id and remove the p, d, t...
    const deleteId = 'p' + passengerId + 'd' + oldDriverId + 't' + transportDateId;

    const driverTransportsTablePassengerSpanToDelete = '#' + deleteId;
    $(driverTransportsTablePassengerSpanToDelete).remove();

    driverPassengersDivId.remove();
}

function operate () {
    const actualSelect = $(this);
    const selectedOption = actualSelect.find('option:selected');
    const driverId = actualSelect.val();
    const passengerId = selectedOption.attr('data-t');
    const transportDateId = selectedOption.attr('data-d');
    const passengerFullName = selectedOption.attr('data-passenger-name');
    const methodName = selectedOption.attr('name');
    const data = {
        d : transportDateId,
        p : driverId,
        t : passengerId
    }

    const driverPassengersDivId = $('div[id*=driverPassengersOnDate_' + driverId + '_' + transportDateId + ']');
    const driverTransportsTablePassengerTdToAddSpan = $('#driverPassengersForDateDiv_' + driverId + '_' + transportDateId);

    const operations = {
        d: deleteTransportOption.bind(undefined, data, actualSelect, driverPassengersDivId),
        u: updateDriverInTransportOption.bind(undefined, data, passengerFullName, driverPassengersDivId, driverTransportsTablePassengerTdToAddSpan),
        c: createTransportOption.bind(undefined, data, passengerFullName, actualSelect, driverTransportsTablePassengerTdToAddSpan)
    };

    const method = operations[methodName];
    method();
}

$(document).ready(
    function () {
        $('select[name="driverInTransportSelect"]').on('change', operate);
    }
);