package com.transports.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoGenerateTemplateFiles {
    private int templateYear;
    private int templateMonth;
    private String involved;
    private List<DtoTemplateDay> extraDays;
}
