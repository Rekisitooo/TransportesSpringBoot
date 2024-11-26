package com.transports.spring.controller.passenger_controller.dto;

import java.util.ArrayList;
import java.util.List;

public final class DtoGetAllPassengersBuilder {
    private String name;
    private String surname;
    private int occupiedSeats;
    private boolean isActive;
    private boolean isShared;
    private final List<Boolean> availableInWeeklyTransportDayList;

    public DtoGetAllPassengersBuilder(){
        this.availableInWeeklyTransportDayList = new ArrayList<>();
    }

    public DtoGetAllPassengers build(){
        return new DtoGetAllPassengers(this.name, this.surname, this.occupiedSeats, this.isActive, this.isShared, this.availableInWeeklyTransportDayList);
    }
    public DtoGetAllPassengersBuilder active(boolean active) {
        this.isActive = active;
        return this;
    }

    public DtoGetAllPassengersBuilder shared(boolean shared) {
        this.isShared = shared;
        return this;
    }

    public DtoGetAllPassengersBuilder name(String name) {
        this.name = name;
        return this;
    }

    public DtoGetAllPassengersBuilder surname(String surname) {
        this.surname = surname;
        return this;
    }

    public DtoGetAllPassengersBuilder occupiedSeats(int occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
        return this;
    }

    public DtoGetAllPassengersBuilder availableInWeeklyTransportDay(boolean isAvailable) {
        this.availableInWeeklyTransportDayList.add(isAvailable);
        return this;
    }
}
