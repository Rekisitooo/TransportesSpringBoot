package com.transports.spring.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FECHA_TRANSPORTE_POR_PLANTILLA")
public final class TransportDateByTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "COD_PLANTILLA")
    private int templateCode;

    @Column(name = "FECHA_TRANSPORTE")
    private Date transportDate;

    @Column(name = "COD_DIA_DE_LA_SEMANA")
    private int dayOfTheWeekCode;

    @Column(name = "NOMBRE_EVENTO")
    private String eventName;

    public TransportDateByTemplate(int id, int templateCode, Date transportDate, int dayOfTheWeekCode) {
        this.id = id;
        this.templateCode = templateCode;
        this.transportDate = transportDate;
        this.dayOfTheWeekCode = dayOfTheWeekCode;
    }

    public TransportDateByTemplate(int templateCode, Date transportDate, int dayOfTheWeekCode, String eventName) {
        this.templateCode = templateCode;
        this.transportDate = transportDate;
        this.dayOfTheWeekCode = dayOfTheWeekCode;
        this.eventName = eventName;
    }

    public TransportDateByTemplate(int templateCode, Date transportDate, int dayOfTheWeekCode) {
        this.id = null;
        this.templateCode = templateCode;
        this.transportDate = transportDate;
        this.dayOfTheWeekCode = dayOfTheWeekCode;
    }
}
