package com.transports.spring.service.transportdatebytemplate;

import com.transports.spring.dto.DtoTemplateData;
import com.transports.spring.exception.transportdate.TransportDateCreationException;
import com.transports.spring.model.TransportDateByTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TransportDateCreationValidator {

    public TransportDateCreationValidator() {}

    public boolean validateInsertion(final DtoTemplateData template, final LocalDate localDate, final TransportDateByTemplate transportDate) throws TransportDateCreationException {
        final boolean dateForTemplateValid = this.isDateForTemplateValid(template, localDate);
        final boolean tranportDateAlreadyAddedToTemplate = this.isTranportDateAlreadyAddedToTemplate(transportDate);

        if (!dateForTemplateValid || !tranportDateAlreadyAddedToTemplate) {
            throw new TransportDateCreationException();
        }
        return true;
    }

    private boolean isDateForTemplateValid(final DtoTemplateData template, final  LocalDate localDate) {
        boolean isDateForTemplateValid = true;
        final String yearString = template.getYear();
        final int year = Integer.parseInt(yearString);
        final String monthString = template.getMonth();
        final int month = Integer.parseInt(monthString);
        if (localDate.getYear() != year || localDate.getMonthValue() != month) {
            isDateForTemplateValid = false;
        }

        return isDateForTemplateValid;
    }

    private boolean isTranportDateAlreadyAddedToTemplate(final TransportDateByTemplate transportDate) {
        return (transportDate != null) && (transportDate.getId() != null);
    }
}
