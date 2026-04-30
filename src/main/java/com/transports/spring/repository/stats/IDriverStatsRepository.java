package com.transports.spring.repository.stats;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.transports.spring.dto.stats.driver.DtoDriverStats;
import com.transports.spring.model.Involved;
import com.transports.spring.model.Transport;

@Repository
@Transactional(readOnly = true)
public interface IDriverStatsRepository extends JpaRepository<Involved, Integer> {

    /* 
        SELECT DISTINCT
            ipp.*
	            FROM TRANSPORTE t 
                    INNER JOIN fecha_transporte_por_plantilla ftpp
                        ON t.COD_FECHA_TRANSPORTE = ftpp.ID
                    INNER JOIN involucrado_por_plantilla ipp	
                        ON ipp.COD_INVOLUCRADO = t.cod_conductor
                INNER JOIN involucrado_por_plantilla ipp	
                    ON ipp.COD_INVOLUCRADO = t.cod_conductor
                    AND ftpp.COD_PLANTILLA = ipp.COD_PLANTILLA
                INNER JOIN color color
                    ON color.id = ipp.cod_color
                GROUP BY 
                    ftpp.fecha_transporte, t.cod_conductor
                HAVING 
                    ftpp.fecha_transporte > DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL ? MONTH), '%Y-%m-01');
    */
    @Query("SELECT DISTINCT " +
                "new com.transports.spring.dto.stats.driver.DtoDriverStats(" +
                    "new com.transports.spring.model.Driver(ipp.involvedByTemplateKey.involvedCode, ipp.name, ipp.surname, ipp.roleCode, 0, null, ipp.colorCode), " +
                    "color" + 
                ") " +
                "FROM Transport t " +
                    "INNER JOIN TransportDateByTemplate ftpp " +
                        "ON t.transportKey.transportDateId = ftpp.ID " +
                    "INNER JOIN InvolvedByTemplate ipp " +
			            "ON ipp.involvedByTemplateKey.involvedCode = t.transportKey.driverId " +
                        "AND ipp.involvedByTemplateKey.templateCode = ftpp.templateCode " +
		            "INNER JOIN Color color " +
			            "ON color.id = ipp.colorCode " +
	            "GROUP BY " +
                    "ftpp.transportDate, t.transportKey.driverId " +
	            "HAVING " + 
                    "ftpp.transportDate > :initialDate AND ftpp.transportDate < (:finalDate)")
    List<DtoDriverStats> findAllDriversWithTransportsOnMonths(@Param("initialDate") Date initialDate, @Param("finalDate") Date finalDate);


    /*
        SELECT DISTINCT
            t.cod_fecha_transporte, 
            T.COD_CONDUCTOR
                FROM TRANSPORTE t 
                    INNER JOIN fecha_transporte_por_plantilla ftpp
                        ON t.COD_FECHA_TRANSPORTE = ftpp.ID
                WHERE 
                    ftpp.fecha_transporte > DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL (?) MONTH), '%Y-%m-01')
                    AND T.COD_CONDUCTOR IN (?);
     */
    @Query("SELECT DISTINCT " +
                "new com.transports.spring.model.Transport(0, t.transportKey.driverId, t.transportKey.transportDateId) " +
                "FROM Transport t " +
                    "INNER JOIN TransportDateByTemplate ftpp " +
                        "ON t.transportKey.transportDateId = ftpp.ID " +
                    "WHERE " +
                        "ftpp.transportDate > (:initialDate) " + 
                        "AND ftpp.transportDate < (:finalDate) " +
                        "AND t.transportKey.driverId IN (:driverCodes)")
    List<Transport> findAllDriverTransportsOnMonths(@Param("initialDate") Date initialDate, @Param("finalDate") Date finalDate, @Param("driverCodes") List<Integer> driverCodes);
}
