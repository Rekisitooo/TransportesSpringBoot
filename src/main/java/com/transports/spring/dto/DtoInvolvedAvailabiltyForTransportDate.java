    package com.transports.spring.dto;

import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;
import com.transports.spring.model.InvolvedByTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoInvolvedAvailabiltyForTransportDate {
    private InvolvedAvailabiltyForTransportDate involvedAvailabiltyForTransportDate;
    private InvolvedByTemplate involvedByTemplate;

    public String getInvolvedCompleteName() {
        String involvedCompleteName = "";
        if (involvedByTemplate != null) {
            return involvedByTemplate.getName() + " " + involvedByTemplate.getSurname();
        }
        return involvedCompleteName;
    }
}
