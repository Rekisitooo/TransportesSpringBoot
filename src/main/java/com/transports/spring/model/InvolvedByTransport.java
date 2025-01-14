package com.transports.spring.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "INVOLUCRADO_POR_TRANSPORTE")
public final class InvolvedByTransport {

    @Column(name = "COD_TRANSPORTE")
    private int transportCode;

    @Column(name = "COD_INVOLUCRADO_POR_PLANTILLA")
    private int involvedByTemplateCode;

}
