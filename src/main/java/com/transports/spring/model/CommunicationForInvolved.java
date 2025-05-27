package com.transports.spring.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "AVISO_POR_INVOLUCRADO")
public final class CommunicationForInvolved {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "COD_INVOLUCRADO_AVISADO", nullable = false)
    private Integer involvedCommunicatedId;

    @Column(name = "COD_FECHA_TRANSPORTE", nullable = false)
    private Integer transportDateCode;

    @Column(name = "COD_CONDUCTOR")
    private Integer driverCode;

    @Column(name = "COD_PASAJERO")
    private Integer passengerCode;

    @Column(name = "FECHA_AVISO", nullable = false)
    private Timestamp communicationDate;
}
