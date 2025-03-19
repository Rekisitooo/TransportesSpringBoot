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

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class TransportDateByTemplateService {

    private final ITransportDateByTemplateRepository transportDateByTemplateRepository;
    private final TransportDateByTemplateCreator transportDateByTemplateCreator;

    public TransportDateByTemplateService(final ITransportDateByTemplateRepository transportDateByTemplateRepository, TransportDateByTemplateCreator transportDateByTemplateCreator) {
        this.transportDateByTemplateRepository = transportDateByTemplateRepository;
        this.transportDateByTemplateCreator = transportDateByTemplateCreator;
    }

    public List<DtoTransportDateByTemplate> findAllMonthDatesWithNameDayOfTheWeekByTemplateId(final int templateId) {
        final List<DtoTransportDateByTemplate> allMonthDatesByTemplateId = this.transportDateByTemplateRepository.findAllMonthDatesByTemplateId(templateId);
        allMonthDatesByTemplateId.sort(Comparator.comparing(DtoTransportDateByTemplate::getTransportDate));

        return allMonthDatesByTemplateId;
    }

    public Map<LocalDate, DtoTransportDateByTemplate> getTransportDateByDayMap(final int templateId) {
        final List<DtoTransportDateByTemplate> monthTransportDates = this.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        final Map<LocalDate, DtoTransportDateByTemplate> transportDateMap = new LinkedHashMap<>();

        for (final DtoTransportDateByTemplate dtoTransportDateByTemplate : monthTransportDates) {
            final Date transportDateDate = dtoTransportDateByTemplate.getTransportDate();
            final LocalDate transportDate = transportDateDate.toLocalDate();

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
