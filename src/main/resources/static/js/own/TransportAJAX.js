/**
 * Deletes a transport
 * @param {object} data - Contains the transport date id, previous driver id and passenger id
 */
export async function deleteTransport(data) {
    try {
        return await $.ajax({
            type: 'DELETE',
            contentType: 'application/json',
            url: '/t',
            data: JSON.stringify(data),
            dataType: 'json'
        });
    } catch (error) {
        return null;
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
      return null;
    }
}