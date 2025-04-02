package com.transports.spring.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE")
public final class InvolvedAvailabiltyForTransportDate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "COD_INVOLUCRADO")
    private int involvedCode;

    @Column(name = "COD_FECHA_TRANSPORTE")
    private int transportDateCode;

    @Column(name = "NECESITA_TRANSPORTE")
    private int needsTransport = 1;

    public InvolvedAvailabiltyForTransportDate(int involvedCode, int transportDateCode) {
        this.transportDateCode = transportDateCode;
        this.involvedCode = involvedCode;
    }

    public InvolvedAvailabiltyForTransportDate(int involvedCode, int transportDateCode, int needsTransport) {
        this.involvedCode = involvedCode;
        this.transportDateCode = transportDateCode;
        this.needsTransport = needsTransport;
    }
}
