document.addEventListener('DOMContentLoaded', function() {
    const tabButtons = document.querySelectorAll('#templateTabs button[data-bs-toggle="tab"]');

    tabButtons.forEach(button => {
        button.addEventListener('shown.bs.tab', function(event) {
            const targetTab = event.target.getAttribute('data-bs-target');
        });
    });
});