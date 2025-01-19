package com.transports.spring.service;

import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.repository.ITransportDateByTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportDateByTemplateService {

    private final ITransportDateByTemplateRepository transportDateByTemplateRepository;

    public TransportDateByTemplateService(final ITransportDateByTemplateRepository transportDateByTemplateRepository) {
        this.transportDateByTemplateRepository = transportDateByTemplateRepository;
    }

    public List<TransportDateByTemplate> findAllMonthDatesWithNameDayOfTheWeekByTemplateId(final int templateId) {
        return this.transportDateByTemplateRepository.findAllMonthDatesByTemplateId(templateId);
    }
}
