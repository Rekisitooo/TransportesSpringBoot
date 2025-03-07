package com.transports.spring.controller;

import com.transports.spring.dto.DtoDriverList;
import com.transports.spring.dto.DtoPassengerList;
import com.transports.spring.dto.DtoTemplateData;
import com.transports.spring.dto.DtoTransportDateByTemplate;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.Template;
import com.transports.spring.model.Transport;
import com.transports.spring.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Calendar;
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
        final DtoTemplateData template = this.templateService.getTemplateDataById(templateId);
        model.addAttribute("lastMonthDay", template.getLastMonthDay());
        model.addAttribute("monthNumber", template.getMonth());
        model.addAttribute("templateMonth", template.getMonthName().toUpperCase());
        model.addAttribute("templateYear", template.getYear());
        model.addAttribute("templateId", template.getId());

        final DtoPassengerList dtoPassengerList = this.involvedByTemplateService.getAllPassengersFromTemplateForTemplateView(templateId);
        final List<Passenger> passengersFromTemplateList = dtoPassengerList.getPassengersFromTemplateList();
        model.addAttribute("passengersFromTemplateList", passengersFromTemplateList);
        model.addAttribute("passengerSeatsAmount", dtoPassengerList.getTotalPassengerSeats());

        final DtoDriverList dtoDriverList = this.involvedByTemplateService.getAllDriversFromTemplateForTemplateView(templateId);
        final List<Driver> driversFromTemplateList = dtoDriverList.getDriversFromTemplateList();
        model.addAttribute("driversFromTemplateList", driversFromTemplateList);
        model.addAttribute("availableDriverSeatsAmount", dtoDriverList.getTotalDriverAvailableSeats());

        final List<DtoTransportDateByTemplate> templateDates = this.transportDateByTemplateService.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        model.addAttribute("templateDates", templateDates);

        final Map<Integer, Map<Integer, Transport>> allPassengerTransportsFromTemplate = this.transportService.findAllPassengerTransportsFromTemplate(passengersFromTemplateList, templateId);
        model.addAttribute("allPassengerTransportsFromTemplate", allPassengerTransportsFromTemplate);

        final Map<Integer, Map<Integer, List<Passenger>>> allDriverTransportsFromTemplate = this.transportService.findAllDriverTransportsFromTemplate(driversFromTemplateList, templateId);
        model.addAttribute("allDriverTransportsFromTemplate", allDriverTransportsFromTemplate);

        final Map<Integer, List<Driver>> driversAvailableForDate = this.involvedAvailabiltyForTransportDateService.findAllDriversAvailableDatesForTemplate(templateId);
        model.addAttribute("driversAvailableForDate", driversAvailableForDate);

        final Map<Integer, List<Passenger>> passengersAvailableForDate = this.involvedAvailabiltyForTransportDateService.findAllPassengersAssistanceDatesForTemplate(templateId);
        model.addAttribute("passengersAvailableForDate", passengersAvailableForDate);

        return "templateCRUD";
    }

    @GetMapping("/generate")
    public String generate(final Model model, @RequestParam(value = "id") final int templateId) {
        try {
            this.templateFileService.generateFiles(templateId);
        } catch (final IOException | GeneratePdfFromExcelException | GenerateJpgFromExcelException e) {
            //TODO print an error
        }

        return this.getById(model, 1);
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