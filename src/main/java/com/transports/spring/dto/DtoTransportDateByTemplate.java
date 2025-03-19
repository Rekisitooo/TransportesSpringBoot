package com.transports.spring.dto;

import lombok.*;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class DtoTransportDateByTemplate {

    private int id;
    private int templateCode;
    private Date transportDate;
    private int dayOfTheWeekCode;
    private String eventName;
    private String dayOfTheWeekName;

    public DtoTransportDateByTemplate(int id, int templateCode, Date transportDate, int dayOfTheWeekCode, String name) {
        this.id = id;
        this.templateCode = templateCode;
        this.transportDate = transportDate;
        this.dayOfTheWeekCode = dayOfTheWeekCode;
        this.dayOfTheWeekName = name;
    }
}
