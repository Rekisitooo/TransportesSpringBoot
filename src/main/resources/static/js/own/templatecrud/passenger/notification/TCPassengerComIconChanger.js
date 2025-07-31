/**
 * Changes the passenger notification icon based on the transport deletion.
 * @param {Object} data - Contains passengerId (t), passengerId (p), and transportDateId (d)
 * @param {jQuery} passengerCommunicationIcon - The warning icon element for the passenger
 */
export async function changePassengerComIconOnTransportDeletion(data, passengerCommunicationIcon) {
    if (passengerCommunicationIcon.hasClass("text-danger")) {
        const passengerNotifications = await getPassengerNotifications(data);

        if (!passengerNotifications?.data?.length) {
            passengerCommunicationIcon.addClass("d-none");
        }

    } else if (passengerCommunicationIcon.hasClass("text-primary")) {
        passengerCommunicationIcon.removeClass("text-primary");
        passengerCommunicationIcon.addClass("text-danger");
    }
}

/**
 * Updates the warning icon visibility for a passenger based on their notification status.
 * Shows the icon if the passenger is not in the passenger's notifications or if there are no notifications.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} passengerCommunicationIcon - The warning icon element for the passenger
 */
export async function changePassengerComIconOnPassengerSelection(data, passengerCommunicationIcon) {
    if (passengerCommunicationIcon.hasClass("d-none")) {
        passengerCommunicationIcon.removeClass("d-none");
        passengerCommunicationIcon.removeClass("text-primary");
        passengerCommunicationIcon.addClass("text-danger");

    } else {
        const passengerNotifications = await getPassengerNotifications(data);
        const hasPassengerInNotifications = passengerNotifications.data.some(
            notification => notification.driverId === data.p
        );

        if (passengerNotifications.data.driverId === data.p) {
            passengerCommunicationIcon.removeClass("text-danger");
            passengerCommunicationIcon.addClass("text-primary");
        } else {
            passengerCommunicationIcon.removeClass("text-primary");
            passengerCommunicationIcon.addClass("text-danger");
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
            url: '/involvedCommunication/get',
            data: {
                involvedCommunicatedId: data.t,
                transportDateCode: data.d
            }
        });
    } catch (error) {
      return null;
    }
}