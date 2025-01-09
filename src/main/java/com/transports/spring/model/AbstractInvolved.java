package com.transports.spring.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "INVOLUCRADO")
public class AbstractInvolved {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "NOMBRE")
    private String name;

    @Column(name = "APELLIDOS")
    private String surname;

    @Column(name = "ACTIVO")
    private boolean isActive;

    @Column(name = "COD_ROL")
    private int roleCode;

    @Column(name = "COD_USUARIO_PROPIETARIO")
    private int userCode;

    @Column(name = "COD_GRUPO_USUARIO")
    private Integer userCodeGroup;

}
