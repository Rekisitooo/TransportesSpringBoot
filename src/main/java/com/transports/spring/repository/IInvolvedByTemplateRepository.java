package com.transports.spring.repository;

import com.transports.spring.model.InvolvedByTemplate;
import com.transports.spring.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Driver;
import java.util.List;

@Repository
public interface IInvolvedByTemplateRepository extends JpaRepository<InvolvedByTemplate, Integer> {

    @Query("SELECT " +
            "   i.ID," +
            "   i.NOMBRE," +
            "   i.APELLIDO" +
            "       FROM INVOLUCRADO_POR_PLANTILLA ipp" +
            "           INNER JOIN INVOLUCRADO i " +
            "               ON i.ID = ipp.COD_INVOLUCRADO " +
            "           INNER JOIN ROL_INVOLUCRADO rol " +
            "               ON i.COD_ROL = rol.ID" +
            "       WHERE " +
            "           rol.DESCRIPCION = 'Viajero' " +
            "           AND ipp.COD_PLANTILLA = :templateCode")
    List<Passenger> getAllPassengersFromTemplate(@Param("templateCode") final int templateId);

    @Query("SELECT " +
            "   i.ID," +
            "   i.NOMBRE," +
            "   i.APELLIDO" +
            "       FROM INVOLUCRADO_POR_PLANTILLA ipp" +
            "           INNER JOIN INVOLUCRADO i " +
            "               ON i.ID = ipp.COD_INVOLUCRADO " +
            "           INNER JOIN ROL_INVOLUCRADO rol " +
            "               ON i.COD_ROL = rol.ID" +
            "       WHERE " +
            "           rol.DESCRIPCION = 'Conductor' " +
            "           AND ipp.COD_PLANTILLA = :templateCode")
    List<Driver> getAllDriversFromTemplate(@Param("templateCode") final int templateId);
}

