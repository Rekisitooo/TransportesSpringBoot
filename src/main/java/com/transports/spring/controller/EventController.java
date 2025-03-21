package com.transports.spring.controller;

import com.transports.spring.model.Event;
import com.transports.spring.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/event")
public final class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @DeleteMapping
    public ResponseEntity<Event> getById(final Model model, @RequestParam (value = "eventId") final int eventId) {
        return this.eventService.delete(eventId);
    }
}