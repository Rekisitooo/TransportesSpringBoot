package com.transports.spring.repository;

import com.transports.spring.dto.DtoInvolvedAvailabiltyForTransportDate;
import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInvolvedAvailabiltyForTransportDateRepository extends JpaRepository<InvolvedAvailabiltyForTransportDate, Integer> {

    @Query("SELECT new com.transports.spring.dto.DtoInvolvedAvailabiltyForTransportDate(" +
            "           new InvolvedAvailabiltyForTransportDate(" +
            "               ipp.involvedByTemplateKey.involvedCode," +
            "               ftpp.id" +
            "           )," +
            "            concat(ipp.name, ' ',ipp.surname)" +
            "       )" +
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
    List<DtoInvolvedAvailabiltyForTransportDate> findAllDriversAvailableDatesForTemplate(@Param("templateId") int templateId);

    @Query("SELECT new com.transports.spring.dto.DtoInvolvedAvailabiltyForTransportDate(" +
            "           new InvolvedAvailabiltyForTransportDate(" +
            "               ipp.involvedByTemplateKey.involvedCode," +
            "               ftpp.id" +
            "           )," +
            "            concat(ipp.name, ' ',ipp.surname)" +
            "       )" +
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
    List<DtoInvolvedAvailabiltyForTransportDate> findAllPassengersAssistanceDatesForTemplate(@Param("templateId") int templateId);

    @Query("SELECT new InvolvedAvailabiltyForTransportDate(ipp.involvedByTemplateKey.involvedCode, ftpp.id, dipft.needsTransport)" +
            " FROM InvolvedByTemplate ipp " +
            "  INNER JOIN InvolvedRole rol_viajero" +
            "   ON ipp.roleCode = rol_viajero.id" +
            "            AND rol_viajero.description = 'Viajero'" +
            "  INNER JOIN InvolvedAvailabiltyForTransportDate dipft" +
            "   ON dipft.involvedCode = ipp.involvedByTemplateKey.involvedCode" +
            "  INNER JOIN TransportDateByTemplate ftpp" +
            "   ON dipft.transportDateCode = ftpp.id" +
            " WHERE " +
            "  ipp.involvedByTemplateKey.involvedCode = :passengerId" +
            "  AND ipp.involvedByTemplateKey.templateCode = :templateId")
    List<InvolvedAvailabiltyForTransportDate> findAllPassengerAssistanceDatesForTemplate(@Param("templateId") int templateId, @Param("passengerId") int passengerId);

    @Modifying
    @Query("UPDATE InvolvedAvailabiltyForTransportDate diftp " +
            "   SET " +
            "       diftp.needsTransport = :needsTransport" +
            "   WHERE" +
            "       diftp.involvedCode = :involvedCode" +
            "       AND diftp.transportDateCode = :transportDateCode")
    void updateInvolvedNeedForTransport(
            @Param("needsTransport") final Integer needsTransport,
            @Param("involvedCode") final Integer involvedCode,
            @Param("transportDateCode") final Integer transportDateCode
    );


    @Modifying
    @Query("DELETE FROM InvolvedAvailabiltyForTransportDate dipft" +
            " WHERE " +
            "  dipft.involvedCode = :involvedCode" +
            "  AND dipft.transportDateCode = :dateCode")
    void deleteInvolvedAssistanceForDate(@Param("involvedCode") int involvedCode, @Param("dateCode") int dateCode);
}
