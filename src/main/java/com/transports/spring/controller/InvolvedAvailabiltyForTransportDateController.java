package com.transports.spring.controller;

import com.transports.spring.dto.requestbody.DtoUpdateInvolvedAvailability;
import com.transports.spring.dto.requestbody.DtoUpdateNeedForTransport;
import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;
import com.transports.spring.service.InvolvedAvailabiltyForTransportDateService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping
    public ResponseEntity<InvolvedAvailabiltyForTransportDate> deleteInvolvedAssistanceForDate(final Model model, @RequestBody DtoUpdateInvolvedAvailability body) {
        return this.involvedAvailabiltyForTransportDateService.deleteInvolvedAssistanceForDate(body.getInvolvedId(), body.getTransportDateId());
    }

    @PostMapping
    public ResponseEntity<InvolvedAvailabiltyForTransportDate> createDriverInTransport(@RequestBody DtoUpdateInvolvedAvailability body){
        return this.involvedAvailabiltyForTransportDateService.saveAvailability(body.getInvolvedId(), body.getTransportDateId());
    }
}