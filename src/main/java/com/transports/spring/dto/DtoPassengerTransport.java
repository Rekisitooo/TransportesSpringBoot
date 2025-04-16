package com.transports.spring.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class DtoPassengerTransport extends AbstractDtoInvolvedTransport {
    private String driverFullName;

    public DtoPassengerTransport(final DtoInvolvedTransport dtoInvolvedTransport) {
        super.transportDate = dtoInvolvedTransport.getTransportDate();
        super.eventName = dtoInvolvedTransport.getEventName();
        this.driverFullName = dtoInvolvedTransport.getName();
    }

    public DtoPassengerTransport(final String eventName, final String driverFullName) {
        super.eventName = eventName;
        this.driverFullName = driverFullName;
    }
}
