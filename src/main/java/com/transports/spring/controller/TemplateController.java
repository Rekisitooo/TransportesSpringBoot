package com.transports.spring.controller;

import com.transports.spring.model.*;
import com.transports.spring.operation.filesgeneration.TemplateFileGenerator;
import com.transports.spring.service.TemplateFileService;
import com.transports.spring.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/template")
public final class TemplateController {

    private final TemplateService templateService;
    private final InvolvedByTemplateService involvedByTemplateService;
    private final TransportService transportService;
    private final TransportDateByTemplateService transportDateByTemplateService;
    private final InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService;
    private final TemplateFileService templateFileService;

    public TemplateController(TemplateService templateService, InvolvedByTemplateService involvedByTemplateService, TransportService transportService, TransportDateByTemplateService transportDateByTemplateService, InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService, TemplateFileService generateTemplateFilesService, TemplateFileService templateFileService) {
        this.templateService = templateService;
        this.involvedByTemplateService = involvedByTemplateService;
        this.transportService = transportService;
        this.transportDateByTemplateService = transportDateByTemplateService;
        this.involvedAvailabiltyForTransportDateService = involvedAvailabiltyForTransportDateService;
        this.templateFileService = templateFileService;
    }

    @GetMapping("/getById")
    public String getById(final Model model, @RequestParam (value = "id") final int templateId) {
        final Template template = this.templateService.findById(templateId);
        model.addAttribute("templateMonth", template.getMonthName().toUpperCase());
        model.addAttribute("templateYear", template.getYear());
        model.addAttribute("templateId", template.getId());

        final List<Passenger> passengersFromTemplateList = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        model.addAttribute("passengersFromTemplateList", passengersFromTemplateList);

        final List<Driver> driversFromTemplateList = this.involvedByTemplateService.getAllDriversFromTemplate(templateId);
        model.addAttribute("driversFromTemplateList", driversFromTemplateList);

        final List<TransportDateByTemplate> templateDates = this.transportDateByTemplateService.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        model.addAttribute("templateDates", templateDates);

        final Map<Integer, Map<Integer, Transport>> allPassengerTransportsFromTemplate = this.transportService.findAllPassengerTransportsFromTemplate(passengersFromTemplateList, templateId);
        model.addAttribute("allPassengerTransportsFromTemplate", allPassengerTransportsFromTemplate);

        final Map<Integer, Map<Integer, List<Passenger>>> allDriverTransportsFromTemplate = this.transportService.findAllDriverTransportsFromTemplate(driversFromTemplateList, templateId);
        model.addAttribute("allDriverTransportsFromTemplate", allDriverTransportsFromTemplate);

        final Map<Integer, List<Driver>> driversAvailableForDate = this.involvedAvailabiltyForTransportDateService.findAllDriversAvailableForDate(templateId);
        model.addAttribute("driversAvailableForDate", driversAvailableForDate);

        final Map<Integer, List<Passenger>> passengersAvailableForDate = this.involvedAvailabiltyForTransportDateService.findAllPassengersAvailableForDate(templateId);
        model.addAttribute("passengersAvailableForDate", passengersAvailableForDate);

        return "templateCRUD";
    }

    @GetMapping("/generate")
    public void generate(@RequestParam (value = "id") final int templateId) {
        try {
            this.templateFileService.generateFiles(templateId);
        } catch (final IOException e) {
            //TODO print an error
        }
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