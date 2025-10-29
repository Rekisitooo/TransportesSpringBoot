package com.transports.spring.operation.transportcrudview.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.dto.driver.DtoScreenDriverTransportsTableInfo;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.operation.transportcrudview.driver.notification.DriverNotificationIconCalculator;
import com.transports.spring.operation.transportcrudview.passenger.notification.PassengerNotificationIconCalculator;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;
import com.transports.spring.vo.transportcrudview.driver.VoTransCVDriver;
import com.transports.spring.vo.transportcrudview.driver.VoTransCVDriverDateInfo;
import com.transports.spring.vo.transportcrudview.driver.VoTransCVGeneralDriverIcon;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;

public class DriverTemplateCrudDataProvider {

        /**
         * Transforms the DTO information into a list of VO objects for displaying
         * driver transport information in the CRUD screen.
         * 
         * @param dtoScreenDriverTransportsTableInfo The DTO containing driver
         *                                              transport information.
         * @return A list of VO objects representing driver transport information.
         */
        public static List<VoTransCVDriver> getScreenDriverTransportsTableInfo(final DtoScreenDriverTransportsTableInfo dtoScreenDriverTransportsTableInfo) {

                final List<DtoTemplateDate> templateDateList = dtoScreenDriverTransportsTableInfo.getTemplateDateList();
                final List<VoTransCVDriver> listVoTransportCrudScreenDriver = new ArrayList<>();
                final Map<Integer, Map<Integer, VoCompleteInvolvedAvailability>> allDriversAssistanceDates = dtoScreenDriverTransportsTableInfo.getDriversAssistanceDates();
                final Map<Integer, Map<Integer, List<VoCompleteNotification>>> allDriverNotifications = dtoScreenDriverTransportsTableInfo.getDriverNotifications();
                final Map<Integer, Map<Integer, List<VoCompleteTransport>>> allDriverTransports = dtoScreenDriverTransportsTableInfo.getAllTemplateDriverTransports();

                for (final Driver driver : dtoScreenDriverTransportsTableInfo.getTemplateDriverList()) {

                        final boolean driverHasOneOrMoreNotifications = (allDriverNotifications.get(driver.getId()) != null);
                        final boolean driverHasOneOrMoreTransports = (allDriverTransports.get(driver.getId()) != null);

                        // info to draw the general driver icons
                        final VoTransCVGeneralDriverIcon voGeneralDriverIcon = DriverIconCalculator.calculateGeneralDriverIcons(
                                                        allDriverNotifications.get(driver.getId()),
                                                        allDriverTransports.get(driver.getId()));

                        // vo with all the driver info
                        final VoTransCVDriver voTransportCrudScreenDriver = new VoTransCVDriver(
                                        voGeneralDriverIcon,
                                        driver);

                        final Map<Integer, VoCompleteInvolvedAvailability> driverAssistanceDates = allDriversAssistanceDates.get(driver.getId());

                        // info for each date
                        for (final DtoTemplateDate templateDate : templateDateList) {
                                final Integer dateId = templateDate.getId();
                                final VoTransCVDriverDateInfo screenDriver = new VoTransCVDriverDateInfo();
                                VoTCVNotificationIconDisplay notificationIconDisplay = new VoTCVNotificationIconDisplay(false, "red");
                                final VoCompleteInvolvedAvailability voCompleteInvolvedAvailability = driverAssistanceDates.get(dateId);
                                
                                if (voCompleteInvolvedAvailability != null) {
                                        screenDriver.setAssistsOnDate(voCompleteInvolvedAvailability.getInvolvedAvailabiltyForTransportDate().getInvolvedCode() == driver.getId());
                                        screenDriver.setProvidesTransport(voCompleteInvolvedAvailability.getInvolvedAvailabiltyForTransportDate().getNeedsTransport() == 1);

                                        // info for the driver notification icon
                                        if (driverHasOneOrMoreNotifications && driverHasOneOrMoreTransports) {
                                                notificationIconDisplay = DriverNotificationIconCalculator.calculateDateDriverIcon(
                                                        allDriverNotifications.get(driver.getId()).get(dateId),
                                                        allDriverTransports.get(driver.getId()).get(dateId));

                                        } else if (driverHasOneOrMoreNotifications || driverHasOneOrMoreTransports) {
                                                notificationIconDisplay = new VoTCVNotificationIconDisplay(true, "red");
                                        }

                                        // info for the driver transport
                                        final boolean driverHasTransportOnDate = (allDriverTransports.get(driver.getId()) != null && allDriverTransports.get(driver.getId()).get(dateId) != null);
                                        if (driverHasTransportOnDate) {
                                                final List<VoCompleteTransport> assignedPassengerList = allDriverTransports.get(driver.getId()).get(dateId);
                                                for (VoCompleteTransport assignedPassenger : assignedPassengerList) {
                                                        screenDriver.addAssignedPassenger(assignedPassenger.getPassenger());
                                                }
                                        }

                                        // info for the driver notified passengers
                                        final boolean driverHasNotificationOnDate = (driverHasOneOrMoreNotifications && allDriverNotifications.get(driver.getId()).get(dateId) != null);
                                        if (driverHasNotificationOnDate) {
                                                final List<VoCompleteNotification> notifiedPassengerList = allDriverNotifications.get(driver.getId()).get(dateId);
                                                for (VoCompleteNotification assignedPassenger : notifiedPassengerList) {
                                                        screenDriver.addNotifiedPassenger(assignedPassenger.getPassenger());
                                                }
                                        }
                                }

                                screenDriver.setVoNotificationIconDisplay(notificationIconDisplay);
                                voTransportCrudScreenDriver.addDriverInfo(dateId, screenDriver);
                        }

                        // add driver info to the list
                        listVoTransportCrudScreenDriver.add(voTransportCrudScreenDriver);
                }

                return listVoTransportCrudScreenDriver;
        }


        /**
         * Gets the list of assigned passengers from a list of complete transport VOs.
         * @param voCompleteTransportList The list of complete transport VOs.
         * @return The list of assigned passengers.
         */
        public static List<Passenger> getAssignedPassengerList(final List<VoCompleteTransport> voCompleteTransportList) {
                
                final List<Passenger> passengerList = new ArrayList<>();
                for (final VoCompleteTransport voCompleteTransport : voCompleteTransportList) {
                        if (voCompleteTransport.getPassenger() != null) {
                                passengerList.add(voCompleteTransport.getPassenger());
                        }
                }
                 
                return passengerList;
        }

        /**
         * Gets the list of assigned passengers from a list of complete transport VOs.
         * @param voCompleteNotificationList The list of complete notification VOs.
         * @return The list of assigned passengers.
         */
        public static List<Passenger> getNotifiedPassengerList(final List<VoCompleteNotification> voCompleteNotificationList) {

                final List<Passenger> passengerList = new ArrayList<>();
                for (final VoCompleteNotification voCompleteNotification : voCompleteNotificationList) {
                        if (voCompleteNotification.getPassenger() != null) {
                                passengerList.add(voCompleteNotification.getPassenger());
                        }
                }
                 
                return passengerList;
        }
}
