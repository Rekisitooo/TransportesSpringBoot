package com.transports.spring.repository;

import com.transports.spring.model.NotificationForInvolved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.util.List;

public interface INotificationForInvolvedRepository extends JpaRepository<NotificationForInvolved, UUID> {

    @Query("SELECT " +
            "   api.notifiedInvolvedId, " +
            "   api.transportDateCode, " +
            "   concat(ippdriver.name, ' ', ippdriver.surname)" +
            "FROM NotificationForInvolved api " +
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
            "FROM NotificationForInvolved api " +
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

    @Query("SELECT new NotificationForInvolved(api.id, api.notifiedInvolvedId, api.transportDateCode, api.driverCode, api.passengerCode, api.notificationDate) " +
            "FROM NotificationForInvolved api " +
            "WHERE " +
            "   api.transportDateCode = :transportDate" +
            "   AND api.notifiedInvolvedId = :involvedId")
    List<NotificationForInvolved> getNotificationForInvolvedInDate(@Param("transportDate") String transportDate, @Param("involvedId") String involvedId);


    @Modifying
    @Query("UPDATE NotificationForInvolved api" +
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
    @Query("DELETE FROM NotificationForInvolved api" +
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
            "           LEFT JOIN NotificationForInvolved apiDriver " +
            "               ON (apiDriver.transportDateCode = t.transportKey.transportDateId " +
            "               AND apiDriver.notifiedInvolvedId = t.transportKey.driverId) " +
            "           JOIN TransportDateByTemplate td " +
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
            "           LEFT JOIN NotificationForInvolved apiPassenger" +
            "               ON (apiPassenger.transportDateCode = t.transportKey.transportDateId" +
            "               AND apiPassenger.notifiedInvolvedId = t.transportKey.passengerId)" +
            "           JOIN TransportDateByTemplate td " +
            "               ON td.id = t.transportKey.transportDateId" +
            "       WHERE td.templateCode = :templateId")
    List<Object[]> getPassengerNotificationsByTemplate(@Param("templateId") Integer templateId);
}
