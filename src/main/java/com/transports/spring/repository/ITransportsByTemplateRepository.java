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
            "   tpp.ID," +
            "   tpp.COD_PLANTILLA" +
            "   tpp.COD_DIA_DE_LA_SEMANA," +
            "   tpp.FECHA_TRANSPORTE," +
            "   ds.NOMBRE" +
            "       FROM TRANSPORTE_POR_PLANTILLA tpp" +
            "           INNER JOIN DIA_DE_LA_SEMANA ds" +
            "               ON tpp.COD_DIA_DE_LA_SEMANA = ds.ID" +
            "       WHERE " +
            "           tpp.COD_PLANTILLA = :codPlantilla")
    List<TransportByTemplate> findAllMonthDatesByTemplateId(@Param("codPlantilla") int templateId);

    @Query("SELECT " +
            "   tpp.ID," +
            "   tpp.COD_PLANTILLA," +
            "   tpp.COD_DIA_DE_LA_SEMANA," +
            "   tpp.FECHA_TRANSPORTE," +
            "   " +
            "       FROM TRANSPORTE_POR_PLANTILLA tpp" +
            "           INNER JOIN INVOLUCRADO_POR_TRANSPORTE ipt" +
            "               ON tpp.ID = ipt.COD_TRANSPORTE" +
            "       WHERE " +
            "           ipt.COD_INVOLUCRADO_POR_PLANTILLA = :involvedId" +
            "           AND tpp.COD_PLANTILLA = :templateId")
    List<TransportByTemplate> findAllTransportsByInvolvedFromTemplate(@Param("involvedID") int involvedId, @Param("templateId") int templateId);
}
