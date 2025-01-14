package com.transports.spring.service;

import com.transports.spring.model.Template;
import com.transports.spring.repository.IInvolvedByTemplateRepository;
import com.transports.spring.repository.ITemplateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class InvolvedByTemplateService {

    private final IInvolvedByTemplateRepository involvedByTemplateRepository;

    public InvolvedByTemplateService(final IInvolvedByTemplateRepository involvedByTemplateRepository) {
        this.involvedByTemplateRepository = involvedByTemplateRepository;
    }

    public void getAllPassengersFromTemplate(final int templateId) {
        this.involvedByTemplateRepository.getAllPassengersFromTemplate(templateId);
    }

    public void getAllDriversFromTemplate(int templateId) {
        this.involvedByTemplateRepository.getAllDriversFromTemplate(templateId);
    }
}
