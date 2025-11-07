package com.transports.spring.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "AVISO_POR_INVOLUCRADO")
public final class InvolvedTransportNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "COD_INVOLUCRADO_AVISADO", nullable = false)
    private Integer notifiedInvolvedId;

    @Column(name = "COD_FECHA_TRANSPORTE", nullable = false)
    private Integer transportDateCode;

    @Column(name = "COD_CONDUCTOR")
    private Integer driverCode;

    @Column(name = "COD_PASAJERO")
    private Integer passengerCode;

    @Column(name = "FECHA_AVISO", nullable = false)
    private Timestamp notificationDate;
}
