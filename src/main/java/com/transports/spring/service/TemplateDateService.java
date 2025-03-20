package com.transports.spring.service;

import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.repository.ITemplateDateRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TemplateDateService {

    private final ITemplateDateRepository templateDateRepository;

    public TemplateDateService(final ITemplateDateRepository templateDateRepository) {
        this.templateDateRepository = templateDateRepository;
    }

    public List<DtoTemplateDate> findAllMonthDatesWithNameDayOfTheWeekByTemplateId(final int templateId) {
        final List<DtoTemplateDate> allMonthDatesByTemplateId = this.templateDateRepository.findAllMonthDatesByTemplateId(templateId);
        allMonthDatesByTemplateId.sort(Comparator.comparing(DtoTemplateDate::getTransportDate));

        return allMonthDatesByTemplateId;
    }
}
