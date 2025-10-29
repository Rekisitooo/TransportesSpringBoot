package com.transports.spring.operation.transportcrudview.passenger;

import java.util.Map;

import com.transports.spring.operation.transportcrudview.passenger.notification.PassengerNotificationIconCalculator;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;
import com.transports.spring.vo.transportcrudview.passenger.VoTransCVGeneralPassengerIcon;

public class PassengerIconCalculator {

    /**
     * Calculates the general icons for a passenger based on their notifications and transports.
     * @return
     */
    public static VoTransCVGeneralPassengerIcon calculateGeneralPassengerIcons(final Map<Integer, VoCompleteNotification> passengerNotificationMap, Map<Integer, VoCompleteTransport> passengerTransportMap) {
        VoTransCVGeneralPassengerIcon generalPassengerIcon;
        
        final PassengerNotificationIconCalculator passengerNotificationIconCalculator = new PassengerNotificationIconCalculator();
        final VoTCVNotificationIconDisplay VoNotificationIconDisplay = passengerNotificationIconCalculator.calculateGeneralPassengerIcon(passengerNotificationMap, passengerTransportMap);

        if (VoNotificationIconDisplay.isShowIcon()) {
            generalPassengerIcon = new VoTransCVGeneralPassengerIcon(true, VoNotificationIconDisplay);
        } else {
            generalPassengerIcon = new VoTransCVGeneralPassengerIcon(false, new VoTCVNotificationIconDisplay(false, "red"));
        }

        return generalPassengerIcon;
    }

}