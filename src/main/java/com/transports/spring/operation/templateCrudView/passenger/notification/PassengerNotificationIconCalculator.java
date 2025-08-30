package com.transports.spring.operation.templateCrudView.passenger.notification;

import java.util.Map;

import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.model.Transport;
import com.transports.spring.vo.notification.VoNotificationIconDisplay;

public class PassengerNotificationIconCalculator {
    
    /**
     * Calculates the notification icon for a passenger based on their notifications and transports.
     * 
     * @param passengerNotificationMap A map of notifications for the passenger, keyed by DateId.
     * @param passengerTransportMap A map of transports for the passenger, keyed by DateId
     * @return NotificationIconDisplay
    */
    public VoNotificationIconDisplay calculateGeneralPassengerIcon(
        final Map<Integer, InvolvedTransportNotification> passengerNotificationMap, Map<Integer, Transport> passengerTransportMap) {
		VoNotificationIconDisplay voNotificationIconDisplay;

        // if there are the same amount of notifications than transports, calculate the icon
        if ((passengerNotificationMap.size() - passengerTransportMap.size()) <= 1)  {
            int redIconCount = 0;

            // count how many red icons there are
            for (Map.Entry<Integer, InvolvedTransportNotification> entry : passengerNotificationMap.entrySet()) {
                final InvolvedTransportNotification notification = entry.getValue();
                final Transport transport = passengerTransportMap.get(entry.getKey());
                
                final VoNotificationIconDisplay voNotification = calculateDatePassengerIcon(notification, transport);
                if ("red".equals(voNotification.getIconColor())) {
                    redIconCount++;
                }
            }

            voNotificationIconDisplay = new VoNotificationIconDisplay(redIconCount > 1, "red");

        } else {
            // if there are less notifications than transports, show red icon
            voNotificationIconDisplay = new VoNotificationIconDisplay(true, "red");
        }
		
        return voNotificationIconDisplay;
	}

    /**
     * Calculates the notification icon for a passenger based on a specific notification and transport.
     * @param notification The notification to evaluate.
     * @param transport The transport to evaluate.
     * @return
    */
	public VoNotificationIconDisplay calculateDatePassengerIcon(final InvolvedTransportNotification notification, final Transport transport) {
		VoNotificationIconDisplay voNotificationIconDisplay;

        if (notification == null) {
            voNotificationIconDisplay = new VoNotificationIconDisplay(true, "red");

        // else if there is no transport nor notification, or if the notification's and transport's passenger matches
        } else if ((notification.getDriverCode() == null && transport == null) ||
                 (notification.getDriverCode() != null && 
                    notification.getDriverCode().equals(transport.getTransportKey().getDriverId()))) {

            voNotificationIconDisplay = new VoNotificationIconDisplay(true, "blue");

        } else {
            voNotificationIconDisplay = new VoNotificationIconDisplay(true, "red");
        }
        
        return voNotificationIconDisplay;
	}
}
