package com.transports.spring.repository;

import com.transports.spring.model.CommunicationForInvolved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.util.List;

public interface ICommunicationForInvolvedRepository extends JpaRepository<CommunicationForInvolved, UUID> {

    @Query("SELECT " +
            "   api.involvedCommunicatedId, " +
            "   api.transportDateCode, " +
            "   concat(ippdriver.name, ' ', ippdriver.surname)" +
            "FROM CommunicationForInvolved api " +
            "   INNER JOIN TransportDateByTemplate ftpp " +
            "       ON ftpp.id = api.transportDateCode" +
            "   INNER JOIN InvolvedByTemplate ipp" +
            "       ON ipp.involvedByTemplateKey.involvedCode = api.involvedCommunicatedId" +
            "   INNER JOIN InvolvedByTemplate ippdriver" +
            "       ON ippdriver.involvedByTemplateKey.involvedCode = api.driverCode" +
            "   WHERE " +
            "       ftpp.templateCode = :templateId" +
            "       AND ipp.roleCode = 1")
    List<Object[]> getAllPassengerCommunicationsForTemplate(@Param("templateId") String templateId);

    @Query("SELECT " +
            "   api.involvedCommunicatedId, " +
            "   api.transportDateCode, " +
            "   concat(ipppassenger.name, ' ', ipppassenger.surname)" +
            "FROM CommunicationForInvolved api " +
            "   INNER JOIN TransportDateByTemplate ftpp " +
            "       ON ftpp.id = api.transportDateCode" +
            "   INNER JOIN InvolvedByTemplate ipp" +
            "       ON ipp.involvedByTemplateKey.involvedCode = api.involvedCommunicatedId" +
            "   INNER JOIN InvolvedByTemplate ipppassenger" +
            "       ON ipppassenger.involvedByTemplateKey.involvedCode = api.passengerCode" +
            "   WHERE " +
            "       ftpp.templateCode = :templateId" +
            "       AND ipp.roleCode = 2")
    List<Object[]> getAllDriverCommunicationsForTemplate(@Param("templateId") String templateId);

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

    @Query("SELECT DISTINCT" +
            "   t.transportKey.transportDateId, " +
            "   t.transportKey.driverId, " +
            "   CASE " +
            "       WHEN apiDriver.id IS NOT NULL THEN true " +
            "       ELSE false " +
            "   END " +
            "      FROM Transport t " +
            "           LEFT JOIN CommunicationForInvolved apiDriver " +
            "               ON (apiDriver.transportDateCode = t.transportKey.transportDateId " +
            "               AND apiDriver.involvedCommunicatedId = t.transportKey.driverId) " +
            "           JOIN TransportDateByTemplate td " +
            "               ON td.id = t.transportKey.transportDateId " +
            "       WHERE td.templateCode = :templateId")
    List<Object[]> getDriverCommunicationsByTemplate(@Param("templateId") Integer templateId);

    @Query("SELECT DISTINCT" +
            "   t.transportKey.transportDateId, " +
            "   t.transportKey.passengerId, " +
            "   CASE " +
            "       WHEN apiPassenger.id IS NOT NULL THEN true " +
            "       ELSE false " +
            "   END" +
            "      FROM Transport t" +
            "           LEFT JOIN CommunicationForInvolved apiPassenger" +
            "               ON (apiPassenger.transportDateCode = t.transportKey.transportDateId" +
            "               AND apiPassenger.involvedCommunicatedId = t.transportKey.passengerId)" +
            "           JOIN TransportDateByTemplate td " +
            "               ON td.id = t.transportKey.transportDateId" +
            "       WHERE td.templateCode = :templateId")
    List<Object[]> getPassengerCommunicationsByTemplate(@Param("templateId") Integer templateId);
}
