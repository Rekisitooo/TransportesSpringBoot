package com.transports.spring.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoDriverTransport  extends DtoInvolvedTransport {
    private List<String> passengerNamesList;

    @Override
    public String getNamesToWriteInTransportDateCell() {
        final StringBuilder sbPassengers = new StringBuilder();
        for (final String passengerName : this.passengerNamesList) {
            sbPassengers.append(passengerName);
            sbPassengers.append(",");
        }

        final String passengersString = sbPassengers.toString();
        final String passengersStringWithoutLastComma = passengersString.substring(0, passengersString.length() - 1);
        return passengersStringWithoutLastComma;
    }
}
