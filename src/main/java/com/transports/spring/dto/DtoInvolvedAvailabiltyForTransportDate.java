    package com.transports.spring.dto;

import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;
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
    private String involvedCompleteName;
}
