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
public class DtoUpdateNotificationForInvolved {
    private Integer involvedNotified;
    private Integer transportDateCode;
    private Integer driverCode;
    private Integer passengerCode;
}
