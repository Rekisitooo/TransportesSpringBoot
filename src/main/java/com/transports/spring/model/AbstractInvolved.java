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
    protected int id;

    @Column(name = "NOMBRE")
    protected String name;

    @Column(name = "APELLIDOS")
    protected String surname;

    @Column(name = "ACTIVO")
    protected boolean isActive;

    @Column(name = "COD_ROL")
    protected int roleCode;

    @Column(name = "COD_USUARIO_PROPIETARIO")
    protected int userCode;

    @Column(name = "COD_GRUPO_USUARIO")
    protected Integer userCodeGroup;

    protected AbstractInvolved(int id, String completeName){
        super();
        this.id = id;
        this.name = completeName;
    }
}
