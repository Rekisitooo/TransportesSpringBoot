package com.transports.spring.service;

import com.transports.spring.dto.DtoTransport;
import com.transports.spring.model.TransportByTemplate;
import com.transports.spring.repository.ITransportsByTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportsByTemplateService {

    private final ITransportsByTemplateRepository templateTransportDateRepository;

    public TransportsByTemplateService(final ITransportsByTemplateRepository templateTransportDateRepository) {
        this.templateTransportDateRepository = templateTransportDateRepository;
    }

    public List<TransportByTemplate> findAllMonthDatesWithNameDayOfTheWeekByTemplateId(final int templateId) {
        return this.templateTransportDateRepository.findAllMonthDatesByTemplateId(templateId);
    }

    public List<DtoTransport> findAllTransportsByPassengerFromTemplate(final int passengerId, final int templateId) {
        return this.templateTransportDateRepository.findAllTransportsByPassengerFromTemplate(passengerId, templateId);
    }
}
