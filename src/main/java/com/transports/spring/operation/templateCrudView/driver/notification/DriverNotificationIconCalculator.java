package com.transports.spring.operation.templateCrudView.driver.notification;

import java.util.Map;

import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.model.Transport;
import com.transports.spring.vo.notification.VoNotificationIconDisplay;

public class DriverNotificationIconCalculator {
    
    /**
     * Calculates the notification icon for a driver based on their notifications and transports.
     * 
     * @param driverNotificationMap A map of notifications for the driver, keyed by DateId.
     * @param driverTransportMap A map of transports for the driver, keyed by DateId
     * @return NotificationIconDisplay
    */
    public VoNotificationIconDisplay calculateGeneralDriverIcon(
        final Map<Integer, InvolvedTransportNotification> driverNotificationMap, Map<Integer, Transport> driverTransportMap) {
		VoNotificationIconDisplay voNotificationIconDisplay;

        // if there are the same amount of notifications than transports, calculate the icon
        if ((driverNotificationMap.size() - driverTransportMap.size()) <= 1)  {
            int redIconCount = 0;

            // count how many red icons there are
            for (Map.Entry<Integer, InvolvedTransportNotification> entry : driverNotificationMap.entrySet()) {
                final InvolvedTransportNotification notification = entry.getValue();
                final Transport transport = driverTransportMap.get(entry.getKey());
                
                final VoNotificationIconDisplay voNotification = calculateDateDriverIcon(notification, transport);
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
     * Calculates the notification icon for a driver based on a specific notification and transport.
     * @param notification The notification to evaluate.
     * @param transport The transport to evaluate.
     * @return
    */
	public VoNotificationIconDisplay calculateDateDriverIcon(final InvolvedTransportNotification notification, final Transport transport) {
		VoNotificationIconDisplay voNotificationIconDisplay;

        if (notification == null) {
            voNotificationIconDisplay = new VoNotificationIconDisplay(true, "red");

        // else if there is no transport nor notification, or if the notification's and transport's passenger matches
        } else if ((notification.getPassengerCode() == null && transport == null) ||
                 (notification.getPassengerCode() != null && 
                    notification.getPassengerCode().equals(transport.getTransportKey().getPassengerId()))) {

            voNotificationIconDisplay = new VoNotificationIconDisplay(true, "blue");

        } else {
            voNotificationIconDisplay = new VoNotificationIconDisplay(true, "red");
        }
        
        return voNotificationIconDisplay;
	}
}
