package com.transports.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DtoFormGetAllPassengers {
    private List<DtoGetAllPassengers> passengersList;
}
