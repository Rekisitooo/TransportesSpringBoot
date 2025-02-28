package com.transports.spring.service;

import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.repository.ITransportDateByTemplateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TransportDateByTemplateService {

    private final ITransportDateByTemplateRepository transportDateByTemplateRepository;

    public TransportDateByTemplateService(final ITransportDateByTemplateRepository transportDateByTemplateRepository) {
        this.transportDateByTemplateRepository = transportDateByTemplateRepository;
    }

    public List<TransportDateByTemplate> findAllMonthDatesWithNameDayOfTheWeekByTemplateId(final int templateId) {
        return this.transportDateByTemplateRepository.findAllMonthDatesByTemplateId(templateId);
    }

    public Map<LocalDate, TransportDateByTemplate> getTransportDateByDayMap(final int templateId) {
        final List<TransportDateByTemplate> monthTransportDates = this.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        final Map<LocalDate, TransportDateByTemplate> transportDateMap = new LinkedHashMap<>();

        for (final TransportDateByTemplate transportDateByTemplate : monthTransportDates) {
            final String transportDateString = transportDateByTemplate.getTransportDate();
            final LocalDate transportDate = LocalDate.parse(transportDateString);

            transportDateMap.put(transportDate, transportDateByTemplate);
        }
        return transportDateMap;
    }

    public TransportDateByTemplate findById(final int transportDateId) {
        return this.transportDateByTemplateRepository.findById(transportDateId).orElseThrow();
    }
}
