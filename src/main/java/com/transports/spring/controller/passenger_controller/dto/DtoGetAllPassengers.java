package com.transports.spring.controller.passenger_controller.dto;

import java.util.ArrayList;
import java.util.List;

public final class DtoGetAllPassengers {
    private String name;
    private String surname;
    private int occupiedSeats;
    private boolean isActive;
    private boolean isShared;
    private List<Boolean> availableInWeeklyTransportDayList;

    public DtoGetAllPassengers(final String name, final String surname, final int occupiedSeats, final boolean isActive, final boolean isShared, final List<Boolean> availableInWeeklyTransportDayList) {
        this.name = name;
        this.surname = surname;
        this.occupiedSeats = occupiedSeats;
        this.isShared = isShared;
        this.isActive = isActive;
        this.availableInWeeklyTransportDayList = new ArrayList<>(availableInWeeklyTransportDayList);
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
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

    public int getOccupiedSeats() {
        return occupiedSeats;
    }

    public void setOcuppiedSeats(int occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }

    public List<Boolean> getAvailableInWeeklyTransportDayList() {
        return new ArrayList<>(availableInWeeklyTransportDayList);
    }

    public void setAvailableInWeeklyTransportDayMap(List<Boolean> availableInWeeklyTransportDayList) {
        this.availableInWeeklyTransportDayList = new ArrayList<>(availableInWeeklyTransportDayList);
    }
}
