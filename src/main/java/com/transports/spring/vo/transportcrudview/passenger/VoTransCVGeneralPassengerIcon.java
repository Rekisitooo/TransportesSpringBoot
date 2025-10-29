package com.transports.spring.vo.transportcrudview.passenger;

import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoTransCVGeneralPassengerIcon {
    private boolean showIcons;
    private VoTCVNotificationIconDisplay voNotificationIconDisplay;

}
