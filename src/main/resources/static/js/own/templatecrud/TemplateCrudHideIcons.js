import { changeElementClass } from './TemplateCrudCommons.js';

$(function() {
    $('#passengerTableHideIconsBtn').on('change', function () {
        if (this.checked) {
            showIcons('div[id*=passenger_icon_row_]');
        } else {
            hideIcons('div[id*=passenger_icon_row_]');
        }
    });

    $('#driverTableHideIconsBtn').on('change', function () {
            if (this.checked) {
                showIcons('div[id*=driver_icon_row_]');
            } else {
                hideIcons('div[id*=driver_icon_row_]');
            }
        });
});

function showIcons(selector) {
    $(selector).each(function() {
        let elementClass = changeElementClass($(this), '', ' d-none');
        $(this).attr('class', elementClass);
    });
}

function hideIcons(selector) {
    $(selector).each(function() {
        let elementClass = $(this).attr('class') + ' d-none';
        $(this).attr('class', elementClass);
    });
}