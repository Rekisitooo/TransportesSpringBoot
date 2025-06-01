package com.transports.spring.controller;

import com.transports.spring.model.CommunicationForInvolved;
import com.transports.spring.service.CommunicationForInvolvedService;
import com.transports.spring.service.response.ServiceResponse;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/get")
    public ResponseEntity<Object> getCommunicationForInvolved(@RequestParam String transportDateCode, @RequestParam String involvedCommunicatedId) {
        final List<CommunicationForInvolved> list = this.communicationForInvolvedService.getCommunicationForInvolvedInDate(transportDateCode, involvedCommunicatedId);
        return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse<>("ok", list));
    }

    @PatchMapping("/updateDriver")
    public ResponseEntity<Object> updateDriver(@RequestBody CommunicationForInvolved body) {
        return this.communicationForInvolvedService.updateDriver(body);
    }

    @PostMapping("/createCommunication")
    public ResponseEntity<Object> createDriverCommunication(@RequestBody CommunicationForInvolved body) {
        return this.communicationForInvolvedService.create(body);
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody CommunicationForInvolved body) {
        ResponseEntity<Object> re = ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse<>("ok", body));
        try {
            this.communicationForInvolvedService.deleteCommunicationForDriver(body.getInvolvedCommunicatedId(), body.getTransportDateCode());
        } catch (final Exception e) {
            re = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ServiceResponse<>("ok", body));
        }
        return re;
    }

    @GetMapping("/tab/communications/{templateId}")
    public ResponseEntity<Object> getAllCommunicationsForTemplate(@PathVariable("templateId") String templateId, Model model) {
        ResponseEntity<Object> re = null;
        try {
            List<CommunicationForInvolved> communicationList = this.communicationForInvolvedService.getAllCommunicationsForTemplate(templateId);
            re = ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse<>("ok", communicationList));
        } catch (final Exception e) {
            re = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ServiceResponse<>("ok", null));
        }
        return re;
    }
}