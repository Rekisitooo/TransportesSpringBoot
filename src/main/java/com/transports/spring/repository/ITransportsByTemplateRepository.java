package com.transports.spring.repository;

import com.transports.spring.dto.DtoTransport;
import com.transports.spring.model.TransportByTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransportsByTemplateRepository extends JpaRepository<TransportByTemplate, Integer> {

    @Query("SELECT " +
            "   ftpp.ID as id," +
            "   ftpp.COD_PLANTILLA as templateCode" +
            "   ftpp.COD_DIA_DE_LA_SEMANA as dayOfTheWeekId," +
            "   ftpp.FECHA_TRANSPORTE as transportDate," +
            "   ds.NOMBRE as dayOfTheWeekName" +
            "       FROM FECHA_TRANSPORTE_POR_PLANTILLA ftpp" +
            "           INNER JOIN DIA_DE_LA_SEMANA ds" +
            "               ON tpp.COD_DIA_DE_LA_SEMANA = ds.ID" +
            "       WHERE " +
            "           tpp.COD_PLANTILLA = :templateId")
    List<TransportByTemplate> findAllMonthDatesByTemplateId(@Param("templateId") int templateId);

    @Query("SELECT " +
            "   ftpp.ID as transportId," +
            "   ftpp.FECHA_TRANSPORTE as date," +
            "   concat(viajero.nombre, ' ', viajero.apellidos) as passenger," +
            "   viajero.ID as passengerId," +
            "   concat(conductor.nombre, ' ', conductor.apellidos) as driver," +
            "   conductor.ID as driverId" +
            "       FROM TRANSPORTE_POR_PLANTILLA tpp" +
            "           INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp" +
            "               ON tpp.COD_FECHA_TRANSPORTE = ftpp.ID" +
            "           INNER JOIN INVOLUCRADO viajero" +
            "               ON tpp.COD_VIAJERO = viajero.ID" +
            "           INNER JOIN INVOLUCRADO conductor" +
            "               ON tpp.COD_CONDUCTOR = conductor.ID" +
            "       WHERE " +
            "           tpp.COD_VIAJERO = :passengerId" +
            "           AND ftpp.COD_PLANTILLA = :templateId")
    List<DtoTransport> findAllTransportsByPassengerFromTemplate(@Param("passengerId") int passengerId, @Param("templateId") int templateId);

    @Query("SELECT " +
            "   ftpp.ID as transportId," +
            "   ftpp.FECHA_TRANSPORTE as date," +
            "   concat(viajero.nombre, ' ', viajero.apellidos) as passenger," +
            "   viajero.ID as passengerId," +
            "   concat(conductor.nombre, ' ', conductor.apellidos) as driver," +
            "   conductor.ID as driverId" +
            "       FROM TRANSPORTE_POR_PLANTILLA tpp" +
            "           INNER JOIN FECHA_TRANSPORTE_POR_PLANTILLA ftpp" +
            "               ON tpp.COD_FECHA_TRANSPORTE = ftpp.ID" +
            "           INNER JOIN INVOLUCRADO viajero" +
            "               ON tpp.COD_VIAJERO = viajero.ID" +
            "           INNER JOIN INVOLUCRADO conductor" +
            "               ON tpp.COD_CONDUCTOR = conductor.ID" +
            "       WHERE " +
            "           tpp.COD_VIAJERO = :passengerId" +
            "           AND ftpp.COD_PLANTILLA = :templateId")
    List<DtoTransport> findAllTransportsByDriverFromTemplate(@Param("driverId") int driverId, @Param("templateId") int templateId);
}
