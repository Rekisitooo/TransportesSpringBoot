package com.transports.spring.service;

import com.transports.spring.model.Template;
import com.transports.spring.repository.ITemplateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class InvolvedByTransportService {

    private final ITemplateRepository templateRepository;

    public InvolvedByTransportService(final ITemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public Template findById(final int templateId) {
        return this.templateRepository.findById(templateId).orElseThrow();
    }

    public List<Template> getAll() {
        return this.templateRepository.findAll();
    }

    @GetMapping("/getAllWithMonthNames")
    public List<Template> getAllWithMonthNames() {
        final String[] months = {"", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        final List<Template> templateList = this.getAll();
        for (final Template template : templateList) {
            final int monthInt = Integer.parseInt(template.getMonth());
            final String month = months[monthInt];
            template.setMonth(month);
        }

        return templateList;
    }

    public Template create(final Template template) {
        return this.templateRepository.save(template);
    }

    public ResponseEntity<Template> delete(final int templateId) {
        final Template existingTemplate = this.findById(templateId);
        this.templateRepository.delete(existingTemplate);
        return ResponseEntity.ok().build();
    }
}
