package com.transports.spring.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoInvolvedTransport extends AbstractDtoInvolvedTransport {
    private String name;

    public DtoInvolvedTransport(String transportDate, String eventName, String name) {
        super(transportDate, eventName);
        this.name = name;
    }
}
