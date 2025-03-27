function getDatesToDisable() {
    const datesToDisable = [];
    $('th[name=monthDate]').each(function() {
        const date = $(this).attr('data-date');
        datesToDisable.push(date);
    });

    return datesToDisable;
}

function validateFormFields(event) {
    let returnValue = true;
    const evenNameInput = event.target[0];

    if (!isValidEventName(evenNameInput.value)) {
        event.preventDefault();
        Swal.fire({
          icon: "error",
          title: "No se puede crear la fecha",
          text: "El nombre del evento es demasiado largo.",
        });
    }

    return returnValue;
}

function isValidEventName(eventName) {
    const body = document.querySelector('body');
    const span = document.createElement("span");
    body.appendChild(span);
    span.style.visibility = "hidden";
    span.style.position = "absolute";
    span.style.fontSize = "11px";
    span.style.fontFamily = "Aptos Narrow";
    span.textContent = "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii";

    const maximumLarge = span.offsetWidth;
    span.textContent = eventName;
    const eventNameWidth = span.offsetWidth;

    span.style.visibility = "hidden";
    if (eventNameWidth > maximumLarge) {
        return false;
    } else {
        return true;
    }
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

export function loadDatePicker() {
    const datepickerElement = $("#addDateCardDateInput");
    const datesToDisable = getDatesToDisable();
    const date = new Date(datesToDisable[0]);
    const firstDayOfTheMonthDate = new Date(date.getFullYear(), date.getMonth(), 1);
    const lastDayOfTheMonthDate = new Date(date.getFullYear(), date.getMonth() + 1, 0);

    datepickerElement.datepicker('destroy');

    datepickerElement.datepicker({
        startDate: firstDayOfTheMonthDate,
        endDate: lastDayOfTheMonthDate,
        format: "yyyy-mm-dd",
        datesDisabled: datesToDisable
    });

    //Avoid showing default datepick
    datepickerElement.on('click',
        function(){
            event.preventDefault();
        }
    );

    //Hide the datepicker when choosing date
    datepickerElement.on('change',
        function(){
            $(this).datepicker('hide');
        }
    );
}

$(document).ready(
    function () {
        $('#addDateCardIsTransportDateCheckboxInput').on('click', addInvolvedAvailabilityToAddDateCard);

        $("input[type='checkbox'][name='addDateCardDriverAvailabilityCheck']").each(function() {
          $(this).on('click', enableDisableCreateDateButton);
        });

        $("input[type='checkbox'][name='addDateCardPassengerAvailabilityCheck']").each(function() {
          $(this).on('click', enableDisableCreateDateButton);
        });

        $('#addDateCardDateForm').submit(validateFormFields);

        loadDatePicker();
    }
);