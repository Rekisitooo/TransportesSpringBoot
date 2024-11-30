package com.transports.spring.controller.passenger_controller;

import com.transports.spring.controller.passenger_controller.dto.DtoGetAllPassengers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FormDtoGetAllPassengers {

    private List<DtoGetAllPassengers> passengersList;
}
