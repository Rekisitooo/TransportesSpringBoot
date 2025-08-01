package com.transports.spring.controller;

import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.service.InvolvedTransportNotificationService;
import com.transports.spring.service.response.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/involvedTransportNotification")
public class InvolvedTransportNotificationController {

    private final InvolvedTransportNotificationService involvedTransportNotificationService;

    public InvolvedTransportNotificationController(InvolvedTransportNotificationService involvedTransportNotificationService) {
        this.involvedTransportNotificationService = involvedTransportNotificationService;
    }

    public void viewNotificationTableForTemplate() {
        //this.involvedTransportNotificationService
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getNotificationForInvolved(@RequestParam String transportDateCode, @RequestParam String notifiedInvolvedId) {
        final List<InvolvedTransportNotification> list = this.involvedTransportNotificationService.getNotificationForInvolvedInDate(transportDateCode, notifiedInvolvedId);
        return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse<>("ok", list));
    }

    @PatchMapping("/updateDriver")
    public ResponseEntity<Object> updateDriver(@RequestBody InvolvedTransportNotification body) {
        return this.involvedTransportNotificationService.updateDriver(body);
    }

    @PostMapping("/createNotification")
    public ResponseEntity<Object> createDriverNotification(@RequestBody InvolvedTransportNotification body){
        return this.involvedTransportNotificationService.create(body);
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody InvolvedTransportNotification body) {
        ResponseEntity<Object> re = ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse<>("ok", body));
        try {
            this.involvedTransportNotificationService.deleteNotificationForDriver(body.getNotifiedInvolvedId(), body.getTransportDateCode());
        } catch (final Exception e) {
            re = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ServiceResponse<>("ok", body));
        }
        return re;
    }
}