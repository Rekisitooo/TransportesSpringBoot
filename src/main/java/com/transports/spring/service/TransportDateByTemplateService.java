package com.transports.spring.service;

import com.transports.spring.dto.DtoAddNewDateForm;
import com.transports.spring.dto.DtoTemplateData;
import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.exception.transportdate.TransportDateCreationException;
import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.repository.ITransportDateByTemplateRepository;
import com.transports.spring.service.transportdatebytemplate.TransportDateByTemplateCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransportDateByTemplateService {

    private final ITransportDateByTemplateRepository transportDateByTemplateRepository;
    private final TemplateDateService templateDateService;
    private final TransportDateByTemplateCreator transportDateByTemplateCreator;

    public TransportDateByTemplateService(final ITransportDateByTemplateRepository transportDateByTemplateRepository, TemplateDateService templateDateService, TransportDateByTemplateCreator transportDateByTemplateCreator) {
        this.transportDateByTemplateRepository = transportDateByTemplateRepository;
        this.templateDateService = templateDateService;
        this.transportDateByTemplateCreator = transportDateByTemplateCreator;
    }

    public Map<LocalDate, DtoTemplateDate> getTransportDateByDayMap(final int templateId) {
        final List<DtoTemplateDate> monthTransportDates = this.templateDateService.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        final Map<LocalDate, DtoTemplateDate> transportDateMap = new LinkedHashMap<>();

        for (final DtoTemplateDate dtoTemplateDate : monthTransportDates) {
            final Date transportDateDate = dtoTemplateDate.getTransportDate();
            final LocalDate transportDate = transportDateDate.toLocalDate();

            transportDateMap.put(transportDate, dtoTemplateDate);
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


    public ResponseEntity<TransportDateByTemplate> delete(final int transportDateId) {
        final TransportDateByTemplate existingTransportDate = this.transportDateByTemplateRepository.findById(transportDateId).orElseThrow();
        this.transportDateByTemplateRepository.delete(existingTransportDate);
        return ResponseEntity.ok().build();
    }
}
