package com.transports.spring.operation.transportcrudview.passenger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.dto.passenger.DtoScreenPassengerTransportsTableInfo;
import com.transports.spring.model.Passenger;
import com.transports.spring.operation.transportcrudview.passenger.notification.PassengerNotificationIconCalculator;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;
import com.transports.spring.vo.transportcrudview.passenger.VoTransCVGeneralPassengerIcon;
import com.transports.spring.vo.transportcrudview.passenger.VoTransCVPassenger;
import com.transports.spring.vo.transportcrudview.passenger.VoTransCVPassengerDateInfo;
import com.transports.spring.vo.transportcrudview.passenger.transport.VoPassengerTransportDisplay;

public class PassengerTemplateCrudDataProvider {

        /**
         * Transforms the DTO information into a list of VO objects for displaying
         * passenger transport information in the CRUD screen.
         * 
         * @param dtoScreenPassengerTransportsTableInfo The DTO containing passenger
         *                                              transport information.
         * @return A list of VO objects representing passenger transport information.
         */
        public static List<VoTransCVPassenger> getScreenPassengerTransportsTableInfo(final DtoScreenPassengerTransportsTableInfo dtoScreenPassengerTransportsTableInfo) {

                final List<DtoTemplateDate> templateDateList = dtoScreenPassengerTransportsTableInfo.getTemplateDateList();
                final List<VoTransCVPassenger> listVoTransportCrudScreenPassenger = new ArrayList<>();
                final Map<Integer, Map<Integer, VoCompleteInvolvedAvailability>> allPassengersAssistanceDates = dtoScreenPassengerTransportsTableInfo.getPassengersAssistanceDates();
                final Map<Integer, Map<Integer, VoCompleteNotification>> allPassengerNotifications = dtoScreenPassengerTransportsTableInfo.getPassengerNotifications();
                final Map<Integer, Map<Integer, VoCompleteTransport>> allPassengerTransports = dtoScreenPassengerTransportsTableInfo.getAllTemplatePassengerTransports();
                
                for (final Passenger passenger : dtoScreenPassengerTransportsTableInfo.getTemplatePassengerList()) {

                        final boolean passengerHasOneOrMoreNotifications = (allPassengerNotifications.get(passenger.getId()) != null);
                        final boolean passengerHasOneOrMoreTransports = (allPassengerTransports.get(passenger.getId()) != null);
                        final Map<Integer, VoCompleteInvolvedAvailability> passengerAssistanceDates = allPassengersAssistanceDates.get(passenger.getId());
                        int redShownNotifIcons = 0;

                        // info to draw the general passenger icons
                        final VoTransCVGeneralPassengerIcon voGeneralPassengerIcon = new VoTransCVGeneralPassengerIcon();

                        // vo with all the passenger info
                        final VoTransCVPassenger voTransportCrudScreenPassenger = new VoTransCVPassenger(voGeneralPassengerIcon, passenger);

                        // info for each date
                        for (final DtoTemplateDate templateDate : templateDateList) {
                                final Integer dateId = templateDate.getId();
                                
                                final VoPassengerTransportDisplay passengerTransportDisplay = new VoPassengerTransportDisplay();
                                passengerTransportDisplay.setAvailableDriverList(dtoScreenPassengerTransportsTableInfo.getDriversAvailableByDate().get(dateId), passenger);

                                VoTCVNotificationIconDisplay notificationIconDisplay = new VoTCVNotificationIconDisplay(false, "red");

                                final VoTransCVPassengerDateInfo screenPassenger = new VoTransCVPassengerDateInfo();

                                // passenger assists on date
                                final VoCompleteInvolvedAvailability voCompleteInvolvedAvailability = passengerAssistanceDates.get(dateId);
                                if (voCompleteInvolvedAvailability != null) {

                                        screenPassenger.setAssistsOnDate(
                                                voCompleteInvolvedAvailability.getInvolvedAvailabiltyForTransportDate().getInvolvedCode() == passenger.getId());
                                        
                                        // if passenger does not assists, it does not need transport either
                                        screenPassenger.setNeedsTransport(
                                                voCompleteInvolvedAvailability.getInvolvedAvailabiltyForTransportDate().getNeedsTransport() == 1);

                                        // info for the passenger notification icon
                                        if (passengerHasOneOrMoreNotifications && passengerHasOneOrMoreTransports) {
                                                notificationIconDisplay = PassengerNotificationIconCalculator.calculateDatePassengerNotifIcon(
                                                        allPassengerNotifications.get(passenger.getId()).get(dateId),
                                                        allPassengerTransports.get(passenger.getId()).get(dateId));

                                        } else if (passengerHasOneOrMoreNotifications || passengerHasOneOrMoreTransports) {
                                                notificationIconDisplay = new VoTCVNotificationIconDisplay(true, "red");
                                        }
                                
                                        // if the passenger does not assist on that date or does not need transport, no icon is shown
                                        notificationIconDisplay.setShowIcon(
                                                screenPassenger.isAssistsOnDate() && screenPassenger.isNeedsTransport());
                                        
                                        // increase the red notification icons count for the general notif icon
                                        if (notificationIconDisplay.isShowIcon() && "red".equalsIgnoreCase(notificationIconDisplay.getIconColor())) {
                                            redShownNotifIcons++;    
                                        }

                                        // info for the passenger transport
                                        final boolean passengerHasTransportOnDate = 
                                                (allPassengerTransports.get(passenger.getId()) != null
                                                 && allPassengerTransports.get(passenger.getId()).get(dateId) != null);

                                        if (passengerHasTransportOnDate) {

                                                passengerTransportDisplay.setSelectedDriverId(
                                                        allPassengerTransports.get(passenger.getId()).get(dateId).getTransport().getTransportKey().getDriverId());
                                                
                                                screenPassenger.setAssignedDriverName(
                                                        allPassengerTransports.get(passenger.getId()).get(dateId).getDriver().getName());
                                        }

                                        final boolean passengerHasNotificationOnDate = 
                                                (passengerHasOneOrMoreNotifications && allPassengerNotifications.get(passenger.getId()).get(dateId) != null);

                                        if (passengerHasNotificationOnDate) {
                                                screenPassenger.setNotifiedDriverName(
                                                        allPassengerNotifications.get(passenger.getId()).get(dateId).getDriver().getName());
                                        }

                                }

                                screenPassenger.setVoNotificationIconDisplay(notificationIconDisplay);
                                screenPassenger.setVoPassengerTransportDisplay(passengerTransportDisplay);
                                voTransportCrudScreenPassenger.addPassengerInfo(dateId, screenPassenger);
                        
                        }
                
                        if (redShownNotifIcons > 1) {
                                voGeneralPassengerIcon.setVoNotificationIconDisplay(new VoTCVNotificationIconDisplay(true, "red"));
                        } else {
                                voGeneralPassengerIcon.setVoNotificationIconDisplay(new VoTCVNotificationIconDisplay(false, "red"));
                        }

                        // add passenger info to the list
                        listVoTransportCrudScreenPassenger.add(voTransportCrudScreenPassenger);
                }

                return listVoTransportCrudScreenPassenger;
        }
}
