package com.transports.spring.controller;

import com.transports.spring.model.Template;
import com.transports.spring.service.InvolvedByTemplateService;
import com.transports.spring.service.InvolvedByTransportService;
import com.transports.spring.service.TemplateDateService;
import com.transports.spring.service.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/template")
public final class TemplateController {

    private final TemplateService templateService;
    private final InvolvedByTemplateService involvedByTemplateService;
    private final TemplateDateService templateDateService;
    private final InvolvedByTransportService involvedByTransportService;

    public TemplateController(TemplateService templateService, InvolvedByTemplateService involvedByTemplateService, TemplateDateService templateDaysService, InvolvedByTransportService involvedByTransportService) {
        this.templateService = templateService;
        this.involvedByTemplateService = involvedByTemplateService;
        this.templateDateService = templateDaysService;
        this.involvedByTransportService = involvedByTransportService;
    }

    @GetMapping("/getById")
    public String getById(@PathVariable (value = "id") final int templateId) {
        final Template template = this.templateService.findById(templateId);
        this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        this.involvedByTemplateService.getAllDriversFromTemplate(templateId);
        this.templateDateService.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        Map<date, Map<String(passengerName), String(driverName)>> map = this.involvedByTransportService.findAllPassengerTransports(templateDaysList);
        Map<date, Map<String(driverName), List<String(passengerNames)>>> map = this.involvedByTransportService.findAllDriverTransports(templateDaysList);
        return "templateCrud";
    }

    @GetMapping("/create")
    public Template create(@RequestBody final Template template) {
        return this.templateService.create(template);
    }

    @GetMapping("/delete")
    public ResponseEntity<Template> delete(@PathVariable (value = "id") final int templateId) {
        return this.templateService.delete(templateId);
    }
}