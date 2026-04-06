package com.transports.spring.repository.stats;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.transports.spring.dto.stats.driver.DtoDriverStats;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Involved;

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
                GROUP BY 
                    ftpp.fecha_transporte, t.cod_conductor
                HAVING 
                    ftpp.fecha_transporte > DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL ? MONTH), '%Y-%m-01');
    */
    @Query("SELECT DISTINCT " +
                "new com.transports.spring.model.Driver(ipp.involvedByTemplateKey.involvedCode, ipp.name, ipp.surname, ipp.roleCode, ipp.seats, ipp.ministryGroup, ipp.colorCode) " +
                "FROM Transport t " +
                    "INNER JOIN TransportDateByTemplate ftpp " +
                        "ON t.transportKey.transportDateId = ftpp.ID " +
                    "INNER JOIN InvolvedByTemplate ipp " +
			            "ON ipp.involvedByTemplateKey.involvedCode = t.transportKey.driverId " +
	            "GROUP BY " +
                    "ftpp.transportDate, t.transportKey.driverId " +
	            "HAVING " + 
                    "ftpp.transportDate > :initialDate")
    List<Driver> findAllDriversWithTransportsOnTheLastMonths(@Param("initialDate") Date initialDate);


    /*
        SELECT DISTINCT
            COUNT(t.cod_fecha_transporte) AS total_transportes, 
            T.COD_CONDUCTOR
                FROM TRANSPORTE t 
                    INNER JOIN fecha_transporte_por_plantilla ftpp
                        ON t.COD_FECHA_TRANSPORTE = ftpp.ID
                    INNER JOIN involucrado_por_plantilla ipp	
                        ON ipp.COD_INVOLUCRADO = t.cod_conductor
                    INNER JOIN color color
                        ON color.id = ipp.cod_color
                WHERE 
                    ftpp.fecha_transporte > DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL (?) MONTH), '%Y-%m-01')
                GROUP BY 
                    T.COD_CONDUCTOR
                HAVING 
                    T.COD_CONDUCTOR IN (?)
                ORDER BY 
                    total_transportes DESC;
     */
    @Query("SELECT DISTINCT " +
                "new com.transports.spring.dto.stats.driver.DtoDriverStats(ipp, color, COUNT(t.transportKey.transportDateId)) " +
                "FROM Transport t " +
                    "INNER JOIN TransportDateByTemplate ftpp " +
                        "ON t.transportKey.transportDateId = ftpp.ID " +
                    "INNER JOIN InvolvedByTemplate ipp " +
			            "ON ipp.involvedByTemplateKey.involvedCode = t.transportKey.driverId " +
		            "INNER JOIN Color color " +
			            "ON color.id = ipp.colorCode " +
                    "WHERE " +
                        "ftpp.transportDate > :initialDate " +
                    "GROUP BY " +
                        "t.transportKey.driverId " + 
                    "HAVING " +
                        "t.transportKey.driverId IN (:driverCodes) " + 
                    "ORDER BY " + 
                        "COUNT(t.transportKey.transportDateId) DESC")
    List<DtoDriverStats> findAllDriverTransportsOnTheLastMonths(@Param("initialDate") Date initialDate, @Param("driverCodes") List<Integer> driverCodes);
}
