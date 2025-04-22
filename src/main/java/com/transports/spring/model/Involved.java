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
public class Involved {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

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

    public Involved(Integer id, String name, String surname, int roleCode, int userCode, Integer userCodeGroup) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.roleCode = roleCode;
        this.userCode = userCode;
        this.userCodeGroup = userCodeGroup;
    }

    protected Involved(int id, String completeName){
        super();
        this.id = id;
        this.name = completeName;
    }

    public String getFullName() {
        return this.getName() + " " + this.getSurname();
    }
}
