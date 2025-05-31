$(function() {
    const $tabButtons = $('#templateTabs button[data-bs-toggle="tab"]');
    const tabButtons = Array.from($tabButtons);

    tabButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault();
            const tab = new bootstrap.Tab(this);
            tab.show();
        });
    });
});