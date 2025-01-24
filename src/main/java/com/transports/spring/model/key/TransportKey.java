package com.transports.spring.model.key;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public final class TransportKey implements Serializable {

    @Column(name = "COD_VIAJERO", nullable = false)
    private int passengerId;

    @Column(name = "COD_CONDUCTOR", nullable = false)
    private int driverId;

    @Column(name = "COD_FECHA_TRANSPORTE", nullable = false)
    private int transportDateId;

}
