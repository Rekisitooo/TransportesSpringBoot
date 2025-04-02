package com.transports.spring.dto;

import com.transports.spring.model.AbstractInvolved;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoTemplateDay {
    private LocalDate date;
    private String eventName;
    private int needsTransport;
    private List<AbstractInvolved> involvedList;

    public DtoTemplateDay(LocalDate date, String eventName, int needsTransport) {
        this.date = date;
        this.eventName = eventName;
        this.needsTransport = needsTransport;
    }
}
