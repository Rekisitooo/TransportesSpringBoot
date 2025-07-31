package com.transports.spring.controller;

import com.transports.spring.model.NotificationForInvolved;
import com.transports.spring.service.NotificationForInvolvedService;
import com.transports.spring.service.response.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/involvedNotification")
public class NotificationForInvolvedController {

    private final NotificationForInvolvedService notificationForInvolvedService;

    public NotificationForInvolvedController(NotificationForInvolvedService notificationForInvolvedService) {
        this.notificationForInvolvedService = notificationForInvolvedService;
    }

    public void viewNotificationTableForTemplate() {
        //this.notificationForInvolvedService
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getNotificationForInvolved(@RequestParam String transportDateCode, @RequestParam String involvedCommunicatedId) {
        final List<NotificationForInvolved> list = this.notificationForInvolvedService.getNotificationForInvolvedInDate(transportDateCode, involvedCommunicatedId);
        return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse<>("ok", list));
    }

    @PatchMapping("/updateDriver")
    public ResponseEntity<Object> updateDriver(@RequestBody NotificationForInvolved body) {
        return this.notificationForInvolvedService.updateDriver(body);
    }

    @PostMapping("/createNotification")
    public ResponseEntity<Object> createDriverNotification(@RequestBody NotificationForInvolved body){
        return this.notificationForInvolvedService.create(body);
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody NotificationForInvolved body) {
        ResponseEntity<Object> re = ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse<>("ok", body));
        try {
            this.notificationForInvolvedService.deleteNotificationForDriver(body.getInvolvedCommunicatedId(), body.getTransportDateCode());
        } catch (final Exception e) {
            re = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ServiceResponse<>("ok", body));
        }
        return re;
    }
}