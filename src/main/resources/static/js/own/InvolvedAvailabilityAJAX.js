/**
 * Deletes the passenger assistance in db
 * @param {Object} data with transportDateId and involvedId
 * @returns 
 */
export async function deleteInvolvedAssistance(data) {
    try {
        await $.ajax({
            url: '/involvedAvailability',
            type: 'DELETE',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function() {
                return true;
            },
            
            error: function(xhr, status, error) {
                console.error('Error en la petición AJAX:', error);
                return false;
            }
        });

        return true;

    } catch (error) {
        console.error('Error en la petición AJAX:', error);
        return false;
    }
}

/**
 * Creates the passenger assistance in db
 * @param {Object} data with transportDateId and involvedId
 * @returns 
 */
export async function createInvolvedAssistance(data) {
    try {
        await $.ajax({
            url: '/involvedAvailability',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function() {
                return true;
            },
            
            error: function(xhr, status, error) {
                console.error('Error en la petición AJAX:', error);
                return false;
            }
        });

        return true;

    } catch (error) {
        console.error('Error en la petición AJAX:', error);
        return false;
    }
}