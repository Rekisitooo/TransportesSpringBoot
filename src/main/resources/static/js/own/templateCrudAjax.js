function updateDriverInTransportOption(transportDateId, driverId, passengerId, passengerFullName) {
    const data = {
        d : transportDateId,
        p : driverId,
        t : passengerId
    }

    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: '/t/updateDriver',
        data: JSON.stringify(data),
        dataType: 'json',
        success : function(response) {
            if (response.status === 'ok') {
               addPassengerInDriverTransportsTable(transportDateId, driverId, passengerId, passengerFullName);
               deletePassengerInDriverTransportsTable(transportDateId, passengerId, response);
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

function createTransportOption(transportDateId, driverId, passengerId) {
    $.ajax({
        type: 'PUT',
        url:'/t/create?transportDateId=' +
            'd=' + transportDateId + '&p=' + driverId + '&t=' + passengerId + '',
        success: function(passengerCompleteName){
            const driverTransportsTablePassengerTdToAddSpan = 'd' + driverId + 't' + transportDateId;
            const spanNode = document.createElement("span");
            const spanTextNode = document.createTextNode(passengerCompleteName);
            spanNode.appendChild(spanTextNode);
            //TODO when UUIDs are implemented, change the id and remove the p, d, t...
            spanNode.setAttribute('id', '#p' + passengerId + driverTransportsTablePassengerTdToAddSpan);3
            $('#' + driverTransportsTablePassengerToChange).append(spanNode);
            $('#' + driverTransportsTablePassengerToChange).append(document.createElement("br"));
        },
        error: function(e) {
            //TODO configurar la alerta para que se muestre en un idioma u otro
            window.alert("Ha ocurrido un error al asignar el conductor.");
        }
    })
}

function deleteTransportOption(transportDateId, driverId, passengerId) {
    $.ajax({
        type: 'DELETE',
        url:'/t/delete?transportDateId=' +
            'd=' + transportDateId + '&p=' + driverId + '&t=' + passengerId + '',
        success: function(){
            //TODO when UUIDs are implemented, change the id and remove the p, d, t...
            const driverTransportsTablePassengerToDelete = '#p' + passengerId + 'd' + driverId + 't' + transportDateId;
            $(driverTransportsTablePassengerToChange).remove();
        },
        error: function(e) {
            //TODO configurar la alerta para que se muestre en un idioma u otro
            window.alert("Ha ocurrido un error desasignar el conductor.");
        }
    })
}

function addPassengerInDriverTransportsTable(transportDateId, driverId, passengerId, passengerFullName) {
    //TODO when UUIDs are implemented, change the id and remove the p, d, t...
    const driverTransportsTablePassengerTdToAddSpan = 'd' + driverId + 't' + transportDateId;
    const newPassengerSpanNode = document.createElement("span");
    const newPassengerSpanTextNode = document.createTextNode(passengerFullName);
    newPassengerSpanNode.appendChild(newPassengerSpanTextNode);
    newPassengerSpanNode.setAttribute('id', 'p' + passengerId + driverTransportsTablePassengerTdToAddSpan);3

    $('#' + driverTransportsTablePassengerTdToAddSpan).append(newPassengerSpanNode);
}

function deletePassengerInDriverTransportsTable(transportDateId, passengerId, response) {
    //TODO when UUIDs are implemented, change the id and remove the p, d, t...
    const oldDriverId = response.data.p;
    const deleteId = 'p' + passengerId + 'd' + oldDriverId + 't' + transportDateId;

    const driverTransportsTablePassengerSpanToDelete = '#' + deleteId;
    $(driverTransportsTablePassengerSpanToDelete).remove();

    const driverTransportsTablePassengerBrToDelete = '#br' + deleteId;
    $(driverTransportsTablePassengerBrToDelete).remove();
}

function operate () {
    const selectedOption = $(this).find('option:selected');
    const driverId = $(this).val();
    const passengerId = selectedOption.attr('data-t');
    const transportDateId = selectedOption.attr('data-d');
    const passengerFullName = selectedOption.attr('data-passenger-name');
    const methodName = selectedOption.attr('name');
    const operations = {
        d: deleteTransportOption,
        u: updateDriverInTransportOption,
        c: createTransportOption,
    };

    const method = operations[methodName];
    method(transportDateId, driverId, passengerId, passengerFullName);
}

$(document).ready(
    function () {
        $('select[name="driverInTransportSelect"]').on('change', operate);
    }
);