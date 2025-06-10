package com.transports.spring.service;

import com.transports.spring.model.CommunicationForInvolved;
import com.transports.spring.repository.ICommunicationForInvolvedRepository;
import com.transports.spring.service.response.ServiceResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommunicationForInvolvedService {

    private final ICommunicationForInvolvedRepository communicationForInvolvedRepository;

    public CommunicationForInvolvedService(ICommunicationForInvolvedRepository communicationForInvolvedRepository) {
        this.communicationForInvolvedRepository = communicationForInvolvedRepository;
    }

    public List<CommunicationForInvolved> getCommunicationForInvolvedInDate (final String transportDate, final String involvedId) {
         return this.communicationForInvolvedRepository.getCommunicationForInvolvedInDate(transportDate, involvedId);
    }

    @Transactional
    public void deleteCommunicationForDriver(final Integer involvedId, final Integer transportDateId) {
        this.communicationForInvolvedRepository.deleteCommunicationsForInvolvedInDate(involvedId, transportDateId);
    }

    @Transactional
    public ResponseEntity<Object> create(final CommunicationForInvolved communicationForInvolved) {
        this.communicationForInvolvedRepository.save(communicationForInvolved);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse<>("ok", communicationForInvolved));
    }

    @Transactional
    public ResponseEntity<Object> updateDriver(final CommunicationForInvolved communicationForInvolved) {
        this.communicationForInvolvedRepository.updateDriver(
                communicationForInvolved.getTransportDateCode(),
                communicationForInvolved.getInvolvedCommunicatedId(),
                communicationForInvolved.getDriverCode(),
                communicationForInvolved.getPassengerCode()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse<>("ok", communicationForInvolved));
    }

    /**
     * Returns a map with the notifications for the template.
     * @param templateId - The template id.
     * @return  Map<dateId, Map<InvolvedId, Boolean (true if notification is sent)>>
     */
    public Map<Integer, Map<Integer, Boolean>> getAllNotificationsForTemplate(Integer templateId) {
        Map<Integer, Map<Integer, Boolean>> result = new HashMap<>();

        List<Object[]> driverNotifications = this.communicationForInvolvedRepository.getDriverCommunicationsByTemplate(templateId);
        processNotifications(driverNotifications, result);

        List<Object[]> passengerNotifications = this.communicationForInvolvedRepository.getPassengerCommunicationsByTemplate(templateId);
        processNotifications(passengerNotifications, result);

        return result;
    }

    /**
     * Process the notifications and update the result map.
     * @param notifications List of notifications to process.
     * @param result Map to update with the processed notifications.
     */
    private static void processNotifications(List<Object[]> notifications, Map<Integer, Map<Integer, Boolean>> result) {
        for (final Object[] row : notifications) {
            final Integer transportDateId = (Integer) row[0];
            final Integer involvedId = (Integer) row[1];
            final Boolean hasNotification = (Boolean) row[2];

            final Map<Integer, Boolean> involvedMap = result.computeIfAbsent(transportDateId, k -> new HashMap<>());
            involvedMap.put(involvedId, !hasNotification);
        }
    }

    /**
     * Returns a map with passenger transport assignments for a template.
     * @param templateId consulted template
     * @return Map<passengerId, Map<transportDateId, driverName>>
     */
    public Map<Integer, Map<Integer, String>> getPassengerCommunicationsMapByTemplate(final String templateId) {
        final Map<Integer, Map<Integer, String>> passengerCommunicationsMap = new HashMap<>();

        final List<Object[]> allPassengerCommunicationsForTemplate =
                this.communicationForInvolvedRepository.getAllPassengerCommunicationsForTemplate(templateId);

        for (final Object[] row : allPassengerCommunicationsForTemplate) {
            final Integer passengerId = (Integer) row[0]; // involvedCommunicatedId
            final Integer transportDateId = (Integer) row[1]; // transportDateCode
            String driverName = (String) row[2]; // driver name + surname

            if (driverName == null) {
                driverName = "";
            }

            // Get or create the transport map for this passenger
            Map<Integer, String> passengerTransports = passengerCommunicationsMap.get(passengerId);
            if (passengerTransports == null) {
                passengerTransports = new HashMap<>();
                passengerCommunicationsMap.put(passengerId, passengerTransports);
            }

            // Add the transport assignment (transportDateId -> driverName)
            passengerTransports.put(transportDateId, driverName);
        }

        return passengerCommunicationsMap;
    }


    /**
     * Returns a map with driver transport assignments for a template.
     * @param templateId consulted template
     * @return Map<driverId, Map<transportDateId, List<passengerName>>>
     */
    public Map<Integer, Map<Integer, List<String>>> getDriverCommunicationsMapByTemplate(final String templateId) {
        final Map<Integer, Map<Integer, List<String>>> driverCommunicationsMap = new HashMap<>();

        final List<Object[]> allDriverCommunicationsForTemplate =
                this.communicationForInvolvedRepository.getAllDriverCommunicationsForTemplate(templateId);

        for (final Object[] row : allDriverCommunicationsForTemplate) {
            final Integer driverId = (Integer) row[0]; // involvedCommunicatedId
            final Integer transportDateId = (Integer) row[1]; // transportDateCode
            String passengerName = (String) row[2]; // passenger name + surname

            if (passengerName == null) {
                passengerName = "";
            }

            // Get or create the transport map for this driver
            Map<Integer, List<String>> driverTransports = driverCommunicationsMap.get(driverId);
            if (driverTransports == null) {
                driverTransports = new HashMap<>();
                driverCommunicationsMap.put(driverId, driverTransports);
            }

            // Get or create the passenger list for this transport date
            List<String> passengerList = driverTransports.get(transportDateId);
            if (passengerList == null) {
                passengerList = new ArrayList<>();
                driverTransports.put(transportDateId, passengerList);
            }

            // Add the passenger name to the list if not already present
            if (!passengerList.contains(passengerName)) {
                passengerList.add(passengerName);
            }
        }

        return driverCommunicationsMap;
    }
}
