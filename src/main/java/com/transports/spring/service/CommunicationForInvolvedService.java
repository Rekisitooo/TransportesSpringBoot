package com.transports.spring.service;

import com.transports.spring.model.CommunicationForInvolved;
import com.transports.spring.repository.ICommunicationForInvolvedRepository;
import com.transports.spring.service.response.ServiceResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommunicationForInvolvedService {

    private final ICommunicationForInvolvedRepository communicationForInvolvedRepository;

    public CommunicationForInvolvedService(ICommunicationForInvolvedRepository communicationForInvolvedRepository) {
        this.communicationForInvolvedRepository = communicationForInvolvedRepository;
    }

    /**
     * Returns all the communications for a template.
     * @param templateId
     * @return Map<dateId, Map<InvolvedId, CommunicationForInvolved>>
     */
    public Map<Integer, Map<Integer, CommunicationForInvolved>> getAllCommunicationsForTemplate (final Integer templateId) {
        final Map<Integer, Map<Integer, CommunicationForInvolved>> commmunicationsMap = new HashMap<>();
        final List<CommunicationForInvolved> allCommunicationsForTemplate = this.communicationForInvolvedRepository.getAllCommunicationsForTemplate(templateId);

        for (final CommunicationForInvolved communicationForInvolved : allCommunicationsForTemplate) {
            final Map<Integer, CommunicationForInvolved> communicationForInvolvedMap = new HashMap<>();

            final Map<Integer, CommunicationForInvolved> communicationForInvolvedForDateMap = commmunicationsMap.get(communicationForInvolved.getTransportDateCode());
            if (communicationForInvolvedForDateMap == null) {
                communicationForInvolvedMap.put(communicationForInvolved.getInvolvedCommunicatedId(), communicationForInvolved);
                commmunicationsMap.put(communicationForInvolved.getTransportDateCode(), communicationForInvolvedMap);
            } else {
                communicationForInvolvedForDateMap.put(communicationForInvolved.getInvolvedCommunicatedId(), communicationForInvolved);
            }

        }

        return commmunicationsMap;
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

        List<Object[]> driverNotifications = communicationForInvolvedRepository.findDriverNotificationsByTemplate(templateId);
        processNotifications(driverNotifications, result);

        List<Object[]> passengerNotifications = communicationForInvolvedRepository.findPassengerNotificationsByTemplate(templateId);
        processNotifications(passengerNotifications, result);

        return result;
    }

    /**
     * Process the notifications and update the result map.
     * @param notifications List of notifications to process.
     * @param result Map to update with the processed notifications.
     */
    private void processNotifications(List<Object[]> notifications, Map<Integer, Map<Integer, Boolean>> result) {
        for (final Object[] row : notifications) {
            final Integer transportDateId = (Integer) row[0];
            final Integer involvedId = (Integer) row[1];
            final Boolean hasNotification = (Boolean) row[2];

            final Map<Integer, Boolean> involvedMap = result.computeIfAbsent(transportDateId, k -> new HashMap<>());
            involvedMap.put(involvedId, !hasNotification);
        }
    }
}
