package com.transports.spring.controller;

import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.service.TransportDateByTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/transportDate")
public final class TransportDateByTemplateController {

    private final TransportDateByTemplateService transportDateService;

    public TransportDateByTemplateController(TransportDateByTemplateService transportDateService) {
        this.transportDateService = transportDateService;
    }

    @DeleteMapping
    public ResponseEntity<TransportDateByTemplate> getById(final Model model, @RequestParam (value = "eventId") final int transportDateId) {
        return this.transportDateService.delete(transportDateId);
    }
}