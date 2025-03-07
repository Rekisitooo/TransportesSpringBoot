package com.transports.spring.dto;

import com.transports.spring.model.Template;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
public class DtoTemplateData {
    private final Template template;
    private final int lastMonthDay;

    public String getMonthName() {
        return this.template.getMonthName();
    }

    public String getYear() {
        return this.template.getYear();
    }

    public int getId() {
        return this.template.getId();
    }

    public String getMonth() {
        return this.template.getMonth();
    }
}
