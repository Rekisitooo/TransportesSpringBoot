package com.transports.spring.dto;

import lombok.*;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class DtoTemplateDate {

    private int id;
    private int templateCode;
    private Date transportDate;
    private int dayOfTheWeekCode;
    private String eventName;
    private String dayOfTheWeekName;
    private String dateType;

    public DtoTemplateDate(int id, int templateCode, Date transportDate, int dayOfTheWeekCode, String name, String dateType) {
        this.id = id;
        this.templateCode = templateCode;
        this.transportDate = transportDate;
        this.dayOfTheWeekCode = dayOfTheWeekCode;
        this.dayOfTheWeekName = name;
        this.dateType = dateType;
    }

}
