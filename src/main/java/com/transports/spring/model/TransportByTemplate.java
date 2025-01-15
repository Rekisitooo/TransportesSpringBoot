package com.transports.spring.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FECHA_TRANSPORTE_POR_PLANTILLA")
public final class TransportByTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "COD_PLANTILLA")
    private int templateCode;

    @Column(name = "FECHA_TRANSPORTE")
    private LocalDate transportDate;

    @Column(name = "COD_DIA_DE_LA_SEMANA")
    private int dayOfTheWeek;

    private String dayOfTheWeekName;

}
