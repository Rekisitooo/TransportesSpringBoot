package com.transports.spring.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoDriverTransport  extends DtoInvolvedTransport {
    private String passengerFullName;

    @Override
    public String getNamesToWriteInTransportDateCell() {
        return this.passengerFullName;
    }

    public void addPassengerName(final String passengerFullName) {
        this.passengerFullName += ", " + passengerFullName;
    }
}
