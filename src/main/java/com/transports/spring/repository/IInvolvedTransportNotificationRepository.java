package com.transports.spring.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.vo.completemodel.VoCompleteNotification;

public interface IInvolvedTransportNotificationRepository extends JpaRepository<InvolvedTransportNotification, UUID> {

        @Query("SELECT " +
            "   new com.transports.spring.vo.completemodel.VoCompleteNotification(" +
            "       new Driver(driver.involvedByTemplateKey.involvedCode, driver.name, driver.surname, true, driver.roleCode, 0, null, driver.seats, driver.ministryGroup)," +
            "       new Passenger(passenger.involvedByTemplateKey.involvedCode, passenger.name, passenger.surname, true, passenger.roleCode, 0, null, passenger.seats, passenger.ministryGroup)," +
            "       new InvolvedTransportNotification(api.id, api.notifiedInvolvedId, api.transportDateCode, api.driverCode, api.passengerCode, api.notificationDate)," +
            "       new TransportDateByTemplate(ftpp.id, ftpp.templateCode, ftpp.transportDate, ftpp.dayOfTheWeekCode, ftpp.eventName)" +
            "   )" +
            "FROM InvolvedTransportNotification api " +
            "   INNER JOIN TransportDateByTemplate ftpp " +
            "       ON ftpp.id = api.transportDateCode" +
            "   INNER JOIN InvolvedByTemplate passenger" +
            "       ON passenger.involvedByTemplateKey.involvedCode = api.notifiedInvolvedId" +
            "   INNER JOIN InvolvedByTemplate driver" +
            "       ON driver.involvedByTemplateKey.involvedCode = api.driverCode" +
            "   WHERE " +
            "       ftpp.templateCode = :templateId" +
            "       AND passenger.roleCode = 1")
    List<VoCompleteNotification> getAllPassengerNotificationsForTemplate(@Param("templateId") String templateId);

    @Query("SELECT DISTINCT " +
            "   new com.transports.spring.vo.completemodel.VoCompleteNotification(" +
            "       new Driver(driver.involvedByTemplateKey.involvedCode, driver.name, driver.surname, true, driver.roleCode, 0, null, driver.seats, driver.ministryGroup)," +
            "       new Passenger(passenger.involvedByTemplateKey.involvedCode, passenger.name, passenger.surname, true, passenger.roleCode, 0, null, passenger.seats, passenger.ministryGroup)," +
            "       new InvolvedTransportNotification(api.id, api.notifiedInvolvedId, api.transportDateCode, api.driverCode, api.passengerCode, api.notificationDate)," +
            "       new TransportDateByTemplate(ftpp.id, ftpp.templateCode, ftpp.transportDate, ftpp.dayOfTheWeekCode, ftpp.eventName)" +
            "   )" +
            "FROM InvolvedTransportNotification api " +
            "   INNER JOIN TransportDateByTemplate ftpp " +
            "       ON ftpp.id = api.transportDateCode" +
            "   INNER JOIN InvolvedByTemplate driver" +
            "       ON driver.involvedByTemplateKey.involvedCode = api.driverCode" +
            "   INNER JOIN InvolvedByTemplate passenger" +
            "       ON passenger.involvedByTemplateKey.involvedCode = api.passengerCode" +
            "   WHERE " +
            "       ftpp.templateCode = :templateId" +
            "       AND driver.roleCode = 2")
    List<VoCompleteNotification> getAllDriverNotificationsForTemplate(@Param("templateId") String templateId);

    @Query("SELECT new InvolvedTransportNotification(api.id, api.notifiedInvolvedId, api.transportDateCode, api.driverCode, api.passengerCode, api.notificationDate) " +
            "FROM InvolvedTransportNotification api " +
            "WHERE " +
            "   api.transportDateCode = :transportDate" +
            "   AND api.notifiedInvolvedId = :involvedId")
    List<InvolvedTransportNotification> getNotificationForInvolvedInDate(@Param("transportDate") String transportDate, @Param("involvedId") String involvedId);


    @Modifying
    @Query("UPDATE InvolvedTransportNotification api" +
            "   SET " +
            "       api.driverCode = :newDriverCode" +
            "   WHERE " +
            "       api.notifiedInvolvedId = :notifiedInvolvedId" +
            "       AND api.transportDateCode = :transportDateCode" +
            "       AND api.passengerCode = :passengerCode")
    void updateDriver(@Param("transportDateCode") Integer transportDateCode,
                      @Param("notifiedInvolvedId") Integer notifiedInvolvedId,
                      @Param("newDriverCode") Integer newDriverCode,
                      @Param("passengerCode") Integer passengerCode
    );

    @Modifying
    @Query("DELETE FROM InvolvedTransportNotification api" +
            "   WHERE " +
            "       api.transportDateCode = :transportDateId" +
            "       AND api.notifiedInvolvedId = :involvedId")
    void deleteNotificationsForInvolvedInDate(@Param("involvedId") Integer involvedId, @Param("transportDateId") Integer transportDateId);

    @Query("SELECT DISTINCT" +
            " new com.transports.spring.vo.completemodel.VoCompleteNotification(" +
            "       new Driver(api.driverCode, null)," +
            "       new Passenger(api.passengerCode, null)," +
            "       new InvolvedTransportNotification(api.id, api.notifiedInvolvedId, api.transportDateCode, api.driverCode, api.passengerCode, api.notificationDate)," +
            "       new TransportDateByTemplate(ftpp.id, ftpp.templateCode, ftpp.transportDate, ftpp.dayOfTheWeekCode, ftpp.eventName)" +
            "   )" + 
            "      FROM Transport t " +
            "           INNER JOIN InvolvedTransportNotification api " +
            "               ON api.transportDateCode = t.transportKey.transportDateId" +
            "               AND api.passengerCode = t.transportKey.passengerId" +
            "               AND api.driverCode = t.transportKey.driverId" +
            "               AND api.notifiedInvolvedId = t.transportKey.driverId " +
            "           INNER JOIN TransportDateByTemplate ftpp " +
            "               ON ftpp.id = api.transportDateCode " +
            "       WHERE " +
            "           ftpp.templateCode = :templateId" + 
            "                   AND api.driverCode = :driverCode")
    List<VoCompleteNotification> getDriverNotificationsByTemplate(@Param("templateId") Integer templateId, @Param("driverCode") Integer driverId);

    @Query("SELECT DISTINCT" +
            " new com.transports.spring.vo.completemodel.VoCompleteNotification(" +
            "       new Driver(api.driverCode, null)," +
            "       new Passenger(api.passengerCode, null)," +
            "       new InvolvedTransportNotification(api.id, api.notifiedInvolvedId, api.transportDateCode, api.driverCode, api.passengerCode, api.notificationDate)," +
            "       new TransportDateByTemplate(ftpp.id, ftpp.templateCode, ftpp.transportDate, ftpp.dayOfTheWeekCode, ftpp.eventName)" +
            "   )" + 
            "      FROM Transport t" +
            "           INNER JOIN InvolvedTransportNotification api" +
            "               ON api.transportDateCode = t.transportKey.transportDateId" +
            "               AND api.driverCode = t.transportKey.driverId" +
            "               AND api.passengerCode = t.transportKey.passengerId" +
            "               AND api.notifiedInvolvedId = t.transportKey.passengerId" +
            "           INNER JOIN TransportDateByTemplate ftpp " +
            "               ON ftpp.id = api.transportDateCode" +
            "       WHERE ftpp.templateCode = :templateId" + 
            "                   AND api.passengerCode = :passengerCode")
    List<VoCompleteNotification> getPassengerNotificationsByTemplate(@Param("templateId") Integer templateId, @Param("passengerCode") Integer passengerId);

    @Query("SELECT DISTINCT" +
            "       new InvolvedTransportNotification(api.id, api.notifiedInvolvedId, api.transportDateCode, api.driverCode, api.passengerCode, api.notificationDate) " +
            "           FROM Transport t" +
            "               RIGHT OUTER JOIN InvolvedTransportNotification api" +
            "                   ON t.transportKey.transportDateId = api.transportDateCode" +
            "                   AND t.transportKey.passengerId = api.notifiedInvolvedId" +
            "                   AND t.transportKey.driverId = api.driverCode" +
            "               INNER JOIN TransportDateByTemplate ftpp" +
            "                   ON api.transportDateCode = ftpp.id" +
            "               INNER JOIN Template p" +
            "                   ON ftpp.templateCode = p.ID" +
            "           WHERE" +
            "               p.id = :templateId" +
            "               AND api.notifiedInvolvedId = :passengerId" +
            "               AND t.transportKey.transportDateId IS NULL")
    List<InvolvedTransportNotification> getPassengerNotificationsWithoutTransport(@Param("templateId") Integer templateId, @Param("passengerId") Integer passengerId);

    @Query("SELECT DISTINCT" +
            "       new InvolvedTransportNotification(api.id, api.notifiedInvolvedId, api.transportDateCode, api.driverCode, api.passengerCode, api.notificationDate) " +
            "           FROM Transport t" +
            "               RIGHT OUTER JOIN InvolvedTransportNotification api" +
            "                   ON t.transportKey.transportDateId = api.transportDateCode" +
            "                   AND t.transportKey.passengerId = api.passengerCode" +
            "                   AND t.transportKey.driverId = api.notifiedInvolvedId" +
            "               INNER JOIN TransportDateByTemplate ftpp" +
            "                   ON api.transportDateCode = ftpp.id" +
            "               INNER JOIN Template p" +
            "                   ON ftpp.templateCode = p.ID" +
            "           WHERE" +
            "               p.id = :templateId" +
            "               AND api.notifiedInvolvedId = :driverId" +
            "               AND t.transportKey.transportDateId IS NULL")
    List<InvolvedTransportNotification> getDriverNotificationsWithoutTransport(@Param("templateId") Integer templateId, @Param("driverId") Integer driverId);
}
