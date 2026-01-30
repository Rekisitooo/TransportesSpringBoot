package com.transports.spring.operation.notificationview.passenger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.dto.notificationview.passenger.DtoPassengerNotificationVTableInfo;
import com.transports.spring.model.Passenger;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.notificationview.passenger.VoNotifVPassenger;
import com.transports.spring.vo.notificationview.passenger.VoNotifVPassengerDateInfo;

public class NotificationVPassengerDataProvider {

        /**
         * Builds the passenger notification view table info
         * 
         * @param dtoPassengerNotificationVTableInfo
         * @return List of VoNotifVPassenger
         */
        public static List<VoNotifVPassenger> getScreenPassengerNotificationsTableInfo(final DtoPassengerNotificationVTableInfo dtoPassengerNotificationVTableInfo) {
                final List<VoNotifVPassenger> voNotifVPassengerList = new ArrayList<>();
                final List<Passenger> templatePassengerList = dtoPassengerNotificationVTableInfo.getTemplatePassengerList();
                final List<DtoTemplateDate> templateDateList = dtoPassengerNotificationVTableInfo.getTemplateDateList();
                final Map<Integer, Map<Integer, VoCompleteNotification>> passengerNotifications = dtoPassengerNotificationVTableInfo.getPassengerNotifications();

                for (final Passenger passenger : templatePassengerList) {

                        final VoNotifVPassenger voNotifVPassenger = new VoNotifVPassenger();
                        voNotifVPassenger.setPassenger(passenger);

                        final Map<Integer, VoCompleteNotification> notificationsByDate = passengerNotifications.get(passenger.getId());

                        for (final DtoTemplateDate date : templateDateList) {
                                final VoNotifVPassengerDateInfo dateInfo = new VoNotifVPassengerDateInfo();

                                // there can be no notifications for this passenger
                                if (notificationsByDate != null) {
                                        final VoCompleteNotification notification = notificationsByDate.get(date.getId());
                                        if (notification != null) {
                                                // setNotifiedDriverName to "" to show "No driver assigned"
                                                if (notification.getDriver() != null) {
                                                        dateInfo.setNotifiedDriverName(notification.getDriver().getFullName());
                                                } else {
                                                        dateInfo.setNotifiedDriverName("");
                                                }
                                        }
                                }

                                voNotifVPassenger.addPassengerInfo(date.getId(), dateInfo);

                        }

                        voNotifVPassengerList.add(voNotifVPassenger);
                }

                return voNotifVPassengerList;
        }
}
