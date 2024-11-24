package com.transports.spring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PLANTILLA")
public final class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "ANNO")
    private String year;

    @Column(name = "MES")
    private String month;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
