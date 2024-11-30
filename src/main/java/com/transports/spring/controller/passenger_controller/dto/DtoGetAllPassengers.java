package com.transports.spring.controller.passenger_controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public final class DtoGetAllPassengers {
    private int id;
    private String name;
    private String surname;
    private int occupiedSeats;
    private boolean isActive;
    private boolean isShared;
    private Map<Integer, Integer> availableInWeeklyTransportDayMap;
}
