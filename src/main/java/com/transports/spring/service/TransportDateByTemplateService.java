package com.transports.spring.service;

import com.transports.spring.dto.DtoAddNewDateForm;
import com.transports.spring.dto.DtoTemplateData;
import com.transports.spring.dto.DtoTransportDateByTemplate;
import com.transports.spring.exception.transportdate.TransportDateCreationException;
import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.repository.ITransportDateByTemplateRepository;
import com.transports.spring.service.transportdatebytemplate.TransportDateByTemplateCreator;
import com.transports.spring.service.transportdatebytemplate.TransportDateCreationValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TransportDateByTemplateService {

    private final ITransportDateByTemplateRepository transportDateByTemplateRepository;
    private final TransportDateByTemplateCreator transportDateByTemplateCreator;

    public TransportDateByTemplateService(TransportDateCreationValidator transportDateCreationValidator, final ITransportDateByTemplateRepository transportDateByTemplateRepository, TransportDateByTemplateCreator transportDateByTemplateCreator) {
        this.transportDateByTemplateRepository = transportDateByTemplateRepository;
        this.transportDateByTemplateCreator = transportDateByTemplateCreator;
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
     * @param template
     * @return createdDateId
     */
    public TransportDateByTemplate addTransportDate(final DtoAddNewDateForm body, final DtoTemplateData template) throws TransportDateCreationException {
        return this.transportDateByTemplateCreator.addTransportDate(body, template);
    }


}
