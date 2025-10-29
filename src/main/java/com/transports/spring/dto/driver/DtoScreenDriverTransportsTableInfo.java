package com.transports.spring.dto.driver;

import java.util.List;
import java.util.Map;

import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.model.Driver;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoScreenDriverTransportsTableInfo {

    private List<DtoTemplateDate> templateDateList;
    private List<Driver> templateDriverList;
    private Map<Integer, Map<Integer, List<VoCompleteTransport>>> allTemplateDriverTransports;
    private Map<Integer, Map<Integer, VoCompleteInvolvedAvailability>> driversAssistanceDates;
    private Map<Integer, Map<Integer, List<VoCompleteNotification>>> driverNotifications;

}
