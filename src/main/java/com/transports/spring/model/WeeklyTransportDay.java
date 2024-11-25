package com.transports.spring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dia_transporte_semanal")
public final class WeeklyTransportDay {

    public WeeklyTransportDay(int id, String description, int dayOfTheWeek, boolean isActive, int userCode, int groupCode) {
        this.id = id;
        this.description = description;
        this.dayOfTheWeek = dayOfTheWeek;
        this.isActive = isActive;
        this.userCode = userCode;
        this.groupCode = groupCode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "DESCRIPCION")
    private String description;

    @Column(name = "COD_DIA_DE_LA_SEMANA")
    private int dayOfTheWeek;

    @Column(name = "ACTIVO")
    private boolean isActive;

    @Column(name = "COD_USUARIO_PROPIETARIO")
    private int userCode;

    @Column(name = "COD_GRUPO_USUARIO")
    private int groupCode;

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
        return this.isActive;
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

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public int getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(int groupCode) {
        this.groupCode = groupCode;
    }
}
