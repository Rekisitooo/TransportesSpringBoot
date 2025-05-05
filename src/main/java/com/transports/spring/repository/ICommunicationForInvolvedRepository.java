package com.transports.spring.repository;

import com.transports.spring.model.CommunicationForInvolved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICommunicationForInvolvedRepository extends JpaRepository<CommunicationForInvolved, Integer> {

    @Query("SELECT new CommunicationForInvolved(api.id, api.involvedCommunicatedId, api.transportDateCode, api.driverCode, api.passengerCode, api.communicationDate) " +
            "FROM CommunicationForInvolved api " +
            "   INNER JOIN TransportDateByTemplate ftpp " +
            "       ON ftpp.id = api.transportDateCode" +
            "   WHERE ftpp.templateCode = :templateId")
    List<CommunicationForInvolved> getAllCommunicationsForTemplate(@Param("templateId") Integer templateId);

    @Query("SELECT new CommunicationForInvolved(api.id, api.involvedCommunicatedId, api.transportDateCode, api.driverCode, api.passengerCode, api.communicationDate) " +
            "FROM CommunicationForInvolved api " +
            "WHERE " +
            "   api.transportDateCode = :transportDate" +
            "   AND api.involvedCommunicatedId = :involvedId")
    List<CommunicationForInvolved> getCommunicationForInvolvedInDate(@Param("transportDate") Integer transportDate, @Param("involvedId") Integer involvedId);


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
}
