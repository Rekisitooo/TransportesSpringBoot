package com.transports.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoUpdateInvolvedTransportNotification {
    private Integer notifiedInvolvedId;
    private Integer transportDateCode;
    private Integer driverCode;
    private Integer passengerCode;
}
