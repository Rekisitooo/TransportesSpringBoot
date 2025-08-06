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