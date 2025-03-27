package com.transports.spring.controller;

import com.transports.spring.model.Event;
import com.transports.spring.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/event")
public final class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Event> delete(final Model model, @PathVariable final int id) {
        return this.eventService.delete(id);
    }
}