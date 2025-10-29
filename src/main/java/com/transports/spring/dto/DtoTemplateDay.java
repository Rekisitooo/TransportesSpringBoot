package com.transports.spring.dto;

import java.time.LocalDate;
import java.util.List;

import com.transports.spring.model.Involved;
import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoTemplateDay {
    private LocalDate date;
    private String eventName;
    private int needsTransport;
    private List<Involved> involvedList;
    private InvolvedAvailabiltyForTransportDate involvedAvailabilityForTransportDate;

    public DtoTemplateDay(LocalDate date, String eventName, int needsTransport, InvolvedAvailabiltyForTransportDate involvedAvailabilityForTransportDate) {
        this.date = date;
        this.eventName = eventName;
        this.needsTransport = needsTransport;
        this.involvedAvailabilityForTransportDate = involvedAvailabilityForTransportDate;
    }

    public DtoTemplateDay(String eventName) {
        this.eventName = eventName;
    }

    public DtoTemplateDay(int needsTransport) {
        this.needsTransport = needsTransport;
    }
}
