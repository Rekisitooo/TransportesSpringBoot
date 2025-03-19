package com.transports.spring.service.transportdatebytemplate;

import com.transports.spring.dto.DtoAddNewDateForm;
import com.transports.spring.dto.DtoTemplateData;
import com.transports.spring.exception.transportdate.TransportDateCreationException;
import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.repository.ITransportDateByTemplateRepository;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class TransportDateByTemplateCreator {

    private final TransportDateCreationValidator transportDateCreationValidator;
    private final ITransportDateByTemplateRepository transportDateByTemplateRepository;

    public TransportDateByTemplateCreator(TransportDateCreationValidator transportDateCreationValidator, final ITransportDateByTemplateRepository transportDateByTemplateRepository) {
        this.transportDateCreationValidator = transportDateCreationValidator;
        this.transportDateByTemplateRepository = transportDateByTemplateRepository;
    }

    /**
     * adds a transport date in a template.
     * @param body
     * @param template
     * @return createdDateId
     */
    public TransportDateByTemplate addTransportDate(final DtoAddNewDateForm body, final DtoTemplateData template) throws TransportDateCreationException {
        final Date addDateCardDateInput = body.getAddDateCardDateInput();
        final LocalDate localDate = addDateCardDateInput.toLocalDate();
        final DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        final java.util.Date utilDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        final java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        final int dayOfTheWeek = dayOfWeek.ordinal();
        final TransportDateByTemplate transportDate = this.transportDateByTemplateRepository.findByTransportDate(sqlDate);

        this.transportDateCreationValidator.validateInsertion(template, localDate, transportDate);
        return this.transportDateByTemplateRepository.save(new TransportDateByTemplate(template.getId(), sqlDate, dayOfTheWeek));
    }


}
