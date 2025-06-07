$(function() {
    const $tabButtons = $('#templateTabs button[data-bs-toggle="tab"]');
    const tabButtons = Array.from($tabButtons);

    tabButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault();
            $('[data-hideable]').remove();
            const tabName = $(this).attr('aria-controls');
            const templateId = $('#templateTitle').attr('data-template-id');
            const dataCall = $(this).attr('data-call');
            insertTabHTML('/template/open' + dataCall + '?id=' + templateId, tabName);

            const tab = new bootstrap.Tab(this);
            tab.show();
        });
    });
});

async function insertTabHTML(urlCall, tabName) {
    const response =
        await $.ajax({
            type: 'GET',
            url: urlCall
        });
    $('#' + tabName).html(response);
}