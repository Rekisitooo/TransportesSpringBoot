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
public final class TemplateTransportDate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "COD_PLANTILLA")
    private int templateCode;

    @Column(name = "FECHA_TRANSPORTE")
    private LocalDate transportDate;

    @Column(name = "PLAZAS")
    private int dayOfTheWeek;

}
