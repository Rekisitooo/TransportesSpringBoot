package com.transports.spring.controller;

import com.transports.spring.model.Template;
import com.transports.spring.model.TransportByTemplate;
import com.transports.spring.service.InvolvedByTemplateService;
import com.transports.spring.service.InvolvedByTransportService;
import com.transports.spring.service.TransportsByTemplateService;
import com.transports.spring.service.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/template")
public final class TemplateController {

    private final TemplateService templateService;
    private final InvolvedByTemplateService involvedByTemplateService;
    private final TransportsByTemplateService templateDateService;
    private final InvolvedByTransportService involvedByTransportService;

    public TemplateController(TemplateService templateService, InvolvedByTemplateService involvedByTemplateService, TransportsByTemplateService templateDaysService, InvolvedByTransportService involvedByTransportService) {
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
        final List<TransportByTemplate> templateDates = this.templateDateService.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        Map<date, Map<String(passengerName), String(driverName)>> map = this.involvedByTransportService.findAllPassengerTransports(templateId, templateDaysList);
        Map<date, Map<String(driverName), List<String(passengerNames)>>> map = this.involvedByTransportService.findAllDriverTransports(templateId, templateDaysList);
        return "templateCrud";
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