package com.transports.spring.controller;

import com.transports.spring.model.CommunicationForInvolved;
import com.transports.spring.service.CommunicationForInvolvedService;
import com.transports.spring.service.response.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/get")
    public ResponseEntity<Object> getCommunicationForInvolved(@RequestParam String transportDateCode, @RequestParam String involvedCommunicatedId) {
        List<CommunicationForInvolved> list = this.communicationForInvolvedService.getCommunicationForInvolvedInDate(Integer.parseInt(transportDateCode), Integer.parseInt(involvedCommunicatedId));
        return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse<>("ok", list));
        //return this.communicationForInvolvedService.getCommunicationForInvolvedInDate(transportDateCode, involvedCommunicatedId);
    }

    @PatchMapping("/updateDriver")
    public ResponseEntity<CommunicationForInvolved> updateDriver(@RequestBody CommunicationForInvolved body) {
        return this.communicationForInvolvedService.updateDriver(body);
    }

    @PostMapping("/createDriverCommunication")
    public ResponseEntity<CommunicationForInvolved> createDriverCommunication(@RequestBody CommunicationForInvolved body){
        return this.communicationForInvolvedService.create(body);
    }

    @DeleteMapping
    public ResponseEntity<CommunicationForInvolved> delete(@RequestBody CommunicationForInvolved body) {
        return this.communicationForInvolvedService.deleteCommunicationForDriver(body);
    }
}
