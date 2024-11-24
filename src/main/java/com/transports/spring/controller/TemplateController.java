package com.transports.spring.controller;

import com.transports.spring.model.Template;
import com.transports.spring.repository.ITemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/template")
public final class TemplateController {

    @Autowired
    private ITemplateRepository templateRepository;

    @GetMapping("/getById")
    public Template getById(@PathVariable (value = "id") final int templateId) {
        return this.templateRepository.findById(templateId).orElseThrow();
    }

    @GetMapping("/getAll")
    public List<Template> getAll() {
        return this.templateRepository.findAll();
    }

    @GetMapping("/getAllWithMonthNames")
    public List<Template> getAllWithMonthNames() {
        final String[] months = {"", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        final List<Template> templateList = this.templateRepository.findAll();
        for (final Template template : templateList) {
            final int monthInt = Integer.parseInt(template.getMonth());
            final String month = months[monthInt];
            template.setMonth(month);
        }

        return templateList;
    }

    @GetMapping("/create")
    public Template create(@RequestBody final Template template) {
        return this.templateRepository.save(template);
    }

    @GetMapping("/update")
    public Template update(@RequestBody final Template templateToUpdate, @PathVariable (value = "id") final int templateId) {
        final Template template = this.templateRepository.findById(templateId).orElseThrow();
        template.setMonth(templateToUpdate.getMonth());
        template.setYear(templateToUpdate.getYear());
        return this.templateRepository.save(template);
    }

    @GetMapping("/delete")
    public ResponseEntity<Template> delete(@PathVariable (value = "id") final int templateId) {
        final Template existingTemplate = this.templateRepository.findById(templateId).orElseThrow();
        this.templateRepository.delete(existingTemplate);
        return ResponseEntity.ok().build();
    }
}