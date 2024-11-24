package com.transports.spring.controller.passenger_controller.dto;

import com.transports.spring.model.WeeklyTransportDay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DtoGetAllPassengers {
    private String name;
    private String surname;
    private int seatsRequider;
    private HashMap<String, Boolean> availableInWeeklyTransportDayMap;

    public DtoGetAllPassengers(String name, String surname, int seatsRequider, Map<String, Boolean> availableInWeeklyTransportDayMap) {
        this.name = name;
        this.surname = surname;
        this.seatsRequider = seatsRequider;
        this.availableInWeeklyTransportDayMap = new HashMap<>(availableInWeeklyTransportDayMap);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getSeatsRequider() {
        return seatsRequider;
    }

    public void setSeatsRequider(int seatsRequider) {
        this.seatsRequider = seatsRequider;
    }

    public Map<String, Boolean> getAvailableInWeeklyTransportDayMap() {
        return new HashMap<>(availableInWeeklyTransportDayMap);
    }

    public void setAvailableInWeeklyTransportDayMap(Map<String, Boolean> availableInWeeklyTransportDayMap) {
        this.availableInWeeklyTransportDayMap = new HashMap<>(availableInWeeklyTransportDayMap);
    }
}
