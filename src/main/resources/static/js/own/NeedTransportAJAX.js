/**
 * Updates a passenger's need for transport
 * 
 * @param {object} data - Contains the transport date id, driver id for where clause, 
 * and the needs transport value to update
 */
export async function updatePassengerNeedForTransport(data, passengerId) {
    try {
        await $.ajax({
            type: 'PATCH',
            contentType: 'application/json',
            url: '/involvedAvailability/updateNeedForTransport/' + passengerId,
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
 * Gets all the transports for the driver
 * @param {object} data - Contains the transportDateCode and driverId
 */
export async function getDriverPassengersForDate(data) {
    try {
        return await $.ajax({
            type: 'GET',
            url: '/t/getPassengersForDriverByDate',
            data: data
        });

    } catch (error) {
        console.error('Error en la petición AJAX:', error);
        return null;
    }
}

/**
 * Gets all the transports for the driver
 * @param {object} data - Contains the transportDateId and passengerId
 */
export async function getDriverForPassengerByDate(data) {
    try {
        return await $.ajax({
           type: 'GET',
           url: '/t/getDriverForPassengerByDate',
           data : data
       });

    } catch (error) {
        console.error('Error en la petición AJAX:', error);
        return null;
    }
}