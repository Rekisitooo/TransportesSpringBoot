package com.transports.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoTransport {
    /**
     * passengerId
     */
    private int t;

    /**
     * driverId
     */
    private int p;

    /**
     * transportDateId
     */
    private int d;

    public int getTransportDateId() {
        return d;
    }

    public int getDriverId() {
        return p;
    }

    public int getPassengerId() {
        return t;
    }
}
