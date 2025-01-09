package com.transports.spring.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "dia_transporte_semanal")
public final class WeeklyTransportDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "DESCRIPCION")
    private String description;

    @Column(name = "COD_DIA_DE_LA_SEMANA")
    private int dayOfTheWeek;

    @Column(name = "ACTIVO")
    private boolean isActive;

    @Column(name = "COD_USUARIO_PROPIETARIO")
    private int userCode;

    @Column(name = "COD_GRUPO_USUARIO")
    private int groupCode;

}
