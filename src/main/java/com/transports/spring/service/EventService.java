package com.transports.spring.service;

import com.transports.spring.dto.DtoAddNewDateForm;
import com.transports.spring.model.Event;
import com.transports.spring.repository.IEventRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventService {

    private final IEventRepository eventRepository;

    public EventService(final IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Map<LocalDate, Event> findAllEventsByTemplateId(final int templateId) {
        final Map<LocalDate, Event> eventMap = new LinkedHashMap<>();

        final List<Event> allEventsByTemplateId = this.eventRepository.findAllEventsByTemplateId(templateId);
        for (final Event event : allEventsByTemplateId) {
            final Date date = event.getDate();
            eventMap.put(date.toLocalDate(), event);
        }

        return eventMap;
    }

    public void addEvent(final DtoAddNewDateForm dtoAddNewDateForm, final int templateId) {
        final Date addDateCardDateInput = dtoAddNewDateForm.getAddDateCardDateInput();
        final String addDateCardEventNameInput = dtoAddNewDateForm.getAddDateCardEventNameInput();

        this.eventRepository.save(new Event(templateId, addDateCardDateInput, addDateCardEventNameInput));
    }
}
