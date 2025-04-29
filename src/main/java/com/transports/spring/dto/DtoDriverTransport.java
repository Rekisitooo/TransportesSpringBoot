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
        super.transportDate = dtoInvolvedTransport.getTransportDate();
        super.eventName = dtoInvolvedTransport.getEventName();
        this.passengerFullNames = dtoInvolvedTransport.getName();
    }

    public DtoDriverTransport(final String passengerFullName, final String eventName) {
        this.passengerFullNames = passengerFullName;
        this.eventName = eventName;
    }

    public void addPassengerName(final String passengerFullName) {
        this.passengerFullNames += ", \n" + passengerFullName;
    }
}
