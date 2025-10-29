package com.transports.spring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.repository.IInvolvedTransportNotificationRepository;
import com.transports.spring.service.response.ServiceResponse;
import com.transports.spring.vo.completemodel.VoCompleteNotification;

import jakarta.transaction.Transactional;

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
     * @return  Map<InvolvedId, List<dateId>>
     */
    public Map<Integer, List<Integer>> getAllNotificationsForTemplate(Integer templateId) {
        Map<Integer, List<Integer>> result = new HashMap<>();

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
    private static void processNotifications(List<Object[]> notifications, Map<Integer, List<Integer>> result) {
        for (final Object[] row : notifications) {
            final Integer transportDateId = (Integer) row[0];
            final Integer involvedId = (Integer) row[1];

            List<Integer> transportDateList = result.get(involvedId);
            if (transportDateList != null) {
                transportDateList.add(transportDateId);
            } else {
                transportDateList = new ArrayList<>();
                transportDateList.add(transportDateId);
                result.put(involvedId, transportDateList);
            }
        }
    }

    /**
     * Returns a map with passenger transport assignments for a template.
     * @param templateId consulted template
     * @return Map<passengerId, Map<transportDateId, VoCompleteNotification>>
     */
    public Map<Integer, Map<Integer, VoCompleteNotification>> getPassengerNotificationsMapByTemplate(final String templateId) {
        final Map<Integer, Map<Integer, VoCompleteNotification>> passengerNotificationsMap = new HashMap<>();

        final List<VoCompleteNotification> allPassengerNotificationsForTemplate =
                this.notificationForInvolvedRepository.getAllPassengerNotificationsForTemplate(templateId);

        for (final VoCompleteNotification voCompleteNotification : allPassengerNotificationsForTemplate) {
            final Integer passengerId = voCompleteNotification.getPassenger().getId();
            final Integer transportDateId = voCompleteNotification.getTransportDateByTemplate().getId();

            // Get or create the transport map for this passenger
            Map<Integer, VoCompleteNotification> passengerTransports = passengerNotificationsMap.get(passengerId);
            if (passengerTransports == null) {
                passengerTransports = new HashMap<>();
            }

            // Add the transport assignment (transportDateId -> passengerNotification)
            passengerTransports.put(transportDateId, voCompleteNotification);
            passengerNotificationsMap.put(passengerId, passengerTransports);
        }

        return passengerNotificationsMap;
    }


    /**
     * Returns a map with driver transport assignments for a template.
     * @param templateId consulted template
     * @return Map<driverId, Map<transportDateId, List<VoCompleteNotification>>>
     */
    public Map<Integer, Map<Integer, List<VoCompleteNotification>>> getDriverNotificationsMapByTemplate(final String templateId) {
        final Map<Integer, Map<Integer, List<VoCompleteNotification>>> driverNotificationsMap = new HashMap<>();

        final List<VoCompleteNotification> allDriverNotificationsForTemplate = this.notificationForInvolvedRepository.getAllDriverNotificationsForTemplate(templateId);

        for (final VoCompleteNotification voCompleteNotification : allDriverNotificationsForTemplate) {
            final Integer driverId = voCompleteNotification.getDriver().getId();
            final Integer transportDateId = voCompleteNotification.getTransportDateByTemplate().getId();

            // Get or create the transport map for this driver
            Map<Integer, List<VoCompleteNotification>> driverNotifications = driverNotificationsMap.get(driverId);
            if (driverNotifications == null) {
                driverNotifications = new HashMap<>();
            }

            // Get or create the notification list for this transport date
            List<VoCompleteNotification> notificationList = driverNotifications.get(transportDateId);
            if (notificationList == null) {
                notificationList = new ArrayList<>();
            }

             // Add the driver notification to the notificationList list
            notificationList.add(voCompleteNotification);
            driverNotifications.put(transportDateId, notificationList);
            driverNotificationsMap.put(driverId, driverNotifications);
        }

        return driverNotificationsMap;
    }

     /**
     * Returns a map with driver transport assignments for a template.
     * @param templateId consulted template
     * @return Map<driverId, Map<transportDateId, List<passengerName>>>
     */
    public Map<Integer, Map<Integer, List<String>>> getDriverNotificationsMapWithNamesByTemplate(final String templateId) {
        final Map<Integer, Map<Integer, List<String>>> driverNotificationsMap = new HashMap<>();

        final List<VoCompleteNotification> allDriverNotificationsForTemplate = this.notificationForInvolvedRepository.getAllDriverNotificationsForTemplate(templateId);

        for (final VoCompleteNotification voCompleteNotification : allDriverNotificationsForTemplate) {
            final Integer driverId = voCompleteNotification.getDriver().getId();
            final Integer transportDateId = voCompleteNotification.getTransportDateByTemplate().getId();
            String passengerName = voCompleteNotification.getPassenger().getName() + " " + voCompleteNotification.getPassenger().getSurname();

            if (passengerName == null) {
                passengerName = "";
            }

            // Get or create the transport map for this driver
            Map<Integer, List<String>> driverTransports = driverNotificationsMap.get(driverId);
            if (driverTransports == null) {
                driverTransports = new HashMap<>();
            }

            // Get or create the passenger list for this transport date
            List<String> passengerList = driverTransports.get(transportDateId);
            if (passengerList == null) {
                passengerList = new ArrayList<>();
            }

            // Add the passenger name to the list if not already present
            if (!passengerList.contains(passengerName)) {
                passengerList.add(passengerName);
            }

            driverTransports.put(transportDateId, passengerList);
            driverNotificationsMap.put(driverId, driverTransports);
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
    public Map<Integer, List<Integer>> getAllInvolvedNotifications(final Integer templateId) {
        return this.getAllNotificationsForTemplate(templateId);
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
