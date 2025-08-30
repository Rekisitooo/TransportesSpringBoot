package com.transports.spring.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "USUARIO")
public final class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "NOMBRE")
    private String name;

    @Column(name = "CONTRASENA")
    private String password;

    @Column(name = "COD_ROL")
    private int roleCode;

    @Column(name = "COD_GRUPO")
    private int groupCode;
}
