/**
 * Gets all the notifications for the driver
 * @param {object} data - Contains the transportDateCode and involvedNotifiedId
 */
export async function getInvolvedNotifications(data) {
    try {
        return await $.ajax({
            type: 'GET',
            url: '/involvedTransportNotification/get',
            data: data
        });

    } catch (error) {
      return null;
    }
}

/**
 * Updates the passenger notification with a new driver
 * @param {object} data - Contains the transportDateCode, notifiedInvolvedId and driverCode
 */
export async function updatePassengerNotification(data) {
    try {
        await $.ajax({
            type: 'PATCH',
            contentType: 'application/json',
            url: '/involvedTransportNotification/updateDriver',
            data: JSON.stringify(data),
        });
        return true;

    } catch (error) {
        return false;
    }
}

/**
 * Creates a new transport notification for the passenger in db
 * 
 * @param {Object} data 
 * @returns 
 */
export async function createPassengerNotification(data) {
    try {
        return await $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/involvedTransportNotification/createNotification',
            data: JSON.stringify(data),
            dataType: 'json'
        });

    } catch (error) {
        return null;
    }
}

/**
 * Deletes the transport notification for the passenger in db
 * @param {Object} data 
 * @returns 
 */
export async function deletePassengerNotification(data) {
    try {
        await $.ajax({
            type: 'DELETE',
            contentType: 'application/json',
            url: '/involvedTransportNotification',
            data: JSON.stringify(data),
            dataType: 'json'
        });
        return true;

    } catch (error) {
        return false;
    }
}