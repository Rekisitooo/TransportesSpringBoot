import { temporalErrorAlert } from './alert/GenericErrorAlert.js';

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

        if (passengerCommunicationResponse?.data?.length) {
            const previousDriverId = passengerCommunicationResponse.data[0].driverId;
            const hasChangedDriver = previousDriverId !== data.p;
            
            if (hasChangedDriver) {
                passengerWarningIcon.removeClass('d-none');
            } else {
                passengerWarningIcon.addClass('d-none');
            }
        } else {
            //si no hay comunicaciones, se muestra el icono para que se pueda indicar el aviso
            passengerWarningIcon.removeClass('d-none');
        }

    } catch (error) {
        passengerWarningIcon.removeClass('d-none');
    }
    
}

/**
 * Updates the warning icon visibility for a driver based on their communication status.
 * Shows the icon if the passenger is not in the driver's communications or if there are no communications.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} newDriverWarningIcon - The warning icon element for the driver
 */
async function updateDriverWarningIcon(data, newDriverWarningIcon) {
    try {
        const driverCommunicationResponse = 
        await $.ajax({
            type: 'GET',
            url: '/involvedCommunication/get',
            data: {
                involvedCommunicatedId: data.t,
                transportDateCode: data.d
            }
        });

        if (driverCommunicationResponse?.data?.length) {
            const hasPassengerInCommunications = driverCommunicationResponse.data.some(
                communication => communication.passengerId === data.t
            );
            
            if (hasPassengerInCommunications) {
                newDriverWarningIcon.addClass('d-none');
            } else {
                newDriverWarningIcon.removeClass('d-none');
            }
        } else {
            //si no hay comunicaciones, se muestra el icono para que se pueda indicar el aviso
            newDriverWarningIcon.removeClass('d-none');
        }

    } catch (error) {
        newDriverWarningIcon.removeClass('d-none');
    }
}

/**
 * Updates the driver assigned to a transport and manages related UI updates.
 * Handles the driver change, updates passenger lists, and manages warning icons.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {string} passengerFullName - Full name of the passenger
 * @param {jQuery} driverPassengersDivId - Div containing the driver's passengers
 * @param {jQuery} driverTransportsTablePassengerTdToAddSpan - Table cell to add passenger span
 * @param {jQuery} newDriverWarningIcon - The warning icon element for the driver
 * @param {jQuery} previousDriverWarningIcon - The warning icon element for the previous driver
 * @param {jQuery} passengerWarningIcon - The warning icon element for the passenger
 */
async function updateDriverInTransportOption(data, passengerFullName, driverPassengersDivId, driverTransportsTablePassengerTdToAddSpan, newDriverWarningIcon, previousDriverWarningIcon, passengerWarningIcon) {
    try {
        const response = 
            await $.ajax({
                type: 'PUT',
                contentType: 'application/json',
                url: '/t/updateDriver',
                data: JSON.stringify(data),
                dataType: 'json'
            });

            if (response?.status === 'ok') {
                addPassengerInDriverTransportsTable(data.d, data.p, data.t, passengerFullName, driverTransportsTablePassengerTdToAddSpan);
                deletePassengerInDriverTransportsTable(data.d, data.t, response.data.p, driverPassengersDivId);
                await updatePassengerWarningIcon(data, passengerWarningIcon);
                await updateDriverWarningIcon(data, newDriverWarningIcon);
                if (previousDriverWarningIcon != null) {
                    newDriverWarningIcon.removeClass('d-none');
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
 * Handles the creation of transport, updates passenger lists, and manages warning icons.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {string} passengerFullName - Full name of the passenger
 * @param {jQuery} actualSelect - The select element being modified
 * @param {jQuery} driverTransportsTablePassengerTdToAddSpan - Table cell to add passenger span
 * @param {jQuery} newDriverWarningIcon - The warning icon element for the driver
 * @param {jQuery} passengerWarningIcon - The warning icon element for the passenger
 */
async function createTransportOption(data, passengerFullName, actualSelect, driverTransportsTablePassengerTdToAddSpan, newDriverWarningIcon, passengerWarningIcon) {
    try {
        await $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url:'/t',
            data: JSON.stringify(data),
            dataType: 'json'
        });

        addPassengerInDriverTransportsTable(data.d, data.p, data.t, passengerFullName, driverTransportsTablePassengerTdToAddSpan);
        actualSelect.children().attr("name", "u");
        actualSelect.children(":first").attr("name", "d");
        await updatePassengerWarningIcon(data, passengerWarningIcon);
        await updateDriverWarningIcon(data, newDriverWarningIcon);

    } catch (error) {
        temporalErrorAlert("Ha ocurrido un error al asignar el conductor.");
    }
}

/**
 * Deletes a transport and manages related UI updates.
 * Handles the deletion of transport, updates passenger lists, and manages warning icons.
 * @param {Object} data - Contains passengerId (t), driverId (p), and transportDateId (d)
 * @param {jQuery} actualSelect - The select element being modified
 * @param {jQuery} driverPassengersDivId - Div containing the driver's passengers
 * @param {jQuery} previousDriverWarningIcon - The warning icon element for the driver
 * @param {jQuery} passengerWarningIcon - The warning icon element for the passenger
 */
async function deleteTransportOption(data, actualSelect, driverPassengersDivId, previousDriverWarningIcon, passengerWarningIcon) {
    try {
        const response = await $.ajax({
            type: 'DELETE',
            contentType: 'application/json',
            url:'/t',
            data: JSON.stringify(data),
            dataType: 'json'
        });

        deletePassengerInDriverTransportsTable(data.d, data.t, response.data.p, driverPassengersDivId);
        actualSelect.children().attr("name", "c");
        actualSelect.children(":first").attr("name", "d");
        await updatePassengerWarningIcon(data, passengerWarningIcon);
        await updateDriverWarningIcon(data, previousDriverWarningIcon);

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
 * Determines the operation to perform (create, update, or delete) based on the selected option.
 * Executes the corresponding operation with the appropriate parameters.
 * @param {jQuery} actualSelect - The select element being modified
 * @param {number} previousDriverId - ID of the previous driver
 */
function operate (actualSelect, previousDriverId) {
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

    const data = {
        d : transportDateId,
        p : newDriverId,
        t : passengerId
    }

    const driverPassengersDivId = $('div[id*=driverPassengersOnDate_' + newDriverId + '_' + transportDateId + ']');
    const driverTransportsTablePassengerTdToAddSpan = $('#driverPassengersForDateDiv_' + newDriverId + '_' + transportDateId);
    const newDriverWarningIcon = $(`#d${newDriverId}t${transportDateId}_date_td .fa-exclamation-circle`);
    const previousDriverWarningIcon = $(`#d${previousDriverId}t${transportDateId}_date_td .fa-exclamation-circle`);
    const passengerWarningIcon = $(`#p${passengerId}t${transportDateId}_date_td .fa-exclamation-circle`);

    const operations = {
        d: () => deleteTransportOption(data, $(actualSelect), driverPassengersDivId, previousDriverWarningIcon, passengerWarningIcon),
        u: () => updateDriverInTransportOption(data, passengerFullName, driverPassengersDivId, driverTransportsTablePassengerTdToAddSpan, newDriverWarningIcon, previousDriverWarningIcon, passengerWarningIcon),
        c: () => createTransportOption(data, passengerFullName, $(actualSelect), driverTransportsTablePassengerTdToAddSpan, newDriverWarningIcon, passengerWarningIcon)
    };

    const method = operations[methodName];
    if (typeof method === 'function') {
        method();
    } else {
        console.error(`Unknown operation: "${methodName}". Available: ${Object.keys(operations).join(', ')}`);
        temporalErrorAlert(`Error: Operación desconocida "${methodName}".`);
    }
}

$(function() {
    $('select[name="driverInTransportSelect"]').each(function() {
        $(this).attr('data-previous-driver-id', $(this).val());
    }).on('change', function (event) {
        const previousDriverId = $(this).data('data-previous-driver-id');
        operate(this, previousDriverId);
        $(this).data('data-previous-driver-id', $(this).val());
    });
});