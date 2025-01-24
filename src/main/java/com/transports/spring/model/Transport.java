package com.transports.spring.model;

import com.transports.spring.model.key.TransportKey;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TRANSPORTE")
public final class Transport {

    @EmbeddedId
    private TransportKey transportKey;

    public Transport(int passengerId, int driverId, int transportDateId) {
        final TransportKey.TransportKeyBuilder builder = TransportKey.builder();
        this.transportKey = builder.passengerId(passengerId).driverId(driverId).transportDateId(transportDateId).build();
    }

}
