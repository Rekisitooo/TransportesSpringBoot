package com.transports.spring.service.notification;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.operation.transportcrudview.driver.notification.DriverNotificationIconCalculator;
import com.transports.spring.operation.transportcrudview.passenger.notification.PassengerNotificationIconCalculator;
import com.transports.spring.service.InvolvedAvailabiltyForTransportDateService;
import com.transports.spring.service.InvolvedTransportNotificationService;
import com.transports.spring.service.TransportService;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;

@Service
public class GeneralNotifIconService {

    private final InvolvedTransportNotificationService involvedTransportNotificationService;
    private final InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService;
    private final TransportService transportService;

    public GeneralNotifIconService(
        final TransportService transportService,
        final InvolvedTransportNotificationService involvedTransportNotificationService,
        final InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService) {

        this.transportService = transportService;
        this.involvedTransportNotificationService = involvedTransportNotificationService;
        this.involvedAvailabiltyForTransportDateService = involvedAvailabiltyForTransportDateService;
    }

    /**
     * Gets how the general driver notification icon should be at the moment: red, invisible...
     * @param templateId
     * @param driverId
     * @return
     */
    public VoTCVNotificationIconDisplay getGeneralDriverNotificationIconStatus(final Integer templateId, final Integer driverId) {
        final Driver driver = new Driver(driverId, null);
        final Map<Integer, List<VoCompleteNotification>> driverNotificationsMap = this.involvedTransportNotificationService.getDriverNotificationsMapByTemplate(templateId, driver);
        final Map<Integer, List<VoCompleteTransport>> driverTransportsMap =  this.transportService.findDriverTransportsFromTemplate(driver, templateId);
        final Map<Integer, VoCompleteInvolvedAvailability> driverAssistanceDates = this.involvedAvailabiltyForTransportDateService.findDriverAssistanceDatesForTemplate(templateId, driver);
        
        return new DriverNotificationIconCalculator().calculateGeneralNotificationsDriverIcon(driverNotificationsMap, driverTransportsMap, driverAssistanceDates);
    }

    /**
     * Gets how the general passenger notification icon should be at the moment: red, invisible...
     * @param templateId
     * @param passengerId
     * @return
     */
    public VoTCVNotificationIconDisplay getGeneralPassengerNotificationIconStatus(final Integer templateId, final Integer passengerId) {
        final Passenger passenger = new Passenger(passengerId, null);
        final Map<Integer, VoCompleteNotification> passengerNotificationsMap = this.involvedTransportNotificationService.getPassengerNotificationsMapByTemplate(templateId, passenger);
        final Map<Integer, VoCompleteTransport> passengerTransportsMap =  this.transportService.findPassengerTransportsFromTemplate(passenger, templateId);
        final Map<Integer, VoCompleteInvolvedAvailability> passengerAssistanceDates = this.involvedAvailabiltyForTransportDateService.findPassengersAssistanceDates(templateId, passenger);
        
        return new PassengerNotificationIconCalculator().calculateGeneralNotificationsPassengerIcon(passengerNotificationsMap, passengerTransportsMap, passengerAssistanceDates);
    }
}
