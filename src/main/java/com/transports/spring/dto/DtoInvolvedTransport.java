package com.transports.spring.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class DtoInvolvedTransport implements IDtoInvolvedTransport {
    protected String transportDate;
    protected String eventName;
}
