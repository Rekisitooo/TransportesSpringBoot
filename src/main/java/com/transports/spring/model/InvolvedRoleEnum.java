package com.transports.spring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvolvedRoleEnum {

    PASSENGER(1),
    DRIVER(1);

    private final int id;

}
