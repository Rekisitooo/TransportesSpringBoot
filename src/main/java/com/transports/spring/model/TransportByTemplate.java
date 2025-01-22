package com.transports.spring.model;

import com.transports.spring.model.key.TransportByTemplateKey;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TRANSPORTE_POR_PLANTILLA")
public final class TransportByTemplate {

    @EmbeddedId
    private TransportByTemplateKey transportByTemplateKey;

    public TransportByTemplate(int passengerId, int driverId, int transportDateId) {
        final TransportByTemplateKey.TransportByTemplateKeyBuilder builder = TransportByTemplateKey.builder();
        this.transportByTemplateKey = builder.passengerId(passengerId).driverId(driverId).transportDateId(transportDateId).build();
    }

}
