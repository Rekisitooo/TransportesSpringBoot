package com.transports.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "EVENTO")
public final class Event {

    @Id
    private int id;

    @Column(name = "COD_PLANTILLA")
    private int templateCode;

    @Column(name = "FECHA")
    private Date date;

    @Column(name = "NOMBRE_EVENTO")
    private String eventName;

}
