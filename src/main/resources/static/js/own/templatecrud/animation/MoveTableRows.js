const rows = document.querySelectorAll('tbody tr');

rows.forEach(row => {
    row.addEventListener('dragstart', () => {
        row.classList.add('dragging');
    });

    row.addEventListener('dragend', () => {
        row.classList.remove('dragging');
    });

    row.addEventListener('dragover', (e) => {
        e.preventDefault(); // Necesario para permitir el drop
        const draggingRow = document.querySelector('.dragging');
        const currentRow = row;

        // Verifica si la fila actual es diferente a la que se está arrastrando
        if (draggingRow !== currentRow) {
            const bounding = currentRow.getBoundingClientRect();
            const offset = bounding.y + bounding.height / 2;
            if (e.clientY - offset > 0) {
                currentRow.parentNode.insertBefore(draggingRow, currentRow.nextSibling);
            } else {
                currentRow.parentNode.insertBefore(draggingRow, currentRow);
            }
        }
    });
});