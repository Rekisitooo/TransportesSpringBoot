package com.transports.spring.dto.notificationview.passenger;

import java.util.List;
import java.util.Map;

import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.model.Passenger;
import com.transports.spring.vo.completemodel.VoCompleteNotification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoPassengerNotificationVTableInfo {
    private List<DtoTemplateDate> templateDateList;
    private List<Passenger> templatePassengerList;
    private Map<Integer, Map<Integer, VoCompleteNotification>> passengerNotifications;

}
