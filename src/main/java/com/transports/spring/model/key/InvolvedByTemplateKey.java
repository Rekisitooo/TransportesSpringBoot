package com.transports.spring.model.key;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public final class InvolvedByTemplateKey implements Serializable {

    @Column(name = "COD_INVOLUCRADO", nullable = false)
    private int involvedCode;

    @Column(name = "COD_PLANTILLA", nullable = false)
    private int templateCode;

}
