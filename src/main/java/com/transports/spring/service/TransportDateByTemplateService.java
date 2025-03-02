package com.transports.spring.service;

import com.transports.spring.dto.DtoTransportDateByTemplate;
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

    public List<DtoTransportDateByTemplate> findAllMonthDatesWithNameDayOfTheWeekByTemplateId(final int templateId) {
        return this.transportDateByTemplateRepository.findAllMonthDatesByTemplateId(templateId);
    }

    public Map<LocalDate, DtoTransportDateByTemplate> getTransportDateByDayMap(final int templateId) {
        final List<DtoTransportDateByTemplate> monthTransportDates = this.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        final Map<LocalDate, DtoTransportDateByTemplate> transportDateMap = new LinkedHashMap<>();

        for (final DtoTransportDateByTemplate dtoTransportDateByTemplate : monthTransportDates) {
            final String transportDateString = dtoTransportDateByTemplate.getTransportDate();
            final LocalDate transportDate = LocalDate.parse(transportDateString);

            transportDateMap.put(transportDate, dtoTransportDateByTemplate);
        }
        return transportDateMap;
    }

    public TransportDateByTemplate findById(final int transportDateId) {
        return this.transportDateByTemplateRepository.findById(transportDateId).orElseThrow();
    }
}
