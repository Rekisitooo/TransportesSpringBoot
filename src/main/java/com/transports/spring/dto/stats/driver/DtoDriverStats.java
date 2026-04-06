package com.transports.spring.dto.stats.driver;

import com.transports.spring.model.Color;
import com.transports.spring.model.InvolvedByTemplate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DtoDriverStats {

    private InvolvedByTemplate involvedByTemplate;
    private Long totalTransports;
    private Integer transportPercentage;
    private Color color;

    public DtoDriverStats(final InvolvedByTemplate involvedByTemplate, final Color color, final Long totalTransports) {
        this.involvedByTemplate = involvedByTemplate;
        this.totalTransports = totalTransports;
        this.color = color;
    }

    public DtoDriverStats(final InvolvedByTemplate involvedByTemplate, final Color color) {
        this.involvedByTemplate = involvedByTemplate;
        this.color = color;
    }
}
