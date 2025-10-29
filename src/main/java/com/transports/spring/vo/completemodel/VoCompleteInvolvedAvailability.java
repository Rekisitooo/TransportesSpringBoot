package com.transports.spring.vo.completemodel;

import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;
import com.transports.spring.model.TransportDateByTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoCompleteInvolvedAvailability {
    private InvolvedAvailabiltyForTransportDate involvedAvailabiltyForTransportDate;
    private TransportDateByTemplate transportDateByTemplate;
}