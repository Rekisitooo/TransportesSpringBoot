package com.transports.spring.operation.transportcrudview.driver.notification;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;

public class DriverNotificationIconCalculator {
    
    /**
     * Calculates the notification icon for a driver based on their notifications
     * and transports.
     * 
     * @param driverNotificationMap A map of notifications for the driver,
     *                                 keyed by DateId.
     * @param driverTransportMap    A map of transports for the driver, keyed
     *                                 by DateId
     * @param driverAssistanceDates A map of the driver assitance dates, keyed
     *                                  by DateId
     * @return NotificationIconDisplay
     */
    public VoTCVNotificationIconDisplay calculateGeneralNotificationsDriverIcon(
        final Map<Integer, List<VoCompleteNotification>> driverNotificationMap, 
        final Map<Integer, List<VoCompleteTransport>> driverTransportMap, 
        final Map<Integer, VoCompleteInvolvedAvailability> driverAssistanceDates) {
        
        VoTCVNotificationIconDisplay voTCVNotificationIconDisplay;

        // driver never assists
        if (driverAssistanceDates == null || driverAssistanceDates.isEmpty()) {
            voTCVNotificationIconDisplay = new VoTCVNotificationIconDisplay(false, "red");
        
        } else {

            int visibleNotificationIcons = this.getRedShownNotificationIcons(
                driverAssistanceDates, driverNotificationMap, driverTransportMap);

                if (visibleNotificationIcons > 1) {
                    voTCVNotificationIconDisplay = new VoTCVNotificationIconDisplay(true, "red");
                } else {
                    voTCVNotificationIconDisplay = new VoTCVNotificationIconDisplay(false, "red");
                }
        }

        return voTCVNotificationIconDisplay;
    }

    /**
     * Calculates how many red shown notification icons are there.
     * 
     * @param driverAssistanceDates
     * @param driverNotificationMap
     * @param driverTransportMap
     * @param visibleNotificationIcons
     * @return
     */
    private int getRedShownNotificationIcons(
        final Map<Integer, VoCompleteInvolvedAvailability> driverAssistanceDates, 
        final Map<Integer, List<VoCompleteNotification>> driverNotificationMap, 
        final Map<Integer, List<VoCompleteTransport>> driverTransportMap) {
            
        int visibleNotificationIcons = 0;

        // each driver assistance date
        for (final Map.Entry<Integer, VoCompleteInvolvedAvailability> driverAvailability : driverAssistanceDates.entrySet()) {
           
            // Calculate notification icon display
            final List<VoCompleteNotification> voCompleteNotification = (driverNotificationMap != null) ?
                driverNotificationMap.get(driverAvailability.getKey()) : null;
            
            final List<VoCompleteTransport> voCompleteTransport = (driverTransportMap != null) ?
                driverTransportMap.get(driverAvailability.getKey()) : null;

            final VoTCVNotificationIconDisplay notificationIconDisplay = calculateDateDriverNotifIcon(voCompleteNotification, voCompleteTransport);
            
            // icon is shown and red
            if (notificationIconDisplay.isShowIcon() && "red".equalsIgnoreCase(notificationIconDisplay.getIconColor())) {
                visibleNotificationIcons++;
            }
            
            // if the are already more than 1, it's enough to draw the icon
            if (visibleNotificationIcons > 1) {
                break;
            }
        }

        return visibleNotificationIcons;
    }

    /**
     * Calculates the notification icon for a driver based on a specific notification and transport.
     * @param notificationList The list of notifications for the driver to evaluate.
     * @param transportList The list of transports for the driver to evaluate.
     * @return voNotificationIconDisplay
    */
	public static VoTCVNotificationIconDisplay calculateDateDriverNotifIcon(
        final List<VoCompleteNotification> notificationList, 
        final List<VoCompleteTransport> transportList) {

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
        final List<Integer> notificatedDriverIds = notificationList.stream()
                                       .map(VoCompleteNotification::getDriverId)
                                       .sorted()
                                       .collect(Collectors.toList());

        final List<Integer> transportedDriverIds = transportList.stream()
                                       .map(VoCompleteTransport::getDriverId)
                                       .sorted()
                                       .collect(Collectors.toList());

        return notificatedDriverIds.equals(transportedDriverIds);
    }
}
