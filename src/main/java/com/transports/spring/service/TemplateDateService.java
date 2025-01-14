package com.transports.spring.service;

import com.transports.spring.model.Template;
import com.transports.spring.repository.ITemplateRepository;
import com.transports.spring.repository.ITemplateTransportDateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class TemplateDateService {

    private final ITemplateTransportDateRepository templateTransportDateRepository;

    public TemplateDateService(final ITemplateTransportDateRepository templateTransportDateRepository) {
        this.templateTransportDateRepository = templateTransportDateRepository;
    }

    public void findAllMonthDatesWithNameDayOfTheWeekByTemplateId(final int templateId) {
        return this.templateTransportDateRepository.findAllMonthDatesByTemplateId(templateId);
    }
}
