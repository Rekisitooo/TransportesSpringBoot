package com.transports.spring.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class DtoTransport {
    private String driver;
    private int driverId;
    private String passenger;
    private int passengerId;
    private String date;
    private int transportId;
}
