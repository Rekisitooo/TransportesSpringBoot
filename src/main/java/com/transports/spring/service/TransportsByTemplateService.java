package com.transports.spring.service;

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

    public List<TransportByTemplate> findAllTransportsByInvolved(final int involvedId) {
        return this.templateTransportDateRepository.findAllTransportsByInvolved(involvedId);
    }
}
