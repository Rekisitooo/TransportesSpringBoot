/**
 * Changes the passenger notification icon based on the transport deletion.
 * @param {Object} data - Contains passengerId (t), passengerId (p), and transportDateId (d)
 * @param {jQuery} passengerNotificationIcon - The warning icon element for the passenger
 */
export async function changePassengerComIconOnTransportDeletion(data, passengerNotificationIcon) {
    if (passengerNotificationIcon.hasClass("text-danger")) {
        const passengerNotifications = await getPassengerNotifications(data);

        if (!passengerNotifications?.data?.length) {
            passengerNotificationIcon.addClass("d-none");
        }

    } else if (passengerNotificationIcon.hasClass("text-primary")) {
        passengerNotificationIcon.removeClass("text-primary");
        passengerNotificationIcon.addClass("text-danger");
    }
}

/**
 * Updates the warning icon visibility for a passenger based on their notification status.
 * Shows the icon if the passenger is not in the passenger's notifications or if there are no notifications.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} passengerNotificationIcon - The warning icon element for the passenger
 */
export async function changePassengerComIconOnPassengerSelection(data, passengerNotificationIcon) {
    if (passengerNotificationIcon.hasClass("d-none")) {
        passengerNotificationIcon.removeClass("d-none");
        passengerNotificationIcon.removeClass("text-primary");
        passengerNotificationIcon.addClass("text-danger");

    } else {
        const passengerNotifications = await getPassengerNotifications(data);
        const hasPassengerInNotifications = passengerNotifications.data.some(
            notification => notification.driverId === data.p
        );

        if (passengerNotifications.data.driverId === data.p) {
            passengerNotificationIcon.removeClass("text-danger");
            passengerNotificationIcon.addClass("text-primary");
        } else {
            passengerNotificationIcon.removeClass("text-primary");
            passengerNotificationIcon.addClass("text-danger");
        }
    }
 }

/**
 * Retrieves the passenger notifications for a given passenger and transport date.
 * @param {Object} data - Contains passengerId (t), passengerId (p), and transportDateId (d)
 * @returns {Promise<Object|null>} - A promise that resolves to the response data or null if an error occurs.
 */
async function getPassengerNotifications(data) {
    try {
        return await $.ajax({
            type: 'GET',
            url: '/involvedNotification/get',
            data: {
                notifiedInvolvedId: data.t,
                transportDateCode: data.d
            }
        });
    } catch (error) {
      return null;
    }
}