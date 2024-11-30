package com.transports.spring.controller.passenger_controller;

import com.transports.spring.controller.passenger_controller.dto.DtoGetAllPassengers;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class FromDtoGetAllPassengers {

    private List<DtoGetAllPassengers> dtoGetAllPassengersList;
}
