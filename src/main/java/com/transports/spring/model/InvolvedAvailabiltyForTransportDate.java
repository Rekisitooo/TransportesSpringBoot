package com.transports.spring.model;

import com.transports.spring.model.key.InvolvedByTemplateKey;
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
    private int id;

    @Column(name = "COD_INVOLUCRADO")
    private int involvedCode;

    @Column(name = "COD_FECHA_TRANSPORTE")
    private int transportDateCode;

    public InvolvedAvailabiltyForTransportDate(int transportDateCode, int involvedCode) {
        this.transportDateCode = transportDateCode;
        this.involvedCode = involvedCode;
    }

}
