package com.transports.spring.service;

import com.transports.spring.dto.DtoUpdateCommunicationForInvolved;
import com.transports.spring.model.CommunicationForInvolved;
import com.transports.spring.repository.ICommunicationForInvolvedRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationForInvolvedService {

    private ICommunicationForInvolvedRepository communicationForInvolvedRepository;

    public List<CommunicationForInvolved> getAllCommunicationsForTemplate (final Integer templateId) {
        return this.communicationForInvolvedRepository.getAllCommunicationsForTemplate(templateId);
    }

    public CommunicationForInvolved getCommunicationForInvolvedInDate (final Integer transportDate, final Integer involvedId) {
        return this.communicationForInvolvedRepository.getCommunicationForInvolvedInDate(transportDate, involvedId);
    }

    public void delete(final CommunicationForInvolved communicationForInvolved) {
        this.communicationForInvolvedRepository.delete(communicationForInvolved);
    }

    public void create(final CommunicationForInvolved communicationForInvolved) {
        this.communicationForInvolvedRepository.save(communicationForInvolved);
    }

    public void updateDriver(final DtoUpdateCommunicationForInvolved dtoUpdateCommunication) {
        this.communicationForInvolvedRepository.updateDriver(
                dtoUpdateCommunication.getTransportDateCode(),
                dtoUpdateCommunication.getInvolvedCommunicated(),
                dtoUpdateCommunication.getDriverCode(),
                dtoUpdateCommunication.getPassengerCode()
        );
    }
}
