import { changeElementClass } from './TemplateCrudCommons.js';

$(function() {
    $('#passengerTableHideIconsBtn').on('change', function () {
        if (this.checked) {
            showIcons('[data-passenger-showable-icon]');
        } else {
            hideIcons('[data-passenger-showable-icon]');
        }
    });

    $('#driverTableHideIconsBtn').on('change', function () {
            if (this.checked) {
                showIcons('[data-driver-showable-icon]');
            } else {
                hideIcons('[data-driver-showable-icon]');
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