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

    if (!this.checked) {
        $('#btnCreateNewDate').attr('disabled', this.checked);
        $('#creationDateDisabledReason').text('');
    }
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

        $('#addDateCardDateForm').submit(validateFormFields);

        loadDatePicker();
    }
);