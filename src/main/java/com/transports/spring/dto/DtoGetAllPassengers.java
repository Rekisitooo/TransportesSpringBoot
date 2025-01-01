package com.transports.spring.dto;

import com.transports.spring.comparable.IPassengerDtoGetAllPassengersComparable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class DtoGetAllPassengers implements IPassengerDtoGetAllPassengersComparable {
    private int id;
    private String name;
    private String surname;
    private int occupiedSeats;
    private boolean isActive;
    private Boolean isShared;
    private boolean isSharedFieldToBeModified;
    private String ownerAlias;
    private Map<Integer, Integer> availableInWeeklyTransportDayMap;

    @Override
    public boolean isShared() {
        return this.isShared;
    }
}
