import { temporalErrorAlert } from './alert/GenericErrorAlert.js';
import { changeElementClass } from './TemplateCrudCommons.js';

$(function() {
    $('select[name="driverInTransportSelect"]').each(function() {
        $(this).data('previous-driver-id', $(this).val());
    }).on('change', function (event) {
        const previousDriverId = $(this).data('previous-driver-id');
        operate(this, previousDriverId);
        $(this).data('previous-driver-id', $(this).val());
    });
});

const SELECTORS = {
    DRIVER_PASSENGERS_DIV: (newDriverId, transportDateId) => `div[id*=driverPassengersOnDate_${newDriverId}_${transportDateId}_${passengerId}]`,
    DRIVER_TRANSPORTS_TABLE_TD: (newDriverId, transportDateId) => `#driverPassengersForDateDiv_${newDriverId}_${transportDateId}`,
    DRIVER_WARNING_ICON: (driverId, transportDateId) => `#d${driverId}t${transportDateId}_date_td .fa-exclamation-circle`,
    PASSENGER_WARNING_ICON: (passengerId, transportDateId) => `#p${passengerId}t${transportDateId}_date_td .fa-exclamation-circle`
};

function getTransportElements(transportDateId, newDriverId, passengerId, previousDriverId = null) {
    const elements = {
        driverPassengersDivId: $(SELECTORS.DRIVER_PASSENGERS_DIV(newDriverId, transportDateId)),
        driverTransportsTablePassengerTdToAddSpan: $(SELECTORS.DRIVER_TRANSPORTS_TABLE_TD(newDriverId, transportDateId)),
        newDriverWarningIcon: $(SELECTORS.DRIVER_WARNING_ICON(newDriverId, transportDateId)),
        passengerWarningIcon: $(SELECTORS.PASSENGER_WARNING_ICON(passengerId, transportDateId))
    };
    if (previousDriverId && previousDriverId > 0) {
        elements.previousDriverWarningIcon = $(SELECTORS.DRIVER_WARNING_ICON(previousDriverId, transportDateId));
    }

    return elements;
}

/**
 * Updates the warning icon visibility for a passenger based on their communication status.
 * Shows the icon if the driver has changed or if there are no communications.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} passengerWarningIcon - The warning icon element for the passenger
 */
async function updatePassengerWarningIcon(data, passengerWarningIcon) {
    try {
        const passengerCommunicationResponse = 
            await $.ajax({
                type: 'GET',
                url: '/involvedCommunication/get',
                data: {
                    involvedCommunicatedId: data.t,
                    transportDateCode: data.d
                }
            });

        let passengerWarningIconClass;

        //if there were communications
        if (passengerCommunicationResponse?.data?.length) {
            const previousDriverId = passengerCommunicationResponse.data[0].driverId;
            const hasChangedDriver = previousDriverId !== data.p;

            let passengerWarningIconClass;
            if (hasChangedDriver) { //icon turns red
                passengerWarningIconClass = changeElementClass(passengerWarningIcon, 'text-danger', 'text-muted');
                passengerWarningIcon.attr('class', passengerWarningIconClass);
            } else { //icon turns blue
                passengerWarningIconClass = changeElementClass(passengerWarningIcon, 'text-muted', 'text-danger');
                passengerWarningIcon.attr('class', passengerWarningIconClass);
            }
        } else {
            //if no communications, icon turns red
            passengerWarningIconClass = changeElementClass(passengerWarningIcon, 'text-danger', 'text-muted');
            passengerWarningIcon.attr('class', passengerWarningIconClass);
        }


    } catch (error) { //if error, icon turns red
        passengerWarningIconClass = changeElementClass(passengerWarningIcon, 'text-danger', 'text-muted');
        passengerWarningIcon.attr('class', passengerWarningIconClass);
    }
}

/**
 * Updates the warning icon visibility for a driver based on their communication status.
 * Shows the icon if the passenger is not in the driver's communications or if there are no communications.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} driverWarningIcon - The warning icon element for the driver
 */
async function updateDriverWarningIcon(data, driverWarningIcon) {
    try {
        const driverCommunicationResponse =
        await $.ajax({
            type: 'GET',
            url: '/involvedCommunication/get',
            data: {
                involvedCommunicatedId: data.p,
                transportDateCode: data.d
            }
        });

        let driverWarningIconClass;

        //if there were communications
        if (driverCommunicationResponse?.data?.length) {
            const hasPassengerInCommunications = driverCommunicationResponse.data.some(
                communication => communication.passengerId === data.t
            );

            if (hasPassengerInCommunications) { // icon turns blue
                driverWarningIconClass = changeElementClass(driverWarningIcon, 'text-muted', 'text-danger');
                driverWarningIcon.attr('class', driverWarningIconClass);
            } else { // icon turns red
                driverWarningIconClass = changeElementClass(driverWarningIcon, 'text-danger', 'text-muted');
                driverWarningIcon.attr('class', driverWarningIconClass);
            }
        } else {
             // if no communications, icon turns red
            driverWarningIconClass = changeElementClass(driverWarningIcon, 'text-danger', 'text-muted');
            driverWarningIcon.attr('class', driverWarningIconClass);
        }

    } catch (error) { // if error, icon turns red
        driverWarningIconClass = changeElementClass(driverWarningIcon, 'text-danger', 'text-muted');
        driverWarningIcon.attr('class', driverWarningIconClass);
    }
}

/**
 * Updates the driver assigned to a transport and manages related UI updates.
 * @param {number} transportDateId - ID of the transport date
 * @param {number} newDriverId - ID of the new driver
 * @param {number} passengerId - ID of the passenger
 * @param {string} passengerFullName - Full name of the passenger
 * @param {number} previousDriverId - ID of the previous driver (optional)
 */
async function updateDriverInTransportOption(transportDateId, newDriverId, passengerId, passengerFullName, previousDriverId = null) {
    const data = {
        d: transportDateId,
        p: newDriverId,
        t: passengerId
    };

    const elements = getTransportElements(transportDateId, newDriverId, passengerId, previousDriverId);

    try {
        const response = await $.ajax({
            type: 'PUT',
            contentType: 'application/json',
            url: '/t/updateDriver',
            data: JSON.stringify(data),
            dataType: 'json'
        });

        if (response?.status === 'ok') {
            addPassengerInDriverTransportsTable(transportDateId, newDriverId, passengerId, passengerFullName, elements.driverTransportsTablePassengerTdToAddSpan);
            deletePassengerInDriverTransportsTable(transportDateId, passengerId, response.data.p, elements.driverPassengersDivId);
            await updatePassengerWarningIcon(data, elements.passengerWarningIcon);
            await updateDriverWarningIcon(data, elements.newDriverWarningIcon);

            //if there was a previous driver, update its warning icon
            if (previousDriverId && previousDriverId > 0 && elements.previousDriverWarningIcon?.length) {
                const previousDriverData = { ...data, p: previousDriverId };
                await updateDriverWarningIcon(previousDriverData, elements.previousDriverWarningIcon);
            }

        } else {
            temporalErrorAlert("Ha ocurrido un error al actualizar el conductor.");
        }
    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al actualizar el conductor.");
    }
}

/**
 * Creates a new transport assignment and manages related UI updates.
 * @param {number} transportDateId - ID of the transport date
 * @param {number} newDriverId - ID of the new driver
 * @param {number} passengerId - ID of the passenger
 * @param {string} passengerFullName - Full name of the passenger
 * @param {jQuery} actualSelect - The select element being modified
 */
async function createTransportOption(transportDateId, newDriverId, passengerId, passengerFullName, actualSelect) {
    const data = {
        d: transportDateId,
        p: newDriverId,
        t: passengerId
    };

    const elements = getTransportElements(transportDateId, newDriverId, passengerId);

    try {
        await $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/t',
            data: JSON.stringify(data),
            dataType: 'json'
        });

        addPassengerInDriverTransportsTable(transportDateId, newDriverId, passengerId, passengerFullName, elements.driverTransportsTablePassengerTdToAddSpan);
        actualSelect.children().attr("name", "u");
        actualSelect.children(":first").attr("name", "d");
        await Promise.all([
            updatePassengerWarningIcon(data, elements.passengerWarningIcon),
            updateDriverWarningIcon(data, elements.newDriverWarningIcon)
        ]);
    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al asignar el conductor.");
    }
}

/**
 * Deletes a transport and manages related UI updates.
 * @param {number} transportDateId - ID of the transport date
 * @param {number} passengerId - ID of the passenger
 * @param {number} previousDriverId - ID of the previous driver
 * @param {jQuery} actualSelect - The select element being modified
 */
async function deleteTransportOption(transportDateId, passengerId, previousDriverId, actualSelect) {
    const data = {
        d: transportDateId,
        p: previousDriverId, // En delete, el driver actual es el que se va a eliminar
        t: passengerId
    };

    const elements = getTransportElements(transportDateId, previousDriverId, passengerId);

    try {
        const response = await $.ajax({
            type: 'DELETE',
            contentType: 'application/json',
            url: '/t',
            data: JSON.stringify(data),
            dataType: 'json'
        });

        deletePassengerInDriverTransportsTable(transportDateId, passengerId, response.data.p, elements.driverPassengersDivId);
        actualSelect.children().attr("name", "c");
        actualSelect.children(":first").attr("name", "d");
        await Promise.all([
            updatePassengerWarningIcon(data, elements.passengerWarningIcon),
            updateDriverWarningIcon(data, elements.newDriverWarningIcon)
        ]);
    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al desasignar el conductor.");
    }
}

/**
 * Adds a passenger to the driver's transports table td.
 * @param {number} transportDateId - ID of the transport date
 * @param {number} driverId - ID of the driver
 * @param {number} passengerId - ID of the passenger
 * @param {string} passengerFullName - Full name of the passenger
 * @param {jQuery} driverTransportsTablePassengerTdToAddSpan - Table cell to add passenger span
 */
export function addPassengerInDriverTransportsTable(transportDateId, driverId, passengerId, passengerFullName, driverTransportsTablePassengerTdToAddSpan) {
    //TODO when UUIDs are implemented, change the id and remove the p, d, t...
    const newPassengerSpanNode = document.createElement("span");
    const newPassengerSpanTextNode = document.createTextNode(passengerFullName);
    newPassengerSpanNode.appendChild(newPassengerSpanTextNode);
    newPassengerSpanNode.setAttribute('id', 'p' + passengerId + 'd' + driverId + 't' + transportDateId);
    newPassengerSpanNode.setAttribute('data-span-id', driverId);

    const newDivRowNode = document.createElement("div");
    newDivRowNode.setAttribute('class', 'row');
    newDivRowNode.setAttribute('id', 'driverPassengersOnDate_' + driverId + '_' + transportDateId + '_' + passengerId);
    newDivRowNode.append(newPassengerSpanNode);

    driverTransportsTablePassengerTdToAddSpan.append(newDivRowNode);
}

/**
 * Removes a passenger from the driver's transports table td.
 * @param {number} transportDateId - ID of the transport date
 * @param {number} passengerId - ID of the passenger
 * @param {number} oldDriverId - ID of the previous driver
 * @param {jQuery} driverPassengersDivId - Div containing the driver's passengers
 */
function deletePassengerInDriverTransportsTable(transportDateId, passengerId, oldDriverId, driverPassengersDivId) {
    //TODO when UUIDs are implemented, change the id and remove the p, d, t...
    const deleteId = 'p' + passengerId + 'd' + oldDriverId + 't' + transportDateId;

    const driverTransportsTablePassengerSpanToDelete = '#' + deleteId;
    $(driverTransportsTablePassengerSpanToDelete).remove();

    driverPassengersDivId.remove();
}

/**
 * Handles the change event of the driver selection.
 * @param {jQuery} actualSelect - The select element being modified
 * @param {number} previousDriverId - ID of the previous driver
 */
function operate(actualSelect, previousDriverId) {
    const selectedOption = $(actualSelect).find('option:selected');
    const newDriverId = $(actualSelect).val();
    const passengerId = selectedOption.attr('data-t');
    const transportDateId = selectedOption.attr('data-d');
    const passengerFullName = selectedOption.attr('data-passenger-name');
    const methodName = selectedOption.attr('name');

    if (!methodName) {
        temporalErrorAlert("Error: No se pudo determinar la operación a realizar.");
        return;
    }

    if ((!newDriverId || !passengerId || !transportDateId) && (!passengerId || !transportDateId && methodName === 'd')) {
        console.error('Missing required data:', { newDriverId, passengerId, transportDateId });
        temporalErrorAlert("Error: Faltan datos requeridos para la operación.");
        return;
    }

    const operations = {
        d: () => deleteTransportOption(transportDateId, passengerId, previousDriverId, $(actualSelect)),
        u: () => updateDriverInTransportOption(transportDateId, newDriverId, passengerId, passengerFullName, previousDriverId),
        c: () => createTransportOption(transportDateId, newDriverId, passengerId, passengerFullName, $(actualSelect))
    };

    const method = operations[methodName];
    if (typeof method === 'function') {
        method();
    } else {
        console.error(`Unknown operation: "${methodName}". Available: ${Object.keys(operations).join(', ')}`);
        temporalErrorAlert(`Error: Operación desconocida "${methodName}".`);
    }
}