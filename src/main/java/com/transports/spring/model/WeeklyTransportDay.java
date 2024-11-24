package com.transports.spring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dia_transporte_semanal")
public final class WeeklyTransportDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "DESCRIPCION")
    private String description;

    @Column(name = "COD_DIA_DE_LA_SEMANA")
    private int dayOfTheWeek;

    @Column(name = "ACTIVO")
    private boolean isActive;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
