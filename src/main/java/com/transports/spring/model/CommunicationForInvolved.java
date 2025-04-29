package com.transports.spring.model;

import com.transports.spring.model.key.CommunicationForInvolvedKey;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "AVISO_POR_IVOLUCRADO")
public final class CommunicationForInvolved {

    @EmbeddedId
    private CommunicationForInvolvedKey key;

    @Column(name = "COD_CONDUCTOR")
    private Integer driverCode;

    @Column(name = "COD_PASAJERO")
    private Integer passengerCode;

    @Column(name = "FECHA_AVISO")
    private Timestamp communicationDate;


    public CommunicationForInvolved(Integer involvedCommunicated, Integer transportDateCode, Integer driverCode, Integer passengerCode, Timestamp communicationDate) {
        this.key = new CommunicationForInvolvedKey(involvedCommunicated, transportDateCode);
        this.driverCode = driverCode;
        this.passengerCode = passengerCode;
        this.communicationDate = communicationDate;
    }
}
