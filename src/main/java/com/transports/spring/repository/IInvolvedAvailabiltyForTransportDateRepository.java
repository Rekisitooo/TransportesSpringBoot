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
            "   ON dipft.involvedAvailabilityForTransportDateKey.involvedCode = ipp.involvedByTemplateKey.involvedCode" +
            "  INNER JOIN TransportDateByTemplate ftpp" +
            "   ON dipft.involvedAvailabilityForTransportDateKey.transportDateCode = ftpp.id" +
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
            "  INNER JOIN InvolvedRole passenger_role" +
            "   ON ipp.roleCode = passenger_role.id" +
            "            AND passenger_role.description = 'Viajero'" +
            "  INNER JOIN InvolvedAvailabiltyForTransportDate dipft" +
            "   ON dipft.involvedAvailabilityForTransportDateKey.involvedCode = ipp.involvedByTemplateKey.involvedCode" +
            "  INNER JOIN TransportDateByTemplate ftpp" +
            "   ON dipft.involvedAvailabilityForTransportDateKey.transportDateCode = ftpp.id" +
            " WHERE " +
            "  ipp.involvedByTemplateKey.templateCode = :templateId")
    List<DtoInvolvedAvailabiltyForTransportDate> findAllPassengersAssistanceDatesForTemplate(@Param("templateId") int templateId);

    @Query("SELECT new InvolvedAvailabiltyForTransportDate(ipp.involvedByTemplateKey.involvedCode, ftpp.id, dipft.needsTransport)" +
            " FROM InvolvedByTemplate ipp " +
            "  INNER JOIN InvolvedRole passenger_role" +
            "   ON ipp.roleCode = passenger_role.id" +
            "            AND passenger_role.description = 'Viajero'" +
            "  INNER JOIN InvolvedAvailabiltyForTransportDate dipft" +
            "   ON dipft.involvedAvailabilityForTransportDateKey.involvedCode = ipp.involvedByTemplateKey.involvedCode" +
            "  INNER JOIN TransportDateByTemplate ftpp" +
            "   ON dipft.involvedAvailabilityForTransportDateKey.transportDateCode = ftpp.id" +
            " WHERE " +
            "  ipp.involvedByTemplateKey.involvedCode = :passengerId" +
            "  AND ipp.involvedByTemplateKey.templateCode = :templateId")
    List<InvolvedAvailabiltyForTransportDate> findAllPassengerAssistanceDatesForTemplate(@Param("templateId") int templateId, @Param("passengerId") int passengerId);

    @Query("SELECT new InvolvedAvailabiltyForTransportDate(ipp.involvedByTemplateKey.involvedCode, ftpp.id, dipft.needsTransport)" +
            " FROM InvolvedByTemplate ipp " +
            "  INNER JOIN InvolvedRole passenger_role" +
            "   ON ipp.roleCode = passenger_role.id" +
            "            AND passenger_role.description = 'Conductor'" +
            "  INNER JOIN InvolvedAvailabiltyForTransportDate dipft" +
            "   ON dipft.involvedAvailabilityForTransportDateKey.involvedCode = ipp.involvedByTemplateKey.involvedCode" +
            "  INNER JOIN TransportDateByTemplate ftpp" +
            "   ON dipft.involvedAvailabilityForTransportDateKey.transportDateCode = ftpp.id" +
            " WHERE " +
            "  ipp.involvedByTemplateKey.involvedCode = :driverId" +
            "  AND ipp.involvedByTemplateKey.templateCode = :templateId")
    List<InvolvedAvailabiltyForTransportDate> findAllDriversAssistanceDatesForTemplate(@Param("templateId") int templateId, @Param("driverId") int driverId);

    @Modifying
    @Query("UPDATE InvolvedAvailabiltyForTransportDate diftp " +
            "   SET " +
            "       diftp.needsTransport = :needsTransport" +
            "   WHERE" +
            "       diftp.involvedAvailabilityForTransportDateKey.involvedCode = :involvedCode" +
            "       AND diftp.involvedAvailabilityForTransportDateKey.transportDateCode = :transportDateCode")
    void updateInvolvedNeedForTransport(
            @Param("needsTransport") final Integer needsTransport,
            @Param("involvedCode") final Integer involvedCode,
            @Param("transportDateCode") final Integer transportDateCode
    );


    @Modifying
    @Query("DELETE FROM InvolvedAvailabiltyForTransportDate dipft" +
            " WHERE " +
            "  dipft.involvedAvailabilityForTransportDateKey.involvedCode = :involvedCode" +
            "  AND dipft.involvedAvailabilityForTransportDateKey.transportDateCode = :dateCode")
    void deleteInvolvedAssistanceForDate(@Param("involvedCode") int involvedCode, @Param("dateCode") int dateCode);
}
