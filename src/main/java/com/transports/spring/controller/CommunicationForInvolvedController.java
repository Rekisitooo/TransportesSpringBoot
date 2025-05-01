package com.transports.spring.controller;

import com.transports.spring.model.CommunicationForInvolved;
import com.transports.spring.model.key.CommunicationForInvolvedKey;
import com.transports.spring.service.CommunicationForInvolvedService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/involvedCommunication")
public class CommunicationForInvolvedController {

    private final CommunicationForInvolvedService communicationForInvolvedService;

    public CommunicationForInvolvedController(CommunicationForInvolvedService communicationForInvolvedService) {
        this.communicationForInvolvedService = communicationForInvolvedService;
    }

    public void viewCommunicationTableForTemplate() {
        //this.communicationForInvolvedService
    }

    @RequestMapping("/get")
    public List<CommunicationForInvolved> getCommunicationForInvolved(final Model model, @RequestBody CommunicationForInvolvedKey body) {
        return this.communicationForInvolvedService.getCommunicationForInvolvedInDate(body.getTransportDateCode(), body.getInvolvedCommunicatedId());
    }

    @PatchMapping("/updateDriver")
    public ResponseEntity<CommunicationForInvolved> updateDriver(final Model model, @RequestBody CommunicationForInvolved body) {
        return this.communicationForInvolvedService.updateDriver(body);
    }

    @PostMapping
    public ResponseEntity<CommunicationForInvolved> createDriverCommunication(@RequestBody CommunicationForInvolved body){
        return this.communicationForInvolvedService.create(body);
    }

    @DeleteMapping
    public ResponseEntity<CommunicationForInvolved> delete(final Model model, @RequestBody CommunicationForInvolved body) {
        return this.communicationForInvolvedService.deleteCommunicationForDriver(body);
    }
}
