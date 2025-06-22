/**
 * Changes the driver communication icon based on the transport deletion.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} driverCommunicationIcon - The warning icon element for the driver
 */
export async function changeDriverComIconOnTransportDeletion(data, driverCommunicationIcon) {

    if (driverCommunicationIcon.hasClass("text-danger")) {
        const driverCommunications = await getDriverCommunications(data);

        if (!driverCommunications?.data?.length) {
            driverCommunicationIcon.addClass("d-none");
        } else {
            const hasInvolvedId = driverCommunications.data.some(
                communication => communication.involvedId === data.involvedId
            );

            if (hasInvolvedId) {
                driverCommunicationIcon.removeClass("text-danger");
                driverCommunicationIcon.addClass("text-muted");
            }
        }

    } else if (driverCommunicationIcon.hasClass("text-muted")) {
        driverCommunicationIcon.removeClass("alert-muted");
        driverCommunicationIcon.addClass("text-danger");
    }

}

/**
 * Updates the warning icon visibility for a driver based on their communication status.
 * Shows the icon if the passenger is not in the driver's communications or if there are no communications.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} driverCommunicationIcon - The warning icon element for the driver
 */
export async function changeDriverComIconOnDriverSelection(data, driverCommunicationIcon) {
    if (driverCommunicationIcon.hasClass("d-none")) {
        driverCommunicationIcon.removeClass("d-none");
        driverCommunicationIcon.removeClass("text-muted");
        driverCommunicationIcon.addClass("text-danger");

    } else {
        const driverCommunications = await getDriverCommunications(data);
        const hasPassengerInCommunications = driverCommunications.data.some(
            communication => communication.passengerId === data.t
        );

        if (!hasPassengerInCommunications) {
            driverCommunicationIcon.removeClass("text-danger");
            driverCommunicationIcon.addClass("text-muted");
        } else {
            driverCommunicationIcon.removeClass("text-muted");
            driverCommunicationIcon.addClass("text-danger");
        }
    }
 }

/**
 * Retrieves the driver communications for a given driver and transport date.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @returns {Promise<Object|null>} - A promise that resolves to the response data or null if an error occurs.
 */
async function getDriverCommunications(data) {
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