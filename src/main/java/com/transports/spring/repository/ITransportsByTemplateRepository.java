package com.transports.spring.repository;

import com.transports.spring.model.TransportByTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransportsByTemplateRepository extends JpaRepository<TransportByTemplate, Integer> {

    @Query("SELECT " +
            "   new TransportByTemplate(viajero.id, conductor.id, ftpp.id)" +
            "       FROM TransportByTemplate tpp" +
            "           INNER JOIN TransportDateByTemplate ftpp" +
            "               ON tpp.transportByTemplateKey.transportDateId = ftpp.id" +
            "           INNER JOIN AbstractInvolved viajero" +
            "               ON tpp.transportByTemplateKey.passengerId = viajero.id" +
            "           INNER JOIN AbstractInvolved conductor" +
            "               ON tpp.transportByTemplateKey.driverId = conductor.id" +
            "       WHERE " +
            "           tpp.transportByTemplateKey.passengerId = :passengerId" +
            "           AND ftpp.templateCode = :templateId")
    List<TransportByTemplate> findAllPassengerTransportsFromTemplate(@Param("passengerId") int passengerId, @Param("templateId") int templateId);

    @Query("SELECT " +
            "   new TransportByTemplate(viajero.id, conductor.id, ftpp.id)" +
            "       FROM TransportByTemplate tpp" +
            "           INNER JOIN TransportDateByTemplate ftpp" +
            "               ON tpp.transportByTemplateKey.transportDateId = ftpp.id" +
            "           INNER JOIN AbstractInvolved viajero" +
            "               ON tpp.transportByTemplateKey.passengerId = viajero.id" +
            "           INNER JOIN AbstractInvolved conductor" +
            "               ON tpp.transportByTemplateKey.driverId = conductor.id" +
            "       WHERE " +
            "           tpp.transportByTemplateKey.driverId = :driverId" +
            "           AND ftpp.templateCode = :templateId")
    List<TransportByTemplate> findAllDriverTransportsFromTemplate(@Param("driverId") int driverId, @Param("templateId") int templateId);
}
