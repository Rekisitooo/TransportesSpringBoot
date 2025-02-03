package com.transports.spring.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class DtoInvolvedTransport {
    protected String transportDate;
    protected String eventName;

    public abstract String getNamesToWriteInTransportDateCell();
}
