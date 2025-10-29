package com.transports.spring.operation.transportcrudview.driver.notification;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;

public class DriverNotificationIconCalculator {
    
    /**
     * Calculates the notification icon for a driver based on their notifications and transports.
     * 
     * @param driverNotificationMap A map of notifications for the driver, keyed by DateId.
     * @param driverTransportMap A map of transports for the driver, keyed by DateId
     * @return NotificationIconDisplay
    */
    public VoTCVNotificationIconDisplay calculateGeneralDriverIcon(final Map<Integer, List<VoCompleteNotification>> driverNotificationMap, final Map<Integer, List<VoCompleteTransport>> driverTransportMap) {
		VoTCVNotificationIconDisplay voNotificationIconDisplay;

        // if there are the same amount of notifications than transports, calculate the icon
        if ((driverNotificationMap != null && driverTransportMap != null) && (driverNotificationMap.size() - driverTransportMap.size()) <= 1) {
            int redIconCount = 0;

            // count how many red icons there are
            for (Map.Entry<Integer, List<VoCompleteNotification>> entry : driverNotificationMap.entrySet()) {
                final List<VoCompleteNotification> notifications = entry.getValue();
                final List<VoCompleteTransport> transports = driverTransportMap.get(entry.getKey());

                final VoTCVNotificationIconDisplay voNotification = calculateDateDriverIcon(notifications, transports);
                if ("red".equals(voNotification.getIconColor())) {
                    redIconCount++;
                }
            }

            voNotificationIconDisplay = new VoTCVNotificationIconDisplay(redIconCount > 1, "red");

        } else {
            // if there are less notifications than transports, it has to show a red icon
            voNotificationIconDisplay = new VoTCVNotificationIconDisplay(true, "red");
        }
		
        return voNotificationIconDisplay;
	}

    /**
     * Calculates the notification icon for a driver based on a specific notification and transport.
     * @param notificationList The list of notifications for the driver to evaluate.
     * @param transportList The list of transports for the driver to evaluate.
     * @return VoNotificationIconDisplay
    */
	public static VoTCVNotificationIconDisplay calculateDateDriverIcon(final List<VoCompleteNotification> notificationList, final List<VoCompleteTransport> transportList) {
		VoTCVNotificationIconDisplay voNotificationIconDisplay;
        
        final boolean driverHasNotificationsAndTransports = (notificationList != null && !notificationList.isEmpty() && transportList != null && !transportList.isEmpty());
        if (driverHasNotificationsAndTransports && areNotifiedDriversSameAsTransportedDrivers(notificationList, transportList)) {
            voNotificationIconDisplay = new VoTCVNotificationIconDisplay(true, "blue");
        } else {
            voNotificationIconDisplay = new VoTCVNotificationIconDisplay(true, "red");
        }
        
        return voNotificationIconDisplay;
	}

    public static boolean areNotifiedDriversSameAsTransportedDrivers(final List<VoCompleteNotification> notificationList, final List<VoCompleteTransport> transportList) {
        final List<Integer> notificatedPassengerIds = notificationList.stream()
                                       .map(VoCompleteNotification::getPassengerId)
                                       .sorted()
                                       .collect(Collectors.toList());

        final List<Integer> transportedPassengerIds = transportList.stream()
                                       .map(VoCompleteTransport::getPassengerId)
                                       .sorted()
                                       .collect(Collectors.toList());

        return notificatedPassengerIds.equals(transportedPassengerIds);
    }
}
