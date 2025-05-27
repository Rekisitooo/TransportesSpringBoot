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
    public ResponseEntity<Object> deleteCommunicationForDriver(final Integer involvedId, final Integer transportDateId) {
        this.communicationForInvolvedRepository.deleteCommunicationsForInvolvedInDate(involvedId, transportDateId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse<>("ok", null));
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
}
