/**
 * Changes the driver notification icon based on the transport deletion.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} driverNotificationIcon - The warning icon element for the driver
 */
export async function changeDriverNotifIconOnTransportDeletion(data, driverNotificationIcon) {

    if (driverNotificationIcon.hasClass("text-danger")) {
        const driverNotifications = await getDriverNotifications(data);

        if (!driverNotifications?.data?.length) {
            driverNotificationIcon.addClass("d-none");
        } else {
            const hasInvolvedId = driverNotifications.data.some(
                notification => notification.involvedId === data.involvedId
            );

            if (hasInvolvedId) {
                driverNotificationIcon.removeClass("text-danger");
                driverNotificationIcon.addClass("text-primary");
            }
        }

    } else if (driverNotificationIcon.hasClass("text-primary")) {
        driverNotificationIcon.removeClass("text-primary");
        driverNotificationIcon.addClass("text-danger");
    }

}

/**
 * Updates the warning icon visibility for a driver based on their notification status.
 * Shows the icon if the passenger is not in the driver's notifications or if there are no notifications.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} driverNotificationIcon - The warning icon element for the driver
 */
export async function changeDriverNotifIconOnDriverSelection(data, driverNotificationIcon) {
    if (driverNotificationIcon.hasClass("d-none")) {
        driverNotificationIcon.removeClass("d-none");
        driverNotificationIcon.removeClass("text-primary");
        driverNotificationIcon.addClass("text-danger");

    } else {
        const driverNotifications = await getDriverNotifications(data);
        const hasPassengerInNotifications = driverNotifications.data.some(
            notification => notification.passengerId === data.t
        );

        if (!hasPassengerInNotifications) {
            driverNotificationIcon.removeClass("text-danger");
            driverNotificationIcon.addClass("text-primary");
        } else {
            driverNotificationIcon.removeClass("text-primary");
            driverNotificationIcon.addClass("text-danger");
        }
    }
 }

/**
 * Retrieves the driver notifications for a given driver and transport date.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @returns {Promise<Object|null>} - A promise that resolves to the response data or null if an error occurs.
 */
async function getDriverNotifications(data) {
    try {
        return await $.ajax({
            type: 'GET',
            url: '/involvedTransportNotification/get',
            data: {
                notifiedInvolvedId: data.p,
                transportDateCode: data.d
            }
        });
    } catch (error) {
      return null;
    }
}