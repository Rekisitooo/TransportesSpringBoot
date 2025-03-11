function addColumnToPassengersTable(dateData) {

}

function addColumnToDriversTable(dateData) {

}

function addInvolvedAvailabilityToAddDateCard() {
    const isChecked = this.checked;
    $('#addDateCardAvailability').toggle(isChecked);

    if (this.checked) {
        enableDisableCreateDateButton();
    } else {
        $('#btnCreateNewDate').attr('disabled', this.checked);
        $('#creationDateDisabledReason').text('');
    }
}

function createDate() {

}

function enableDisableAvailabilityReason() {
    const availabilityReasonInputId = $(this).attr('id');
    if (this.checked) {
        $('#' + availabilityReasonInputId + 'Input').hide();
    } else {
        $('#' + availabilityReasonInputId + 'Input').show();
    }
}

function enableDisableCreateDateButton() {
    const passengerNodes = $("input[type='checkbox'][name='addDateCardPassengerAvailabilityCheck']");
    let selectedPassengerArray = [];
    passengerNodes.each(function() {
        if (this.checked) {
            selectedPassengerArray.push(this);
        }
    });

    const driverNodes = $("input[type='checkbox'][name='addDateCardDriverAvailabilityCheck']");
    let selectedDriverArray = [];
    driverNodes.each(function() {
        if (this.checked) {
            selectedDriverArray.push(this);
        }
    });

    let passengersArranged = 0;
    let driverAvailableSeats = 0;
    const createDateButtonNode = $('#btnCreateNewDate');
    const creationDateDisabledReasonNode = $('#creationDateDisabledReason');
    if (selectedDriverArray.length == 0) {
        creationDateDisabledReasonNode.text('Debes añadir algún conductor.');
        createDateButtonNode.attr('disabled', true);
    } else if (selectedPassengerArray.length == 0) {
        creationDateDisabledReasonNode.text('Añade algún viajero.');
        createDateButtonNode.attr('disabled', true);
    } else {
        $.each(selectedDriverArray, function() {
            const seats = $(this).attr('data-seats');
            driverAvailableSeats += parseInt(seats);
        });

        $.each(selectedPassengerArray, function() {
            const seats = $(this).attr('data-seats');
            passengersArranged += parseInt(seats);
        });

        if (driverAvailableSeats < passengersArranged) {
            creationDateDisabledReasonNode.text('Hay seleccionados más viajeros que plazas en los coches.');
            createDateButtonNode.attr('disabled', true);
        } else {
            creationDateDisabledReasonNode.text('');
            createDateButtonNode.attr('disabled', false);
        }
    }
    
    $('#selectedPassengerAvailableSeats').text(passengersArranged);
    $('#selectedDriverAvailableSeats').text(driverAvailableSeats);
    $('#selectedPassengerTotal').text(' (' + selectedPassengerArray.length);
    $('#selectedDriverTotal').text(' (' + selectedDriverArray.length);
}

$(document).ready(
    function () {
        $('#addDateCardIsTransportDateCheckboxInput').on('click', addInvolvedAvailabilityToAddDateCard);
        $('#btnCreateNewDate').on('click', createDate);

        $("input[type='checkbox'][name='addDateCardDriverAvailabilityCheck']").each(function() {
          $(this).on('click', enableDisableAvailabilityReason);
          $(this).on('click', enableDisableCreateDateButton);
        });

        $("input[type='checkbox'][name='addDateCardPassengerAvailabilityCheck']").each(function() {
          $(this).on('click', enableDisableAvailabilityReason);
          $(this).on('click', enableDisableCreateDateButton);
        });
    }
);