const rows = document.querySelectorAll('tbody tr');

rows.forEach(row => {
    const dragHandle = row.querySelector('th[data-draggable=involvedTable]');

    dragHandle.addEventListener('dragstart', () => {
        row.classList.add('dragging');
        e.dataTransfer.setData('text/plain', null); // Required for Firefox
    });

    dragHandle.addEventListener('dragend', () => {
        row.classList.remove('dragging');
    });

    row.addEventListener('dragover', (e) => {
        e.preventDefault();
        const draggingRow = document.querySelector('.dragging');

        if (draggingRow !== row) {
            const bounding = row.getBoundingClientRect();
            const offset = bounding.y + bounding.height / 2;
            if (e.clientY - offset > 0) {
                row.parentNode.insertBefore(draggingRow, row.nextSibling);
            } else {
                row.parentNode.insertBefore(draggingRow, row);
            }
        }
    });
});