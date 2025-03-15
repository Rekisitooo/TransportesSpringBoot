package com.transports.spring.service;

import com.transports.spring.dto.DtoAddNewDateForm;
import com.transports.spring.dto.DtoTemplateData;
import com.transports.spring.enumerationclasses.DateTypeEnum;
import com.transports.spring.exception.InvalidDataInputException;
import com.transports.spring.exception.TransportsException;
import com.transports.spring.model.TransportDateByTemplate;
import org.springframework.stereotype.Service;

@Service
public class AddNewDateToTemplateService {

    private final EventService eventService;
    private final TemplateService templateService;
    private final InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService;
    private final TransportDateByTemplateService transportDateByTemplateService;

    public AddNewDateToTemplateService(EventService eventService, TemplateService templateService, InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService, TransportDateByTemplateService transportDateByTemplateService) {
        this.eventService = eventService;
        this.templateService = templateService;
        this.involvedAvailabiltyForTransportDateService = involvedAvailabiltyForTransportDateService;
        this.transportDateByTemplateService = transportDateByTemplateService;
    }

    public DateTypeEnum newDate(final int templateId, final DtoAddNewDateForm body) throws TransportsException {
        DateTypeEnum dateType;
        final DtoTemplateData template = this.templateService.getTemplateDataById(templateId);
        if (body == null) {
            throw new InvalidDataInputException();
        }

        if (body.getAddDateCardIsTransportDateCheckboxInput() != null && body.getAddDateCardIsTransportDateCheckboxInput()) {
            final TransportDateByTemplate newTemplateDate = this.transportDateByTemplateService.addTransportDate(body, template);
            this.involvedAvailabiltyForTransportDateService.addInvolvedAvailabilityForDate(body, newTemplateDate.getId(), templateId);
            dateType = DateTypeEnum.TRANSPORT_DATE;
        } else {
            this.eventService.addEvent(body, templateId);
            dateType = DateTypeEnum.EVENT;
        }

        return dateType;
    }
}
