package com.transports.spring.service;

import com.transports.spring.dto.DtoTemplateData;
import com.transports.spring.model.Template;
import com.transports.spring.repository.ITemplateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Calendar;
import java.util.List;

@Service
public class TemplateService {

    private final ITemplateRepository templateRepository;

    public TemplateService(final ITemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public DtoTemplateData getTemplateDataById(final int templateId) {
        final Template template = this.findById(templateId);
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(template.getYear()), Integer.parseInt(template.getMonth()) - 1, 1);
        final int lastMonthDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return new DtoTemplateData(template, lastMonthDate);
    }

    public Template findById(final int templateId) {
        return this.templateRepository.findById(templateId).orElseThrow();
    }

    public List<Template> getAll() {
        return this.templateRepository.findAll();
    }

    @GetMapping("/getAllWithMonthNames")
    public List<Template> getAllWithMonthNames() {
        final List<Template> templateList = this.getAll();
        for (final Template template : templateList) {
            template.setMonth(template.getMonthName());
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
