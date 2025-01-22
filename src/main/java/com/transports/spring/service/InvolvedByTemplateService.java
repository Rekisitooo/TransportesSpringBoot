package com.transports.spring.service;

import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.repository.IInvolvedByTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class InvolvedByTemplateService {

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

    public Passenger getByIdAndTemplate(final int involvedId, final int templateId){
        return this.involvedByTemplateRepository.getByIdAndTemplate(involvedId, templateId);
    }
}
