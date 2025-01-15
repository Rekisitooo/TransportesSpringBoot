package com.transports.spring.service;

import com.transports.spring.model.Passenger;
import com.transports.spring.model.Template;
import com.transports.spring.repository.IInvolvedByTemplateRepository;
import com.transports.spring.repository.ITemplateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Driver;
import java.util.List;

@Service
public class InvolvedByTemplateService {

    private final IInvolvedByTemplateRepository involvedByTemplateRepository;

    public InvolvedByTemplateService(final IInvolvedByTemplateRepository involvedByTemplateRepository) {
        this.involvedByTemplateRepository = involvedByTemplateRepository;
    }

    public List<Passenger> getAllPassengersFromTemplate(final int templateId) {
        return this.involvedByTemplateRepository.getAllPassengersFromTemplate(templateId);
    }

    public List<Driver> getAllDriversFromTemplate(int templateId) {
        return this.involvedByTemplateRepository.getAllDriversFromTemplate(templateId);
    }
}
