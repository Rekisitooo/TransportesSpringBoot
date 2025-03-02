package com.transports.spring.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class DtoTransportDateByTemplate {

    private int id;
    private int templateCode;
    private String transportDate;
    private int dayOfTheWeekCode;
    private String eventName;
    private String dayOfTheWeekName;

    public DtoTransportDateByTemplate(int id, int templateCode, String transportDate, int dayOfTheWeekCode, String name) {
        this.id = id;
        this.templateCode = templateCode;
        this.transportDate = transportDate;
        this.dayOfTheWeekCode = dayOfTheWeekCode;
        this.dayOfTheWeekName = name;
    }
}
