package com.transports.spring.repository;

import com.transports.spring.model.InvolvedTransportNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.util.List;

public interface IInvolvedTransportNotificationRepository extends JpaRepository<InvolvedTransportNotification, UUID> {

    @Query("SELECT " +
            "   api.notifiedInvolvedId, " +
            "   api.transportDateCode, " +
            "   concat(ippdriver.name, ' ', ippdriver.surname)" +
            "FROM InvolvedTransportNotification api " +
            "   INNER JOIN TransportDateByTemplate ftpp " +
            "       ON ftpp.id = api.transportDateCode" +
            "   INNER JOIN InvolvedByTemplate ipp" +
            "       ON ipp.involvedByTemplateKey.involvedCode = api.notifiedInvolvedId" +
            "   INNER JOIN InvolvedByTemplate ippdriver" +
            "       ON ippdriver.involvedByTemplateKey.involvedCode = api.driverCode" +
            "   WHERE " +
            "       ftpp.templateCode = :templateId" +
            "       AND ipp.roleCode = 1")
    List<Object[]> getAllPassengerNotificationsForTemplate(@Param("templateId") String templateId);

    @Query("SELECT " +
            "   api.notifiedInvolvedId, " +
            "   api.transportDateCode, " +
            "   concat(ipppassenger.name, ' ', ipppassenger.surname)" +
            "FROM InvolvedTransportNotification api " +
            "   INNER JOIN TransportDateByTemplate ftpp " +
            "       ON ftpp.id = api.transportDateCode" +
            "   INNER JOIN InvolvedByTemplate ipp" +
            "       ON ipp.involvedByTemplateKey.involvedCode = api.notifiedInvolvedId" +
            "   INNER JOIN InvolvedByTemplate ipppassenger" +
            "       ON ipppassenger.involvedByTemplateKey.involvedCode = api.passengerCode" +
            "   WHERE " +
            "       ftpp.templateCode = :templateId" +
            "       AND ipp.roleCode = 2")
    List<Object[]> getAllDriverNotificationsForTemplate(@Param("templateId") String templateId);

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
            "   t.transportKey.transportDateId, " +
            "   t.transportKey.driverId, " +
            "   CASE " +
            "       WHEN apiDriver.id IS NOT NULL THEN true " +
            "       ELSE false " +
            "   END " +
            "      FROM Transport t " +
            "           LEFT JOIN InvolvedTransportNotification apiDriver " +
            "               ON apiDriver.transportDateCode = t.transportKey.transportDateId " +
            "               AND apiDriver.passengerCode = t.transportKey.passengerId" +
            "               AND apiDriver.driverCode = t.transportKey.driverId" +
            "               AND apiDriver.notifiedInvolvedId = t.transportKey.driverId " +
            "           INNER JOIN TransportDateByTemplate td " +
            "               ON td.id = t.transportKey.transportDateId " +
            "       WHERE td.templateCode = :templateId")
    List<Object[]> getDriverNotificationsByTemplate(@Param("templateId") Integer templateId);

    @Query("SELECT DISTINCT" +
            "   t.transportKey.transportDateId, " +
            "   t.transportKey.passengerId, " +
            "   CASE " +
            "       WHEN apiPassenger.id IS NOT NULL THEN true " +
            "       ELSE false " +
            "   END" +
            "      FROM Transport t" +
            "           LEFT JOIN InvolvedTransportNotification apiPassenger" +
            "               ON apiPassenger.transportDateCode = t.transportKey.transportDateId" +
        "                   AND apiPassenger.driverCode = t.transportKey.driverId" +
            "               AND apiPassenger.passengerCode = t.transportKey.passengerId" +
            "               AND apiPassenger.notifiedInvolvedId = t.transportKey.passengerId" +
            "           INNER JOIN TransportDateByTemplate td " +
            "               ON td.id = t.transportKey.transportDateId" +
            "       WHERE td.templateCode = :templateId")
    List<Object[]> getPassengerNotificationsByTemplate(@Param("templateId") Integer templateId);
}
