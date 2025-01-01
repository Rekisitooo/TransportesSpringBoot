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

    /*
        Pbkdf2PasswordEncoder encoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String result = encoder.encode("myPassword");
        assertTrue(encoder.matches("myPassword", result));
     */
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
