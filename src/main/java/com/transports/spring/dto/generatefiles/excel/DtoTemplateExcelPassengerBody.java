package com.transports.spring.dto.generatefiles.excel;

import java.time.LocalDate;
import java.util.Map;

import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoTemplateExcelPassengerBody {
    private Map<LocalDate, VoCompleteInvolvedAvailability> passengerAssistanceDateList;
    private Map<LocalDate, DtoTemplateDate> monthTransportDateByDayMap;
    private Map<LocalDate, DtoPassengerTransport> allTemplatePassengerTransportsByDayMap;
}
