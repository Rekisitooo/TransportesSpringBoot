package com.transports.spring.operation.notificationview.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.dto.notificationview.driver.DtoDriverNotificationVTableInfo;
import com.transports.spring.model.Driver;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.notificationview.driver.VoNotifVDriver;
import com.transports.spring.vo.notificationview.driver.VoNotifVDriverDateInfo;

public class NotificationVDriverDataProvider {

        /**
         * Builds the driver notification view table info
         * 
         * @param dtoDriverNotificationVTableInfo
         * @return List of VoNotifVDriver
         */
        public static List<VoNotifVDriver> getScreenDriverNotificationsTableInfo(final DtoDriverNotificationVTableInfo dtoDriverNotificationVTableInfo) {
                final List<VoNotifVDriver> voNotifVDriverList = new ArrayList<>();
                final List<Driver> templateDriverList = dtoDriverNotificationVTableInfo.getTemplateDriverList();
                final List<DtoTemplateDate> templateDateList = dtoDriverNotificationVTableInfo.getTemplateDateList();
                final Map<Integer, Map<Integer, List<VoCompleteNotification>>> driverNotifications = dtoDriverNotificationVTableInfo.getDriverNotifications();

                for (final Driver driver : templateDriverList) {

                        final VoNotifVDriver voNotifVDriver = new VoNotifVDriver();
                        voNotifVDriver.setDriver(driver);

                        final Map<Integer, List<VoCompleteNotification>> notificationsByDate = driverNotifications.get(driver.getId());

                        for (final DtoTemplateDate date : templateDateList) {
                                final VoNotifVDriverDateInfo dateInfo = new VoNotifVDriverDateInfo();

                                // there can be no notifications for this driver
                                if (notificationsByDate != null && notificationsByDate.get(date.getId()) != null) {
                                        final List<VoCompleteNotification> notifications = notificationsByDate.get(date.getId());
                                        int emptyPassengerNotifNames = 0;
                                        for (final VoCompleteNotification notification : notifications) {
                                                if (notification.getPassenger().getFullName().isBlank()) {
                                                        emptyPassengerNotifNames++;
                                                } else {
                                                        dateInfo.addNotifiedPassengerName(notification.getPassenger().getFullName()); 
                                                }
                                        }

                                        // set null to show 'No passengers assigned'
                                        if (emptyPassengerNotifNames == notifications.size()) {
                                                dateInfo.setNotifiedPassengerNameList(null);
                                        }
                                }

                                voNotifVDriver.addDriverInfo(date.getId(), dateInfo);

                        }

                        voNotifVDriverList.add(voNotifVDriver);
                }

                return voNotifVDriverList;
        }
}
