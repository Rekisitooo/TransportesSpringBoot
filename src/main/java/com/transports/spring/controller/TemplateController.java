package com.transports.spring.controller;

import com.transports.spring.model.*;
import com.transports.spring.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/template")
public final class TemplateController {

    private final TemplateService templateService;
    private final InvolvedByTemplateService involvedByTemplateService;
    private final TransportByTemplateService transportByTemplateService;
    private final TransportDateByTemplateService transportDateByTemplateService;
    private InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService;

    public TemplateController(TemplateService templateService, InvolvedByTemplateService involvedByTemplateService, TransportByTemplateService transportByTemplateService, TransportDateByTemplateService transportDateByTemplateService, InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService) {
        this.templateService = templateService;
        this.involvedByTemplateService = involvedByTemplateService;
        this.transportByTemplateService = transportByTemplateService;
        this.transportDateByTemplateService = transportDateByTemplateService;
        this.involvedAvailabiltyForTransportDateService = involvedAvailabiltyForTransportDateService;
    }

    @GetMapping("/getById")
    public String getById(final Model model, @RequestParam (value = "id") final int templateId) {
        final Template template = this.templateService.findById(templateId);
        final List<Passenger> passengersFromTemplateList = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        model.addAttribute("passengersFromTemplateList", passengersFromTemplateList);
        final List<Driver> driversFromTemplateList = this.involvedByTemplateService.getAllDriversFromTemplate(templateId);
        model.addAttribute("driversFromTemplateList", driversFromTemplateList);
        final List<TransportDateByTemplate> templateDates = this.transportDateByTemplateService.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        model.addAttribute("templateDates", templateDates);
        final Map<Integer, Map<Integer, Integer>> allPassengerTransportsFromTemplate = this.transportByTemplateService.findAllPassengerTransportsFromTemplate(passengersFromTemplateList, templateId);
        model.addAttribute("allPassengerTransportsFromTemplate", allPassengerTransportsFromTemplate);
        final Map<Integer, Map<Integer, List<Integer>>> allDriverTransportsFromTemplate = this.transportByTemplateService.findAllDriverTransportsFromTemplate(driversFromTemplateList, templateId);
        model.addAttribute("allDriverTransportsFromTemplate", allDriverTransportsFromTemplate);
        final Map<Integer, List<Integer>> driversAvailableForDate = this.involvedAvailabiltyForTransportDateService.findAllDriversAvailableForDate(templateId);
        model.addAttribute("driversAvailableForDate", driversAvailableForDate);
        final Map<Integer, List<Integer>> passengersAvailableForDate = this.involvedAvailabiltyForTransportDateService.findAllPassengersAvailableForDate(templateId);
        model.addAttribute("passengersAvailableForDate", passengersAvailableForDate);

        return "templateCRUD";
        /*
        MAPA AUSENCIAS <PASAGERO, MAPA<FECHA, BOOLEAN>>
                ADOLFO	{01/01/2025, TRUE; 05/01/2025, FALSE; }

        cojo el pasajero
        cojo el valor para la fecha
        si es true pinto "AUSENTE"


        MAPA DISPONIBILIDAD <PASAGERO, MAPA<FECHA, BOOLEAN>>
                cojo el pasajero
        cojo el valor para la fecha
        si es true pinto "NO ASISTE"


        MAPA TRANSPORTES <PASAGERO, MAPA<FECHA, CONDUCTOR>>
                cojo el pasajero
        cojo el valor para la fecha y si hay lo pinto
        */
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