package com.transports.spring.repository;

import com.transports.spring.model.AbstractInvolved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Driver;

@Repository
public interface IDriverRepository extends JpaRepository<Driver, Integer> {
    @Query("SELECT " +
            "   i.ID," +
            "   i.NOMBRE," +
            "   i.APELLIDO," +
            "   dift.FECHA_TRANSPORTE," +
            "   dift.COD_FECHA_TRANSPORTE" +
            "       FROM FECHA_TRANSPORTE_POR_PLANTILLA ftpp" +
            "           INNER JOIN DISPONIBILIDAD_INVOLUCRADO_POR_FECHA_TRANSPORTE dift" +
            "               ON ftpp.ID = dift.COD_FECHA_TRANSPORTE" +
            "           INNER JOIN INVOLUCRADO i" +
            "               ON dift.COD_INVOLUCRADO = i.ID" +
            "           INNER JOIN ROL_INVOLUCRADO roli" +
            "               ON i.COD_ROL = roli.ID" +
            "           WHERE" +
            "               roli.DESCRIPCION = 'Conductor' " +
            "               AND ftpp.COD_PLANTILLA = ?; ");
}
