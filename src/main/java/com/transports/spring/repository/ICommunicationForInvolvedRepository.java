package com.transports.spring.repository;

import com.transports.spring.model.CommunicationForInvolved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.util.List;

public interface ICommunicationForInvolvedRepository extends JpaRepository<CommunicationForInvolved, UUID> {

    @Query("SELECT new CommunicationForInvolved(api.id, api.involvedCommunicatedId, api.transportDateCode, api.driverCode, api.passengerCode, api.communicationDate) " +
            "FROM CommunicationForInvolved api " +
            "   INNER JOIN TransportDateByTemplate ftpp " +
            "       ON ftpp.id = api.transportDateCode" +
            "   WHERE ftpp.templateCode = :templateId")
    List<CommunicationForInvolved> getAllCommunicationsForTemplate(@Param("templateId") String templateId);

    @Query("SELECT new CommunicationForInvolved(api.id, api.involvedCommunicatedId, api.transportDateCode, api.driverCode, api.passengerCode, api.communicationDate) " +
            "FROM CommunicationForInvolved api " +
            "WHERE " +
            "   api.transportDateCode = :transportDate" +
            "   AND api.involvedCommunicatedId = :involvedId")
    List<CommunicationForInvolved> getCommunicationForInvolvedInDate(@Param("transportDate") String transportDate, @Param("involvedId") String involvedId);


    @Modifying
    @Query("UPDATE CommunicationForInvolved api" +
            "   SET " +
            "       api.driverCode = :newDriverCode" +
            "   WHERE " +
            "       api.involvedCommunicatedId = :involvedCommunicatedId" +
            "       AND api.transportDateCode = :transportDateCode" +
            "       AND api.passengerCode = :passengerCode")
    void updateDriver(@Param("transportDateCode") Integer transportDateCode,
                      @Param("involvedCommunicatedId") Integer involvedCommunicatedId,
                      @Param("newDriverCode") Integer newDriverCode,
                      @Param("passengerCode") Integer passengerCode
    );

    @Modifying
    @Query("DELETE FROM CommunicationForInvolved api" +
            "   WHERE " +
            "       api.transportDateCode = :transportDateId" +
            "       AND api.involvedCommunicatedId = :involvedId")
    void deleteCommunicationsForInvolvedInDate(@Param("involvedId") Integer involvedId, @Param("transportDateId") Integer transportDateId);

    @Query("SELECT " +
            "   t.transportKey.transportDateId, " +
            "   t.transportKey.driverId, " +
            "   CASE " +
            "       WHEN apiDriver.id IS NOT NULL THEN true " +
            "       ELSE false END " +
            "      FROM Transport t " +
            "           LEFT JOIN CommunicationForInvolved apiDriver " +
            "               ON (apiDriver.transportDateCode = t.transportKey.transportDateId " +
            "               AND apiDriver.involvedCommunicatedId = t.transportKey.driverId) " +
            "           JOIN TransportDateByTemplate td " +
            "               ON td.id = t.transportKey.transportDateId " +
            "       WHERE td.id = :templateId")
    List<Object[]> findDriverNotificationsByTemplate(@Param("templateId") Integer templateId);

    @Query("SELECT " +
            "   t.transportKey.transportDateId, " +
            "   t.transportKey.passengerId, " +
            "   CASE " +
            "       WHEN apiPassenger.id IS NOT NULL THEN true " +
            "       ELSE false END" +
            "      FROM Transport t" +
            "           LEFT JOIN CommunicationForInvolved apiPassenger" +
            "               ON (apiPassenger.transportDateCode = t.transportKey.transportDateId" +
            "               AND apiPassenger.involvedCommunicatedId = t.transportKey.passengerId)" +
            "           JOIN TransportDateByTemplate td " +
            "               ON td.id = t.transportKey.transportDateId" +
            "       WHERE td.id = :templateId")
    List<Object[]> findPassengerNotificationsByTemplate(@Param("templateId") Integer templateId);
}
