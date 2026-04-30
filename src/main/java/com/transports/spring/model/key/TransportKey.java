package com.transports.spring.model.key;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public final class TransportKey implements Serializable {

    @Column(name = "COD_VIAJERO", nullable = false)
    private Integer passengerId;

    @Column(name = "COD_CONDUCTOR", nullable = false)
    private Integer driverId;
    @Column(name = "COD_FECHA_TRANSPORTE", nullable = false)
    private Integer transportDateId;

}
