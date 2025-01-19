package com.transports.spring.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DIA_DE_LA_SEMANA")
public final class DayOfTheWeek {

    @Id
    private int id;

    @Column(name = "NOMBRE")
    private String name;


}
