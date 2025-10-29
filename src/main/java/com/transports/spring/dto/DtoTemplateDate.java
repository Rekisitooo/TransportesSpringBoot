package com.transports.spring.dto;
import java.sql.Date;

import com.transports.spring.model.TransportDateByTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public final class DtoTemplateDate {

    private final TransportDateByTemplate transportDateByTemplate;
    private String dayOfTheWeekName;
    private String dateType;

    public DtoTemplateDate(int id, int templateCode, Date transportDate, int dayOfTheWeekCode, String eventName, String dayOfTheWeekName, String dateType) {
        this.transportDateByTemplate = new TransportDateByTemplate(id, templateCode, transportDate, dayOfTheWeekCode, eventName);
        this.dayOfTheWeekName = dayOfTheWeekName;
        this.dateType = dateType;
    }

     public DtoTemplateDate() {
        this.transportDateByTemplate = new TransportDateByTemplate();
     }

    public DtoTemplateDate(final String eventName, final String dateType) {
        this.transportDateByTemplate = new TransportDateByTemplate();
        this.dateType = dateType;
    }

    public TransportDateByTemplate getTransportDateByTemplate() {
        return transportDateByTemplate;
    }

    public void setTransportDateByTemplate(TransportDateByTemplate transportDateByTemplate) {
        this.transportDateByTemplate.setId(transportDateByTemplate.getId());
        this.transportDateByTemplate.setTemplateCode(transportDateByTemplate.getTemplateCode());
        this.transportDateByTemplate.setTransportDate(transportDateByTemplate.getTransportDate());
        this.transportDateByTemplate.setDayOfTheWeekCode(transportDateByTemplate.getDayOfTheWeekCode());
        this.transportDateByTemplate.setEventName(transportDateByTemplate.getEventName());
    }
    
    public void setId(int id) {
        this.transportDateByTemplate.setId(id);
    }

    public int getId() {
        return this.transportDateByTemplate.getId();
    }

    public int getTemplateCode() {
        return this.transportDateByTemplate.getTemplateCode();
    }

    public void setTemplateCode(int templateCode) {
        this.transportDateByTemplate.setTemplateCode(templateCode);
    }

    public Date getTransportDate() {
        return this.transportDateByTemplate.getTransportDate();
    }

    public void setTransportDate(Date transportDate) {
        this.transportDateByTemplate.setTransportDate(transportDate);
    }

    public int getDayOfTheWeekCode() {
        return this.transportDateByTemplate.getDayOfTheWeekCode();
    }

    public void setDayOfTheWeekCode(int dayOfTheWeekCode) {
        this.transportDateByTemplate.setDayOfTheWeekCode(dayOfTheWeekCode);
    }

    public String getEventName() {
        return this.transportDateByTemplate.getEventName();
    }

    public void setEventName(String eventName) {
        this.transportDateByTemplate.setEventName(eventName);
    }
}
