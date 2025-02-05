package com.transports.spring.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class DtoPassengerTransport extends AbstractDtoInvolvedTransport {
    private String driverFullName;
}
