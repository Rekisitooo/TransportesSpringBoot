package com.transports.spring.vo.passenger.notification;

import com.transports.spring.vo.notification.VoNotificationIconDisplay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoGeneralPassengerIcon {
    private boolean showIcon;
    private VoNotificationIconDisplay voNotificationIconDisplay;

}
