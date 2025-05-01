package com.transports.spring.controller;

import com.transports.spring.dto.*;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.exception.InvolvedDoesNotExistException;
import com.transports.spring.exception.TransportsException;
import com.transports.spring.model.*;
import com.transports.spring.service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/template")
public final class TemplateController {

    public static final String GENERIC_ERROR_FLASH_ATTR = "genericError";
    private final AddNewDateToTemplateService addNewDateToTemplateService;
    private final TemplateService templateService;
    private final InvolvedByTemplateService involvedByTemplateService;
    private final TransportService transportService;
    private final TemplateDateService templateDateService;
    private final InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService;
    private final TemplateFileService templateFileService;
    private final CommunicationForInvolvedService communicationForInvolvedService;

    public TemplateController(AddNewDateToTemplateService addNewDateToTemplateService, TemplateService templateService, InvolvedByTemplateService involvedByTemplateService, TransportService transportService, TemplateDateService templateDateService, InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService, TemplateFileService templateFileService, CommunicationForInvolvedService communicationForInvolvedService) {
        this.addNewDateToTemplateService = addNewDateToTemplateService;
        this.templateService = templateService;
        this.involvedByTemplateService = involvedByTemplateService;
        this.transportService = transportService;
        this.templateDateService = templateDateService;
        this.involvedAvailabiltyForTransportDateService = involvedAvailabiltyForTransportDateService;
        this.templateFileService = templateFileService;
        this.communicationForInvolvedService = communicationForInvolvedService;
    }

    @GetMapping("/openTemplate")
    public String getById(final Model model, @RequestParam (value = "id") final int templateId) throws InvolvedDoesNotExistException {
        this.addDataToTemplateCrud(model, templateId);

        return "templateCRUD";
    }

    @GetMapping("/generate")
    public String generate(final RedirectAttributes rm, final Model model, @RequestParam(value = "id") final int templateId) throws InvolvedDoesNotExistException {
        try {
            this.templateFileService.generateFiles(templateId);
        } catch (final IOException | GeneratePdfFromExcelException | GenerateJpgFromExcelException e) {
            rm.addFlashAttribute(GENERIC_ERROR_FLASH_ATTR, GENERIC_ERROR_FLASH_ATTR);
        }

        this.addDataToTemplateCrud(model, templateId);

        return "redirect:/template/openTemplate?id=" + templateId;
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
    public String newDate(final Model model, final RedirectAttributes rm, @PathVariable (value = "id") final int templateId, final DtoAddNewDateForm body) throws InvolvedDoesNotExistException {
        try {
            this.addNewDateToTemplateService.newDate(templateId, body);
        } catch (final TransportsException e) {
            rm.addFlashAttribute(GENERIC_ERROR_FLASH_ATTR, GENERIC_ERROR_FLASH_ATTR);
        }

        this.addDataToTemplateCrud(model, templateId);
        return "redirect:/template/openTemplate?id=" + templateId;
    }

    private void addDataToTemplateCrud(final Model model, final int templateId) throws InvolvedDoesNotExistException {
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

        final List<DtoTemplateDate> templateDates = this.templateDateService.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        model.addAttribute("templateDates", templateDates);

        final Map<Integer, Map<Integer, Transport>> allPassengerTransportsFromTemplate = this.transportService.findAllPassengerTransportsFromTemplate(passengersFromTemplateList, templateId);
        model.addAttribute("allPassengerTransportsFromTemplate", allPassengerTransportsFromTemplate);

        final Map<Integer, Map<Integer, List<Passenger>>> allDriverTransportsFromTemplate = this.transportService.findAllDriverTransportsFromTemplate(driversFromTemplateList, templateId);
        model.addAttribute("allDriverTransportsFromTemplate", allDriverTransportsFromTemplate);

        final Map<Integer, List<Driver>> driversAvailableForDate = this.involvedAvailabiltyForTransportDateService.findAllDriversAvailableDatesForTemplate(templateId);
        model.addAttribute("driversAvailableForDate", driversAvailableForDate);

        final Map<Integer, List<Passenger>> passengersAvailableForDate = this.involvedAvailabiltyForTransportDateService.findAllPassengersAssistanceDatesForTemplate(templateId);
        model.addAttribute("passengersAvailableForDate", passengersAvailableForDate);

        final Map<Integer, Map<LocalDate, DtoTemplateDay>> passengerAssistanceDates = this.involvedAvailabiltyForTransportDateService.findAllPassengersAssistanceDates(templateId);
        model.addAttribute("passengersAssistanceDates", passengerAssistanceDates);

        final Map<Integer, Map<LocalDate, DtoTemplateDay>> driverAssistanceDates = this.involvedAvailabiltyForTransportDateService.findAllDriversAssistanceDates(templateId);
        model.addAttribute("driverAssistanceDates", driverAssistanceDates);

        final Map<Integer, Map<Integer, CommunicationForInvolved>> involvedCommunications = this.communicationForInvolvedService.getAllCommunicationsForTemplate(templateId);
        model.addAttribute("involvedCommunications", involvedCommunications);
    }
}