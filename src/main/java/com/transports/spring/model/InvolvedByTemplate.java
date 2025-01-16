package com.transports.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "INVOLUCRADO_POR_PLANTILLA")
public final class InvolvedByTemplate {

    @Column(name = "COD_INVOLUCRADO")
    private int involvedCode;

    @Column(name = "COD_PLANTILLA")
    private int templateCode;

    @Column(name = "PLAZAS")
    private int seats;

    @Column(name = "COD_ROL")
    private int roleCode;

}
