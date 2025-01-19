package com.transports.spring.model;

import com.transports.spring.model.key.InvolvedByTemplateKey;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "INVOLUCRADO_POR_PLANTILLA")
public final class InvolvedByTemplate {

    @EmbeddedId
    private InvolvedByTemplateKey involvedByTemplateKey;

    @Column(name = "PLAZAS")
    private int seats;

    @Column(name = "COD_ROL")
    private int roleCode;

}
