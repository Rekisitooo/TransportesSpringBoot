package com.transports.spring.service;

import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.repository.IInvolvedTransportNotificationRepository;
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
public class InvolvedTransportNotificationService {

    private final IInvolvedTransportNotificationRepository notificationForInvolvedRepository;

    public InvolvedTransportNotificationService(IInvolvedTransportNotificationRepository notificationForInvolvedRepository) {
        this.notificationForInvolvedRepository = notificationForInvolvedRepository;
    }

    public List<InvolvedTransportNotification> getNotificationForInvolvedInDate (final String transportDate, final String involvedId) {
         return this.notificationForInvolvedRepository.getNotificationForInvolvedInDate(transportDate, involvedId);
    }

    @Transactional
    public void deleteNotificationForDriver(final Integer involvedId, final Integer transportDateId) {
        this.notificationForInvolvedRepository.deleteNotificationsForInvolvedInDate(involvedId, transportDateId);
    }

    @Transactional
    public ResponseEntity<Object> create(final InvolvedTransportNotification involvedTransportNotification) {
        this.notificationForInvolvedRepository.save(involvedTransportNotification);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse<>("ok", involvedTransportNotification));
    }

    @Transactional
    public ResponseEntity<Object> updateDriver(final InvolvedTransportNotification involvedTransportNotification) {
        this.notificationForInvolvedRepository.updateDriver(
                involvedTransportNotification.getTransportDateCode(),
                involvedTransportNotification.getNotifiedInvolvedId(),
                involvedTransportNotification.getDriverCode(),
                involvedTransportNotification.getPassengerCode()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse<>("ok", involvedTransportNotification));
    }

    /**
     * Returns a map with the notifications for the template.
     * @param templateId - The template id.
     * @return  Map<InvolvedId, Map<dateId, Boolean (true if notification is sent)>>
     */
    public Map<Integer, Map<Integer, Boolean>> getAllNotificationsForTemplate(Integer templateId) {
        Map<Integer, Map<Integer, Boolean>> result = new HashMap<>();

        List<Object[]> driverNotifications = this.notificationForInvolvedRepository.getDriverNotificationsByTemplate(templateId);
        processNotifications(driverNotifications, result);

        List<Object[]> passengerNotifications = this.notificationForInvolvedRepository.getPassengerNotificationsByTemplate(templateId);
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

            final Map<Integer, Boolean> involvedMap = result.computeIfAbsent(involvedId, k -> new HashMap<>());
            involvedMap.put(transportDateId, hasNotification);
        }
    }

    /**
     * Returns a map with passenger transport assignments for a template.
     * @param templateId consulted template
     * @return Map<passengerId, Map<transportDateId, driverName>>
     */
    public Map<Integer, Map<Integer, String>> getPassengerNotificationsMapByTemplate(final String templateId) {
        final Map<Integer, Map<Integer, String>> passengerNotificationsMap = new HashMap<>();

        final List<Object[]> allPassengerNotificationsForTemplate =
                this.notificationForInvolvedRepository.getAllPassengerNotificationsForTemplate(templateId);

        for (final Object[] row : allPassengerNotificationsForTemplate) {
            final Integer passengerId = (Integer) row[0]; // notifiedInvolvedId
            final Integer transportDateId = (Integer) row[1]; // transportDateCode
            String driverName = (String) row[2]; // driver name + surname

            if (driverName == null) {
                driverName = "";
            }

            // Get or create the transport map for this passenger
            Map<Integer, String> passengerTransports = passengerNotificationsMap.get(passengerId);
            if (passengerTransports == null) {
                passengerTransports = new HashMap<>();
                passengerNotificationsMap.put(passengerId, passengerTransports);
            }

            // Add the transport assignment (transportDateId -> driverName)
            passengerTransports.put(transportDateId, driverName);
        }

        return passengerNotificationsMap;
    }


    /**
     * Returns a map with driver transport assignments for a template.
     * @param templateId consulted template
     * @return Map<driverId, Map<transportDateId, List<passengerName>>>
     */
    public Map<Integer, Map<Integer, List<String>>> getDriverNotificationsMapByTemplate(final String templateId) {
        final Map<Integer, Map<Integer, List<String>>> driverNotificationsMap = new HashMap<>();

        final List<Object[]> allDriverNotificationsForTemplate =
                this.notificationForInvolvedRepository.getAllDriverNotificationsForTemplate(templateId);

        for (final Object[] row : allDriverNotificationsForTemplate) {
            final Integer driverId = (Integer) row[0]; // notifiedInvolvedId
            final Integer transportDateId = (Integer) row[1]; // transportDateCode
            String passengerName = (String) row[2]; // passenger name + surname

            if (passengerName == null) {
                passengerName = "";
            }

            // Get or create the transport map for this driver
            Map<Integer, List<String>> driverTransports = driverNotificationsMap.get(driverId);
            if (driverTransports == null) {
                driverTransports = new HashMap<>();
                driverNotificationsMap.put(driverId, driverTransports);
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

        return driverNotificationsMap;
    }

    /**
     * Returns a map that indicates whether the icon to mark all of the transports as notified
     * should show or not.
     * If it has two or more transports without notification, it shows.
     *
     * @param templateId - The template id.
     * @return  Map<InvolvedId, Boolean (true if button has to appear)>>
     */
    public Map<Integer, Boolean> getInvolvedMarkAllNotificationsButton(final Integer templateId) {
        final Map<Integer, Map<Integer, Boolean>> allNotificationsForTemplate = this.getAllNotificationsForTemplate(templateId);
        final Map<Integer, Boolean> allNotificationButtonMap = new HashMap<>();

        // each involved
        for (final Map.Entry<Integer, Map<Integer, Boolean>> integerMapEntry : allNotificationsForTemplate.entrySet()) {
            Integer involvedId = integerMapEntry.getKey();
            Map<Integer, Boolean> involvedNoticationsMap = integerMapEntry.getValue();

            // each notification for date
            int notNotifiedTransportsCounter = 0;
            for (final Map.Entry<Integer, Boolean> integerBooleanEntry : involvedNoticationsMap.entrySet()) {
                if (Boolean.FALSE.equals(integerBooleanEntry.getValue())) {
                    notNotifiedTransportsCounter++;
                }
            }

            // it there is more than 1 notification, mark true to show the button
            allNotificationButtonMap.put(involvedId, (notNotifiedTransportsCounter > 1) );
        }

        return allNotificationButtonMap;
    }

    /**
     * Gets all the passenger notifications that are not transports anymore
     * @param templateId
     * @param passengerId
     * @return list of notifications
     */
    public List<InvolvedTransportNotification> getPassengerNotificationsWithoutTransport(final Integer templateId, final Integer passengerId) {
        return this.notificationForInvolvedRepository.getPassengerNotificationsWithoutTransport(templateId, passengerId);
    }

    /**
     * Gets all the driver notifications that are not transports anymore
     * @param templateId
     * @param driverId
     * @return list of transports
     */
    public List<InvolvedTransportNotification> getDriverNotificationsWithoutTransport(final Integer templateId, final Integer driverId) {
        return this.notificationForInvolvedRepository.getDriverNotificationsWithoutTransport(templateId, driverId);
    }
}
