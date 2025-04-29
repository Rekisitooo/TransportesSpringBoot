package com.transports.spring.repository;

import com.transports.spring.model.CommunicationForInvolved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICommunicationForInvolvedRepository extends JpaRepository<CommunicationForInvolved, Integer> {

    @Query("SELECT new CommunicationForInvolved(api.key.involvedCommunicated, api.key.transportDateCode, api.driverCode, api.passengerCode, api.communicationDate) " +
            "FROM CommunicationForInvolved api " +
            "   INNER JOIN TransportDateByTemplate ftpp " +
            "       ON ftpp.id = api.key.transportDateCode" +
            "WHERE ftpp.templateCode = :templateId")
    List<CommunicationForInvolved> getAllCommunicationsForTemplate(@Param("templateId") Integer templateId);

    @Query("SELECT new CommunicationForInvolved(api.key.involvedCommunicated, api.key.transportDateCode, api.driverCode, api.passengerCode, api.communicationDate) " +
            "FROM CommunicationForInvolved api " +
            "WHERE api.key.transportDateCode = :transportDate")
    CommunicationForInvolved getCommunicationForInvolvedInDate(@Param("transportDate") Integer transportDate, @Param("involvedId") Integer involvedId);

    @Modifying
    @Query("UPDATE CommunicationForInvolved api" +
            "SET api.driverCode = :newDriverCode" +
            "WHERE " +
            "   api.key.communicatedInvolved = :communicatedInvolved" +
            "   AND api.key.transportDateCode = :transportDateCode" +
            "   AND api.passengerCode = :passengerCode")
    void updateDriver(@Param("transportDateCode") Integer transportDateCode,
                      @Param("communicatedInvolved") Integer communicatedInvolved,
                      @Param("newDriverCode") Integer newDriverCode,
                      @Param("passengerCode") Integer passengerCode
    );
}
