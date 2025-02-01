package com.transports.spring.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class DtoPassengerTransport {
    private String transportDate;
    private String eventName;
    private String driverFullName;
}
