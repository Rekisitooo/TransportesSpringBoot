package com.transports.spring.repository;

import com.transports.spring.model.AbstractInvolved;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInvolvedByTemplateRepository extends JpaRepository<AbstractInvolved, Integer> {

    @Query("SELECT " +
            "   new Passenger(i.id, i.name, i.surname, i.isActive, i.roleCode, i.userCode, i.userCodeGroup, i.occupiedSeats)" +
            "       FROM InvolvedByTemplate ipp" +
            "           INNER JOIN AbstractInvolved i " +
            "               ON i.id = ipp.involvedByTemplateKey.involvedCode " +
            "           INNER JOIN InvolvedRole rol " +
            "               ON i.roleCode = rol.id" +
            "       WHERE " +
            "           rol.description = 'Viajero' " +
            "           AND ipp.involvedByTemplateKey.templateCode = :templateCode")
    List<Passenger> getAllPassengersFromTemplate(@Param("templateCode") final int templateId);

    @Query("SELECT " +
            "   new Driver(i.id, i.name, i.surname, i.isActive, i.roleCode, i.userCode, i.userCodeGroup, i.availableSeats)" +
            "       FROM InvolvedByTemplate ipp" +
            "           INNER JOIN AbstractInvolved i " +
            "               ON i.id = ipp.involvedByTemplateKey.involvedCode " +
            "           INNER JOIN InvolvedRole rol " +
            "               ON i.roleCode = rol.id" +
            "       WHERE " +
            "           rol.description = 'Conductor' " +
            "           AND ipp.involvedByTemplateKey.templateCode = :templateCode")
    List<Driver> getAllDriversFromTemplate(@Param("templateCode") final int templateId);
}

