package com.transports.spring.operation.transportcrudview.passenger;

import java.util.Map;

import com.transports.spring.operation.transportcrudview.passenger.notification.PassengerNotificationIconCalculator;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;
import com.transports.spring.vo.transportcrudview.passenger.VoTransCVGeneralPassengerIcon;

public class PassengerIconCalculator {

    /**
     * Calculates the general icons for a passenger based on their notifications and transports.
     * 
     * @param passengerNotificationMap A map of notifications for the passenger,
     *                                 keyed by DateId.
     * @param passengerTransportMap    A map of transports for the passenger, keyed
     *                                 by DateId
     * @param passengerAssistanceDates A map of the passenger assitance dates, keyed
     *                                  by DateId
     * @return
     */
    public static VoTransCVGeneralPassengerIcon calculateGeneralPassengerIcons(
        final Map<Integer, VoCompleteNotification> passengerNotificationMap, 
        final Map<Integer, VoCompleteTransport> passengerTransportMap, 
        final Map<Integer, VoCompleteInvolvedAvailability> passengerAssistanceDates) {

        VoTransCVGeneralPassengerIcon generalPassengerIcon;
        
        final PassengerNotificationIconCalculator passengerNotificationIconCalculator = new PassengerNotificationIconCalculator();
        final VoTCVNotificationIconDisplay VoNotificationIconDisplay = 
            passengerNotificationIconCalculator.calculateGeneralNotificationsPassengerIcon(
                passengerNotificationMap, passengerTransportMap, passengerAssistanceDates);

        if (VoNotificationIconDisplay.isShowIcon()) {
            generalPassengerIcon = new VoTransCVGeneralPassengerIcon(true, VoNotificationIconDisplay);
        } else {
            generalPassengerIcon = new VoTransCVGeneralPassengerIcon(false, new VoTCVNotificationIconDisplay(false, "red"));
        }

        return generalPassengerIcon;
    }

}