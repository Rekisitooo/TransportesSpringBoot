package com.transports.spring.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Month;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    /**
     * @return monthName in english in lower case
     */
    public String getMonthName(){
        final int monthId = Integer.parseInt(this.getMonth());
        return Month.of(monthId).name().toLowerCase();
    }
}
