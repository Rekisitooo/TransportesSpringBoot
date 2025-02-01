package com.transports.spring.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class DtoDriverTransport {
    private String transportDate;
    private String eventName;
    private List<String> passengers;
}
