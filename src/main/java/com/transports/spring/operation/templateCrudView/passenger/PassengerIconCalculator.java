package com.transports.spring.operation.templateCrudView.passenger;

import java.util.Map;

import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.model.Transport;
import com.transports.spring.operation.templateCrudView.passenger.notification.PassengerNotificationIconCalculator;
import com.transports.spring.vo.notification.VoNotificationIconDisplay;
import com.transports.spring.vo.passenger.notification.VoGeneralPassengerIcon;

public class PassengerIconCalculator {

    /**
     * Calculates the general icons for a passenger based on their notifications and transports.
     * @return
     */
    public VoGeneralPassengerIcon calculateGeneralPassengerIcons(final Map<Integer, InvolvedTransportNotification> passengerNotificationMap, Map<Integer, Transport> passengerTransportMap) {
        VoGeneralPassengerIcon generalPassengerIcon;
        
        final PassengerNotificationIconCalculator passengerNotificationIconCalculator = new PassengerNotificationIconCalculator();
        final VoNotificationIconDisplay VoNotificationIconDisplay = passengerNotificationIconCalculator.calculateGeneralPassengerIcon(passengerNotificationMap, passengerTransportMap);

        if (VoNotificationIconDisplay.isShowIcon()) {
            generalPassengerIcon = new VoGeneralPassengerIcon(true, VoNotificationIconDisplay);
        } else {
            generalPassengerIcon = new VoGeneralPassengerIcon(false, null);
        }

        return generalPassengerIcon;
    }

}