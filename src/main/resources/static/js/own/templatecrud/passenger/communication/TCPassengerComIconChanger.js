/**
 * Changes the passenger communication icon based on the transport deletion.
 * @param {Object} data - Contains passengerId (t), passengerId (p), and transportDateId (d)
 * @param {jQuery} passengerCommunicationIcon - The warning icon element for the passenger
 */
export async function changePassengerComIconOnTransportDeletion(data, passengerCommunicationIcon) {
    if (passengerCommunicationIcon.hasClass("text-danger")) {
        const passengerCommunications = await getPassengerCommunications(data);

        if (!passengerCommunications?.data?.length) {
            passengerCommunicationIcon.addClass("d-none");
        }

    } else if (passengerCommunicationIcon.hasClass("text-muted")) {
        passengerCommunicationIcon.removeClass("alert-muted");
        passengerCommunicationIcon.addClass("text-danger");
    }
}

/**
 * Updates the warning icon visibility for a passenger based on their communication status.
 * Shows the icon if the passenger is not in the passenger's communications or if there are no communications.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} passengerCommunicationIcon - The warning icon element for the passenger
 */
export async function changePassengerComIconOnPassengerSelection(data, passengerCommunicationIcon) {
    if (passengerCommunicationIcon.hasClass("d-none")) {
        passengerCommunicationIcon.removeClass("d-none");
        passengerCommunicationIcon.removeClass("text-muted");
        passengerCommunicationIcon.addClass("text-danger");

    } else {
        const passengerCommunications = await getPassengerCommunications(data);
        const hasPassengerInCommunications = passengerCommunicationResponse.data.some(
            communication => communication.driverId === data.p
        );

        if (passengerCommunicationResponse.data.driverId === data.p) {
            passengerCommunicationIcon.removeClass("text-danger");
            passengerCommunicationIcon.addClass("text-muted");
        } else {
            passengerCommunicationIcon.removeClass("text-muted");
            passengerCommunicationIcon.addClass("text-danger");
        }
    }
 }

/**
 * Retrieves the passenger communications for a given passenger and transport date.
 * @param {Object} data - Contains passengerId (t), passengerId (p), and transportDateId (d)
 * @returns {Promise<Object|null>} - A promise that resolves to the response data or null if an error occurs.
 */
async function getPassengerCommunications(data) {
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