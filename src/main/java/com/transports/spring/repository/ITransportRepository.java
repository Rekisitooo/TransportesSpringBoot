package com.transports.spring.repository;

import com.transports.spring.dto.DtoGetPassengersForDriverByDate;
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

    @Query("SELECT DISTINCT" +
            "   new Transport(passenger.involvedByTemplateKey.involvedCode, driver.involvedByTemplateKey.involvedCode, ftpp.id)" +
            "       FROM Transport t" +
            "           INNER JOIN TransportDateByTemplate ftpp" +
            "               ON t.transportKey.transportDateId = ftpp.id" +
            "           INNER JOIN InvolvedByTemplate passenger" +
            "               ON t.transportKey.passengerId = passenger.involvedByTemplateKey.involvedCode" +
            "           INNER JOIN InvolvedByTemplate driver" +
            "               ON t.transportKey.driverId = driver.involvedByTemplateKey.involvedCode" +
            "       WHERE " +
            "           t.transportKey.passengerId = :passengerId" +
            "           AND ftpp.templateCode = :templateId")
    List<Transport> findAllPassengerTransportsFromTemplate(@Param("passengerId") int passengerId, @Param("templateId") int templateId);

    @Query("SELECT DISTINCT" +
            "   new Transport(passenger.involvedByTemplateKey.involvedCode, driver.involvedByTemplateKey.involvedCode, ftpp.id)" +
            "       FROM Transport t" +
            "           INNER JOIN TransportDateByTemplate ftpp" +
            "               ON t.transportKey.transportDateId = ftpp.id" +
            "           INNER JOIN InvolvedByTemplate passenger" +
            "               ON t.transportKey.passengerId = passenger.involvedByTemplateKey.involvedCode" +
            "           INNER JOIN InvolvedByTemplate driver" +
            "               ON t.transportKey.driverId = driver.involvedByTemplateKey.involvedCode" +
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
            "   new com.transports.spring.dto.DtoInvolvedTransport(ftpp.transportDate, ftpp.eventName, CONCAT(passenger.name, ' ', passenger.surname))" +
            "       FROM Transport t" +
            "           INNER JOIN TransportDateByTemplate ftpp" +
            "               ON t.transportKey.transportDateId = ftpp.id" +
            "           INNER JOIN InvolvedByTemplate passenger" +
            "               ON t.transportKey.passengerId = passenger.involvedByTemplateKey.involvedCode" +
            "           INNER JOIN InvolvedByTemplate driver" +
            "               ON t.transportKey.driverId = driver.involvedByTemplateKey.involvedCode" +
            "       WHERE " +
            "           t.transportKey.driverId = :driverId" +
            "           AND ftpp.templateCode = :templateId")
    List<DtoInvolvedTransport> findDriverTransportsFromTemplate(@Param("driverId") int driverId, @Param("templateId") int templateId);

    @Query("SELECT " +
            "   new com.transports.spring.dto.DtoInvolvedTransport(ftpp.transportDate, ftpp.eventName, CONCAT(driver.name, ' ', driver.surname))" +
            "       FROM Transport t" +
            "           INNER JOIN TransportDateByTemplate ftpp" +
            "               ON t.transportKey.transportDateId = ftpp.id" +
            "           INNER JOIN InvolvedByTemplate passenger" +
            "               ON t.transportKey.passengerId = passenger.involvedByTemplateKey.involvedCode" +
            "           INNER JOIN InvolvedByTemplate driver" +
            "               ON t.transportKey.driverId = driver.involvedByTemplateKey.involvedCode" +
            "       WHERE " +
            "           t.transportKey.passengerId = :passengerId" +
            "           AND ftpp.templateCode = :templateId" +
            "       ORDER BY ftpp.transportDate ASC")
    List<DtoInvolvedTransport> findPassengerTransportsFromTemplate(@Param("passengerId") int passengerId, @Param("templateId") int templateId);

    @Query("SELECT DISTINCT " +
            "   new com.transports.spring.dto.DtoGetPassengersForDriverByDate(" +
            "           new Transport(t.transportKey.passengerId, t.transportKey.driverId, t.transportKey.transportDateId), " +
            "           CONCAT(ipppassenger.name, ' ', ipppassenger.surname)" +
            "   )" +
            "       FROM Transport t" +
            "           INNER JOIN InvolvedByTemplate ippdriver" +
            "               ON t.transportKey.driverId = ippdriver.involvedByTemplateKey.involvedCode" +
            "           INNER JOIN InvolvedByTemplate ipppassenger" +
            "               ON t.transportKey.passengerId = ipppassenger.involvedByTemplateKey.involvedCode" +
            "       WHERE " +
            "           t.transportKey.driverId = :driverId" +
            "           AND t.transportKey.transportDateId = :transportDateId")
    List<DtoGetPassengersForDriverByDate> getPassengersForDriverByDate(@Param("transportDateId") Integer transportDateId, @Param("driverId") Integer driverId);

    @Query("SELECT " +
            "   new Transport(t.transportKey.passengerId, t.transportKey.driverId, t.transportKey.transportDateId)" +
            "       FROM Transport t" +
            "       WHERE " +
            "           t.transportKey.passengerId = :passengerId" +
            "           AND t.transportKey.transportDateId = :transportDateId")
    Transport findTransportByPassenger(@Param("transportDateId") Integer transportDateId, @Param("passengerId") Integer passengerId);
}
