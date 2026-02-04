package com.transports.spring.operation.transportcrudview.passenger.notification;

import java.util.Map;

import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.model.Transport;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;

public class PassengerNotificationIconCalculator {

    /**
     * Calculates the notification icon for a passenger based on their notifications
     * and transports.
     * 
     * @param passengerNotificationMap A map of notifications for the passenger,
     *                                 keyed by DateId.
     * @param passengerTransportMap    A map of transports for the passenger, keyed
     *                                 by DateId
     * @param passengerAssistanceDates A map of the passenger assitance dates, keyed
     *                                  by DateId
     * @return NotificationIconDisplay
     */
    public VoTCVNotificationIconDisplay calculateGeneralNotificationsPassengerIcon(
        final Map<Integer, VoCompleteNotification> passengerNotificationMap, 
        final Map<Integer, VoCompleteTransport> passengerTransportMap, 
        final Map<Integer, VoCompleteInvolvedAvailability> passengerAssistanceDates) {
        
        VoTCVNotificationIconDisplay voTCVNotificationIconDisplay;

        // passenger never assists
        if (passengerAssistanceDates == null || passengerAssistanceDates.isEmpty()) {
            voTCVNotificationIconDisplay = new VoTCVNotificationIconDisplay(false, "red");
        
        } else {

            int visibleNotificationIcons = this.getRedShownNotificationIcons(
                passengerAssistanceDates, passengerNotificationMap, passengerTransportMap);

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
     * @param passengerAssistanceDates
     * @param passengerNotificationMap
     * @param passengerTransportMap
     * @param visibleNotificationIcons
     * @return
     */
    private int getRedShownNotificationIcons(
        final Map<Integer, VoCompleteInvolvedAvailability> passengerAssistanceDates, 
        final Map<Integer, VoCompleteNotification> passengerNotificationMap, 
        final Map<Integer, VoCompleteTransport> passengerTransportMap) {
            
        int visibleNotificationIcons = 0;


        // each passenger assistance date
        for (final Map.Entry<Integer, VoCompleteInvolvedAvailability> passengerAvailability : passengerAssistanceDates.entrySet()) {

            // Calculate notification icon display
            final VoCompleteNotification voCompleteNotification = (passengerNotificationMap != null) ?
                passengerNotificationMap.get(passengerAvailability.getKey()) : null;
            
            final VoCompleteTransport voCompleteTransport = (passengerTransportMap != null) ?
                passengerTransportMap.get(passengerAvailability.getKey()) : null;

            final VoTCVNotificationIconDisplay notificationIconDisplay = calculateDatePassengerNotifIcon(voCompleteNotification, voCompleteTransport);

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
     * Calculates the notification icon for a passenger based on a specific
     * notification and transport.
     * 
     * @param voCompleteNotification The notification to evaluate.
     * @param voCompleteTransport    The transport to evaluate.
     * @return
     */
    public static VoTCVNotificationIconDisplay calculateDatePassengerNotifIcon(
        final VoCompleteNotification voCompleteNotification,
        final VoCompleteTransport voCompleteTransport) {

        VoTCVNotificationIconDisplay VoTCVNotificationIconDisplay;

        if (voCompleteNotification != null && voCompleteTransport != null) {
            final InvolvedTransportNotification notification = voCompleteNotification.getInvolvedTransportNotification();
            final Transport transport = voCompleteTransport.getTransport();

            if (notification.getDriverCode() != null && notification.getDriverCode().equals(transport.getTransportKey().getDriverId())) {
                VoTCVNotificationIconDisplay = new VoTCVNotificationIconDisplay(true, "blue");
            } else {
                VoTCVNotificationIconDisplay = new VoTCVNotificationIconDisplay(true, "red");
            }

        } else {
            VoTCVNotificationIconDisplay = new VoTCVNotificationIconDisplay(false, "red");
        }

        return VoTCVNotificationIconDisplay;
    }

}
