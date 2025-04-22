package com.transports.spring.repository;

import com.transports.spring.dto.DtoInvolvedTransport;
import com.transports.spring.model.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransportRepository extends JpaRepository<Transport, Integer> {

    @Query("SELECT " +
            "   new Transport(viajero.id, conductor.id, ftpp.id)" +
            "       FROM Transport t" +
            "           INNER JOIN TransportDateByTemplate ftpp" +
            "               ON t.transportKey.transportDateId = ftpp.id" +
            "           INNER JOIN Involved viajero" +
            "               ON t.transportKey.passengerId = viajero.id" +
            "           INNER JOIN Involved conductor" +
            "               ON t.transportKey.driverId = conductor.id" +
            "       WHERE " +
            "           t.transportKey.passengerId = :passengerId" +
            "           AND ftpp.templateCode = :templateId")
    List<Transport> findAllPassengerTransportsFromTemplate(@Param("passengerId") int passengerId, @Param("templateId") int templateId);

    @Query("SELECT " +
            "   new Transport(viajero.id, conductor.id, ftpp.id)" +
            "       FROM Transport t" +
            "           INNER JOIN TransportDateByTemplate ftpp" +
            "               ON t.transportKey.transportDateId = ftpp.id" +
            "           INNER JOIN Involved viajero" +
            "               ON t.transportKey.passengerId = viajero.id" +
            "           INNER JOIN Involved conductor" +
            "               ON t.transportKey.driverId = conductor.id" +
            "       WHERE " +
            "           t.transportKey.driverId = :driverId" +
            "           AND ftpp.templateCode = :templateId")
    List<Transport> findAllDriverTransportsFromTemplate(@Param("driverId") int driverId, @Param("templateId") int templateId);

    @Modifying
    @Query("UPDATE Transport t" +
            "   SET t.transportKey.driverId = :driverId" +
            "   WHERE " +
            "       t.transportKey.transportDateId = :transportDateId" +
            "       AND t.transportKey.passengerId = :passengerId")
    void updateDriverInTransport(@Param("transportDateId") int transportDateId, @Param("driverId") int driverId, @Param("passengerId") int passengerId);

    @Query("SELECT " +
            "   new Transport(t.transportKey.passengerId, conductor.id, t.transportKey.transportDateId)" +
            "       FROM Transport t" +
            "           INNER JOIN Involved conductor" +
            "               ON t.transportKey.driverId = conductor.id" +
            "       WHERE " +
            "           t.transportKey.passengerId = :passengerId" +
            "           AND t.transportKey.transportDateId = :transportDateId")
    Transport findTransportByPassenger(@Param("transportDateId") int transportDateId, @Param("passengerId") int passengerId);

    @Query("SELECT " +
            "   new com.transports.spring.dto.DtoInvolvedTransport(ftpp.transportDate, ftpp.eventName, CONCAT(viajero.name, ' ', viajero.surname))" +
            "       FROM Transport t" +
            "           INNER JOIN TransportDateByTemplate ftpp" +
            "               ON t.transportKey.transportDateId = ftpp.id" +
            "           INNER JOIN Involved viajero" +
            "               ON t.transportKey.passengerId = viajero.id" +
            "           INNER JOIN Involved conductor" +
            "               ON t.transportKey.driverId = conductor.id" +
            "       WHERE " +
            "           t.transportKey.driverId = :driverId" +
            "           AND ftpp.templateCode = :templateId")
    List<DtoInvolvedTransport> findDriverTransportsFromTemplate(@Param("driverId") int driverId, @Param("templateId") int templateId);

    @Query("SELECT " +
            "   new com.transports.spring.dto.DtoInvolvedTransport(ftpp.transportDate, ftpp.eventName, CONCAT(conductor.name, ' ', conductor.surname))" +
            "       FROM Transport t" +
            "           INNER JOIN TransportDateByTemplate ftpp" +
            "               ON t.transportKey.transportDateId = ftpp.id" +
            "           INNER JOIN Involved viajero" +
            "               ON t.transportKey.passengerId = viajero.id" +
            "           INNER JOIN Involved conductor" +
            "               ON t.transportKey.driverId = conductor.id" +
            "       WHERE " +
            "           t.transportKey.passengerId = :passengerId" +
            "           AND ftpp.templateCode = :templateId" +
            "       ORDER BY ftpp.transportDate ASC")
    List<DtoInvolvedTransport> findPassengerTransportsFromTemplate(@Param("passengerId") int passengerId, @Param("templateId") int templateId);
}
