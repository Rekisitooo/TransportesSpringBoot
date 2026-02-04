package com.transports.spring.operation.transportcrudview.driver;

import java.util.List;
import java.util.Map;

import com.transports.spring.operation.transportcrudview.driver.notification.DriverNotificationIconCalculator;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;
import com.transports.spring.vo.transportcrudview.driver.VoTransCVGeneralDriverIcon;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;

public class DriverIconCalculator {

    /**
     * Calculates the general icons for a driver based on their notifications and transports.
     * @param driverNotificationMap A map of notifications for the driver,
     *                                 keyed by DateId.
     * @param driverTransportMap    A map of transports for the driver, keyed
     *                                 by DateId
     * @param driverAssistanceDates A map of the driver assitance dates, keyed
     *                                  by DateId
     * @return
     */
    public static VoTransCVGeneralDriverIcon calculateGeneralNotificationDriverIcon(
        final Map<Integer, List<VoCompleteNotification>> driverNotificationMap,
        final Map<Integer, List<VoCompleteTransport>> driverTransportMap,
        final Map<Integer, VoCompleteInvolvedAvailability> driverAssistanceDates) {
            
        VoTransCVGeneralDriverIcon generalDriverIcon;
        
        final DriverNotificationIconCalculator driverNotificationIconCalculator = new DriverNotificationIconCalculator();
        final VoTCVNotificationIconDisplay VoNotificationIconDisplay = 
            driverNotificationIconCalculator.calculateGeneralNotificationsDriverIcon(
                driverNotificationMap, driverTransportMap, driverAssistanceDates);
        
        if (VoNotificationIconDisplay.isShowIcon()) {
            generalDriverIcon = new VoTransCVGeneralDriverIcon(true, VoNotificationIconDisplay);
        } else {
            generalDriverIcon = new VoTransCVGeneralDriverIcon(false, new VoTCVNotificationIconDisplay(false, "red"));
        }

        return generalDriverIcon;
    }

}