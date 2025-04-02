package com.transports.spring.dto.requestbody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoUpdateNeedForTransport {
    private Integer transportDateId;
    private Integer driverId;
    private Integer passengerNeedsTransport;
}
