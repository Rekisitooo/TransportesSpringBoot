package com.transports.spring.service;

import com.transports.spring.dto.DtoAddNewDateForm;
import com.transports.spring.dto.DtoTransportDateByTemplate;
import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.repository.ITransportDateByTemplateRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.DayOfWeek;
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

    /**
     * adds a transport date in a template.
     * @param body
     * @param templateId
     * @return createdDateId
     */
    public int addTransportDate(final DtoAddNewDateForm body, final int templateId) {
        final Date addDateCardDateInput = body.getAddDateCardDateInput();
        final LocalDate localDate = addDateCardDateInput.toLocalDate();
        final DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        final String string = localDate.toString();
        final int ordinal = dayOfWeek.ordinal();

        this.transportDateByTemplateRepository.save(new TransportDateByTemplate(templateId, string, ordinal));
        return this.transportDateByTemplateRepository.findById();
    }
}
