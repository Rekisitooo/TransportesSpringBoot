package com.transports.spring.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FECHA_TRANSPORTE_POR_PLANTILLA")
public final class TransportDateByTemplate {

    @Id
    private int id;

    @Column(name = "COD_PLANTILLA")
    private int templateCode;

    @Column(name = "FECHA_TRANSPORTE")
    private String transportDate;

    @Column(name = "COD_DIA_DE_LA_SEMANA")
    private int dayOfTheWeekCode;

    @Column(name = "NOMBRE_EVENTO")
    private String eventName;

    private String dayOfTheWeekName;

    public TransportDateByTemplate(int id, int templateCode, String transportDate, int dayOfTheWeekCode, String dayOfTheWeekName) {
        this.id = id;
        this.templateCode = templateCode;
        this.transportDate = transportDate;
        this.dayOfTheWeekCode = dayOfTheWeekCode;
        this.eventName = eventName;
        this.dayOfTheWeekName = dayOfTheWeekName;
    }
}
