package com.transports.spring.controller;

import com.transports.spring.dto.*;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.model.*;
import com.transports.spring.service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/template")
public final class TemplateController {

    private static final String SUCCESS_ALERT_FLASH_ATTR_KEY = "success";
    private static final String ERROR_ALERT_FLASH_ATTR_KEY = "error";

    private final EventService eventService;
    private final TemplateService templateService;
    private final InvolvedByTemplateService involvedByTemplateService;
    private final TransportService transportService;
    private final TransportDateByTemplateService transportDateByTemplateService;
    private final InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService;
    private final TemplateFileService templateFileService;

    public TemplateController(EventService eventService, TemplateService templateService, InvolvedByTemplateService involvedByTemplateService, TransportService transportService, TransportDateByTemplateService transportDateByTemplateService, InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService, TemplateFileService generateTemplateFilesService, TemplateFileService templateFileService) {
        this.eventService = eventService;
        this.templateService = templateService;
        this.involvedByTemplateService = involvedByTemplateService;
        this.transportService = transportService;
        this.transportDateByTemplateService = transportDateByTemplateService;
        this.involvedAvailabiltyForTransportDateService = involvedAvailabiltyForTransportDateService;
        this.templateFileService = templateFileService;
    }

    @GetMapping("/openTemplate")
    public String getById(final Model model, @RequestParam (value = "id") final int templateId) {
        this.addDataToTemplateCrud(model, templateId);

        return "templateCRUD";
    }

    @GetMapping("/generate")
    public String generate(final RedirectAttributes rm, final Model model, @RequestParam(value = "id") final int templateId) {
        try {
            this.templateFileService.generateFiles(templateId);
        } catch (final IOException | GeneratePdfFromExcelException | GenerateJpgFromExcelException e) {
            rm.addFlashAttribute(ERROR_ALERT_FLASH_ATTR_KEY, "generateTemplatesError");
        }

        this.addDataToTemplateCrud(model, templateId);

        return "redirect:/templateCRUD";
    }

    @GetMapping("/create")
    public Template create(@RequestBody final Template template) {
        return this.templateService.create(template);
    }

    @GetMapping("/delete")
    public ResponseEntity<Template> delete(@PathVariable (value = "id") final int templateId) {
        return this.templateService.delete(templateId);
    }

    @PostMapping(path = "/newDate/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String newDate(final Model model, final RedirectAttributes rm, @PathVariable (value = "id") final int templateId, final DtoAddNewDateForm body) {
        //TODO check the date is not already added in the template
        //TODO check if the date is valid (from the current month and year)
        //TODO check body info
        //TODO check involvedd exist before the insert
        if (body.isAddDateCardIsTransportDateCheckboxInput()) {
            final TransportDateByTemplate newTemplateDate = this.transportDateByTemplateService.addTransportDate(body, templateId);
            this.involvedAvailabiltyForTransportDateService.addInvolvedAvailability(body, newTemplateDate.getId());
            rm.addFlashAttribute(SUCCESS_ALERT_FLASH_ATTR_KEY, "newDateAddedCorrectly");
        } else {
            this.eventService.addEvent(body, templateId);
            rm.addFlashAttribute(SUCCESS_ALERT_FLASH_ATTR_KEY, "eventAddedCorrectly");
        }

        this.addDataToTemplateCrud(model, templateId);

        return "redirect:/templateCRUD";
    }

    private void addDataToTemplateCrud(final Model model, final int templateId) {
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
    }
}