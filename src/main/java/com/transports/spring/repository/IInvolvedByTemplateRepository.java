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
            "   new Passenger(ipp.involvedByTemplateKey.involvedCode, ipp.name, ipp.surname, ipp.roleCode, ipp.seats)" +
            "       FROM InvolvedByTemplate ipp" +
            "           INNER JOIN InvolvedRole rol " +
            "               ON ipp.roleCode = rol.id" +
            "       WHERE " +
            "           rol.description = 'Viajero' " +
            "           AND ipp.involvedByTemplateKey.templateCode = :templateCode")
    List<Passenger> getAllPassengersFromTemplate(@Param("templateCode") final int templateId);

    @Query("SELECT " +
            "   new Driver(ipp.involvedByTemplateKey.involvedCode, ipp.name, ipp.surname, ipp.roleCode, ipp.seats)" +
            "       FROM InvolvedByTemplate ipp" +
            "           INNER JOIN InvolvedRole rol " +
            "               ON ipp.roleCode = rol.id" +
            "       WHERE " +
            "           rol.description = 'Conductor' " +
            "           AND ipp.involvedByTemplateKey.templateCode = :templateCode")
    List<Driver> getAllDriversFromTemplate(@Param("templateCode") final int templateId);

    @Query("SELECT " +
            "   new Passenger(ipp.involvedByTemplateKey.involvedCode, ipp.name, ipp.surname, ipp.roleCode, ipp.seats)" +
            "       FROM InvolvedByTemplate ipp" +
            "       WHERE" +
            "           ipp.involvedByTemplateKey.templateCode = :templateCode" +
            "           AND ipp.involvedByTemplateKey.involvedCode = :involvedCode")
    Passenger getByIdAndTemplate(@Param("involvedCode") int involvedId, @Param("templateCode") final int templateId);
}

