package com.transports.spring.controller;

import com.transports.spring.dto.requestbody.DtoUpdateNeedForTransport;
import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;
import com.transports.spring.service.InvolvedAvailabiltyForTransportDateService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/involvedAvailability")
public final class InvolvedAvailabiltyForTransportDateController {

    private final InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService;

    public InvolvedAvailabiltyForTransportDateController(InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService) {
        this.involvedAvailabiltyForTransportDateService = involvedAvailabiltyForTransportDateService;
    }

    @PatchMapping("/updateNeedForTransport/{passengerId}")
    public ResponseEntity<InvolvedAvailabiltyForTransportDate> updateInvolvedNeedForTransport(final Model model, @PathVariable final int passengerId, @RequestBody DtoUpdateNeedForTransport body) {
        return this.involvedAvailabiltyForTransportDateService.
                updateInvolvedNeedForTransport(body, passengerId);
    }
}