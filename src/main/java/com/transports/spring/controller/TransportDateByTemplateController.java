package com.transports.spring.controller;

import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.service.TransportDateByTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transportDate")
public final class TransportDateByTemplateController {

    private final TransportDateByTemplateService transportDateService;

    public TransportDateByTemplateController(TransportDateByTemplateService transportDateService) {
        this.transportDateService = transportDateService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransportDateByTemplate> delete(final Model model, @PathVariable final int id) {
        //TODO disable date instead of erasing
        return this.transportDateService.delete(id);
    }
}