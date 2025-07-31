/**
 * Changes the driver notification icon based on the transport deletion.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} driverCommunicationIcon - The warning icon element for the driver
 */
export async function changeDriverComIconOnTransportDeletion(data, driverCommunicationIcon) {

    if (driverCommunicationIcon.hasClass("text-danger")) {
        const driverNotifications = await getDriverNotifications(data);

        if (!driverNotifications?.data?.length) {
            driverCommunicationIcon.addClass("d-none");
        } else {
            const hasInvolvedId = driverNotifications.data.some(
                notification => notification.involvedId === data.involvedId
            );

            if (hasInvolvedId) {
                driverCommunicationIcon.removeClass("text-danger");
                driverCommunicationIcon.addClass("text-primary");
            }
        }

    } else if (driverCommunicationIcon.hasClass("text-primary")) {
        driverCommunicationIcon.removeClass("text-primary");
        driverCommunicationIcon.addClass("text-danger");
    }

}

/**
 * Updates the warning icon visibility for a driver based on their notification status.
 * Shows the icon if the passenger is not in the driver's notifications or if there are no notifications.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} driverCommunicationIcon - The warning icon element for the driver
 */
export async function changeDriverComIconOnDriverSelection(data, driverCommunicationIcon) {
    if (driverCommunicationIcon.hasClass("d-none")) {
        driverCommunicationIcon.removeClass("d-none");
        driverCommunicationIcon.removeClass("text-primary");
        driverCommunicationIcon.addClass("text-danger");

    } else {
        const driverNotifications = await getDriverNotifications(data);
        const hasPassengerInNotifications = driverNotifications.data.some(
            notification => notification.passengerId === data.t
        );

        if (!hasPassengerInNotifications) {
            driverCommunicationIcon.removeClass("text-danger");
            driverCommunicationIcon.addClass("text-primary");
        } else {
            driverCommunicationIcon.removeClass("text-primary");
            driverCommunicationIcon.addClass("text-danger");
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
            url: '/involvedCommunication/get',
            data: {
                involvedCommunicatedId: data.p,
                transportDateCode: data.d
            }
        });
    } catch (error) {
      return null;
    }
}