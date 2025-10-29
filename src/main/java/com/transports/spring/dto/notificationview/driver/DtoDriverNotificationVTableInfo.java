package com.transports.spring.dto.notificationview.driver;

import java.util.List;
import java.util.Map;

import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.model.Driver;
import com.transports.spring.vo.completemodel.VoCompleteNotification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoDriverNotificationVTableInfo {
    private List<DtoTemplateDate> templateDateList;
    private List<Driver> templateDriverList;
    private Map<Integer, Map<Integer, List<VoCompleteNotification>>> driverNotifications;

}
