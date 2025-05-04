package com.transports.spring.dto;

import com.transports.spring.model.Transport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoGetPassengersForDriverByDate {
    private Transport transport;
    private String passengerFullName;
}
