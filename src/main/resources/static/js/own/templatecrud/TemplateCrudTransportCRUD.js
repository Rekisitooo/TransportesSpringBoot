function updateDriverInTransportOption(data, passengerFullName) {
    $.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: '/t/updateDriver',
        data: JSON.stringify(data),
        dataType: 'json',
        success : function(response) {
            if (response.status === 'ok') {
               addPassengerInDriverTransportsTable(data.d, data.p, data.t, passengerFullName);
               deletePassengerInDriverTransportsTable(data.d, data.t, response.data.p);
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

function createTransportOption(data, passengerFullName, actualSelect) {
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url:'/t',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response){
           addPassengerInDriverTransportsTable(data.d, data.p, data.t, passengerFullName);
           actualSelect.children().attr("name", "u");
           actualSelect.children(":first").attr("name", "d");
        },
        error: function(e) {
            //TODO configurar la alerta para que se muestre en un idioma u otro
            window.alert("Ha ocurrido un error al asignar el conductor.");
        }
    })
}

function deleteTransportOption(data, actualSelect) {
    $.ajax({
        type: 'DELETE',
        contentType: 'application/json',
        url:'/t',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response){
            deletePassengerInDriverTransportsTable(data.d, data.t, response.data.p);
            actualSelect.children().attr("name", "c");
            actualSelect.children(":first").attr("name", "d");
        },
        error: function(e) {
            //TODO configurar la alerta para que se muestre en un idioma u otro
            window.alert("Ha ocurrido un error al desasignar el conductor.");
        }
    })
}

function addPassengerInDriverTransportsTable(transportDateId, driverId, passengerId, passengerFullName) {
    //TODO when UUIDs are implemented, change the id and remove the p, d, t...
    const driverTransportsTablePassengerTdToAddSpan = 'd' + driverId + 't' + transportDateId;
    const newPassengerSpanNode = document.createElement("span");
    const newPassengerSpanTextNode = document.createTextNode(passengerFullName);
    newPassengerSpanNode.appendChild(newPassengerSpanTextNode);
    newPassengerSpanNode.setAttribute('id', 'p' + passengerId + driverTransportsTablePassengerTdToAddSpan);
    newPassengerSpanNode.setAttribute('data-span-id', driverId);

    $('#' + driverTransportsTablePassengerTdToAddSpan).append(newPassengerSpanNode);
}

function deletePassengerInDriverTransportsTable(transportDateId, passengerId, oldDriverId) {
    //TODO when UUIDs are implemented, change the id and remove the p, d, t...
    const deleteId = 'p' + passengerId + 'd' + oldDriverId + 't' + transportDateId;

    const driverTransportsTablePassengerSpanToDelete = '#' + deleteId;
    $(driverTransportsTablePassengerSpanToDelete).remove();

    const driverTransportsTablePassengerBrToDelete = '#br' + deleteId;
    $(driverTransportsTablePassengerBrToDelete).remove();
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
    const operations = {
        d: deleteTransportOption.bind(undefined, data, actualSelect),
        u: updateDriverInTransportOption.bind(undefined, data, passengerFullName),
        c: createTransportOption.bind(undefined, data, passengerFullName, actualSelect)
    };

    const method = operations[methodName];
    method();
}

$(document).ready(
    function () {
        $('select[name="driverInTransportSelect"]').on('change', operate);
    }
);