package com.transports.spring.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoDriverTransport extends AbstractDtoInvolvedTransport {
    private String passengerFullNames;

    public DtoDriverTransport(final DtoInvolvedTransport dtoInvolvedTransport) {
        this.transportDate = dtoInvolvedTransport.getTransportDate();
        this.eventName = dtoInvolvedTransport.getEventName();
        this.passengerFullNames = dtoInvolvedTransport.getName();
    }

    public void addPassengerName(final String passengerFullName) {
        this.passengerFullNames += ", " + passengerFullName;
    }
}
