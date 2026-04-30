package com.transports.spring.dto.stats.driver;

import com.transports.spring.model.Color;
import com.transports.spring.model.Driver;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DtoDriverStats {
    private Driver driver;
    private Long totalTransports;
    private int transportPercentage;
    private Color color;

    public DtoDriverStats(final Driver driver, final Color color, final Long totalTransports) {
        this.driver = driver;
        this.totalTransports = totalTransports;
        this.color = color;
        this.transportPercentage = 0;
    }

    public DtoDriverStats(final Driver driver, final Color color) {
        this.driver = driver;
        this.color = color;
        this.transportPercentage = 0;
    }
}
