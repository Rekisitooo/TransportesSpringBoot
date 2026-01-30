package com.transports.spring.operation.transportcrudview.passenger.notification;

import java.util.Map;

import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.model.Transport;
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
     * @return NotificationIconDisplay
     */
    public VoTCVNotificationIconDisplay calculateGeneralNotificationsPassengerIcon(final Map<Integer, VoCompleteNotification> passengerNotificationMap, Map<Integer, VoCompleteTransport> passengerTransportMap) {
        VoTCVNotificationIconDisplay VoTCVNotificationIconDisplay;

        // if there are the same amount of notifications than transports, calculate the icon
        if ((passengerNotificationMap != null && passengerTransportMap != null) && (passengerNotificationMap.size() - passengerTransportMap.size()) <= 1) {
            int redIconCount = 0;

            // count how many red icons there are
            for (Map.Entry<Integer, VoCompleteNotification> entry : passengerNotificationMap.entrySet()) {
                final VoCompleteNotification notification = entry.getValue();
                final VoCompleteTransport transport = passengerTransportMap.get(entry.getKey());

                final VoTCVNotificationIconDisplay voNotification = calculateDatePassengerIcon(notification, transport);
                if ("red".equals(voNotification.getIconColor())) {
                    redIconCount++;
                }
            }

            VoTCVNotificationIconDisplay = new VoTCVNotificationIconDisplay(redIconCount > 1, "red");

        } else {
            // if there are less notifications than transports, show red icon
            VoTCVNotificationIconDisplay = new VoTCVNotificationIconDisplay(true, "red");
        }

        return VoTCVNotificationIconDisplay;
    }

    /**
     * Calculates the notification icon for a passenger based on a specific
     * notification and transport.
     * 
     * @param voCompleteNotification The notification to evaluate.
     * @param voCompleteTransport    The transport to evaluate.
     * @return
     */
    public static VoTCVNotificationIconDisplay calculateDatePassengerIcon(final VoCompleteNotification voCompleteNotification, final VoCompleteTransport voCompleteTransport) {
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
