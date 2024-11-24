package com.transports.spring.model;

import jakarta.persistence.*;

@Entity
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
