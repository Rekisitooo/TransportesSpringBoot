package com.transports.spring.repository;

import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;
import com.transports.spring.model.TransportByTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInvolvedAvailabiltyForTransportDateRepository extends JpaRepository<InvolvedAvailabiltyForTransportDate, Integer> {

    @Query("SELECT new InvolvedAvailabiltyForTransportDate(ipp.involvedByTemplateKey.involvedCode, ftpp.id, concat(ipp.name, ' ',ipp.surname))" +
            " FROM InvolvedByTemplate ipp " +
            "  INNER JOIN InvolvedRole rol_conductor" +
            "   ON ipp.roleCode = rol_conductor.id" +
            "            AND rol_conductor.description = 'Conductor'" +
            "  INNER JOIN InvolvedAvailabiltyForTransportDate dipft" +
            "   ON dipft.involvedCode = ipp.involvedByTemplateKey.involvedCode" +
            "  INNER JOIN TransportDateByTemplate ftpp" +
            "   ON dipft.transportDateCode = ftpp.id" +
            " WHERE " +
            "  ipp.involvedByTemplateKey.templateCode = :templateId")
    List<InvolvedAvailabiltyForTransportDate> findAllDriversAvailableForDate(@Param("templateId") int templateId);

    @Query("SELECT new InvolvedAvailabiltyForTransportDate(ipp.involvedByTemplateKey.involvedCode, ftpp.id, concat(ipp.name, ' ',ipp.surname))" +
            " FROM InvolvedByTemplate ipp " +
            "  INNER JOIN InvolvedRole rol_viajero" +
            "   ON ipp.roleCode = rol_viajero.id" +
            "            AND rol_viajero.description = 'Viajero'" +
            "  INNER JOIN InvolvedAvailabiltyForTransportDate dipft" +
            "   ON dipft.involvedCode = ipp.involvedByTemplateKey.involvedCode" +
            "  INNER JOIN TransportDateByTemplate ftpp" +
            "   ON dipft.transportDateCode = ftpp.id" +
            " WHERE " +
            "  ipp.involvedByTemplateKey.templateCode = :templateId")
    List<InvolvedAvailabiltyForTransportDate> findAllPassengersAvailableForDate(@Param("templateId") int templateId);
}
