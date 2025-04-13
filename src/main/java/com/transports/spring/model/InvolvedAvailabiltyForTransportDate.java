package com.transports.spring.model;

import com.transports.spring.model.key.InvolvedAvailabilityForTransportDateKey;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE")
public final class InvolvedAvailabiltyForTransportDate {

    @EmbeddedId
    private InvolvedAvailabilityForTransportDateKey involvedAvailabilityForTransportDateKey;

    @Column(name = "NECESITA_TRANSPORTE")
    private int needsTransport = 1;

    public InvolvedAvailabiltyForTransportDate(int involvedCode, int transportDateCode) {
        this.involvedAvailabilityForTransportDateKey = new InvolvedAvailabilityForTransportDateKey(involvedCode, transportDateCode);
    }

    public InvolvedAvailabiltyForTransportDate(int involvedCode, int transportDateCode, int needsTransport) {
        this.involvedAvailabilityForTransportDateKey = new InvolvedAvailabilityForTransportDateKey(involvedCode, transportDateCode);
        this.needsTransport = needsTransport;
    }

    public int getInvolvedCode() {
        return this.involvedAvailabilityForTransportDateKey.getInvolvedCode();
    }

    public void setInvolvedCode(int involvedCode) {
        this.involvedAvailabilityForTransportDateKey.setInvolvedCode(involvedCode);
    }

    public int getTransportDateCode() {
        return this.involvedAvailabilityForTransportDateKey.getTransportDateCode();
    }

    public void setTransportDateCode(int transportDateCode) {
        this.involvedAvailabilityForTransportDateKey.setTransportDateCode(transportDateCode);
    }
}
