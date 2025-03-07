package com.transports.spring.dto;

import com.transports.spring.model.Passenger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class DtoPassengerList {
    private List<Passenger> passengersFromTemplateList;
    private int totalPassengerSeats;
}
