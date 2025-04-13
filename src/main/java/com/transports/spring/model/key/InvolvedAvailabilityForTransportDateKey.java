package com.transports.spring.model.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public final class InvolvedAvailabilityForTransportDateKey implements Serializable {

    @Column(name = "COD_INVOLUCRADO", nullable = false)
    private int involvedCode;

    @Column(name = "COD_FECHA_TRANSPORTE", nullable = false)
    private int transportDateCode;
}
