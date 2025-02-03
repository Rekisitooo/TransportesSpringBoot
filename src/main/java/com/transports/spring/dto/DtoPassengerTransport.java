package com.transports.spring.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class DtoPassengerTransport extends DtoInvolvedTransport {
    private String driverFullName;

    @Override
    public String getNamesToWriteInTransportDateCell() {
        return this.driverFullName;
    }
}
