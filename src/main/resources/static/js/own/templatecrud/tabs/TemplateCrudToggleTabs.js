import { changeElementClass } from '../TemplateCrudCommons.js';
import { temporalErrorAlert } from '../alert/GenericErrorAlert.js';

$(function() {
    const $tabButtons = $('#templateTabs button[data-bs-toggle="tab"]');
    const tabButtons = Array.from($tabButtons);

    tabButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault();

            // hides the tab content of the tab unselected
            $('[data-hideable]').each(function() {
                let elementClass = changeElementClass($(this), 'd-none', 'd-block');
                $(this).attr('class', elementClass);
            });

            const tabName = $(this).attr('aria-controls');
            const tabElementsToShow = $('[data-tab = ' + tabName + ']');

            if (tabName === 'communicationsTab') {
                const templateId = $('#templateTitle').attr('data-template-id');
                const dataCall = $(this).attr('data-call');
                insertTabHTML('/template/open' + dataCall + '?id=' + templateId, tabName);
            } else {
                tabElementsToShow.each(function() {
                   let elementClass = changeElementClass($(this), 'd-block', 'd-none');
                   $(this).attr('class', elementClass);
               });
            }

            const tab = new bootstrap.Tab(this);
            tab.show();

            $('[data-tabs="generalTabs"]').each(function() {
                $(this).removeClass('show active');
            });

            $('#' + tabName).addClass('show active');
        });
    });
});

async function insertTabHTML(urlCall, tabName) {
    try {
        const response =
            await $.ajax({
                type: 'GET',
                url: urlCall
            });
        $('#' + tabName).html(response);
    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al recuperar la información de la pestaña.");
    }
}