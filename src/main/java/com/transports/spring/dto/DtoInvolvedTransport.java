package com.transports.spring.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Date;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoInvolvedTransport extends AbstractDtoInvolvedTransport {
    private String name;

    public DtoInvolvedTransport(Date transportDate, String eventName, String name) {
        super(transportDate, eventName);
        this.name = name;
    }
}
