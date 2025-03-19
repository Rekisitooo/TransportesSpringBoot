package com.transports.spring.repository;

import com.transports.spring.dto.DtoTransportDateByTemplate;
import com.transports.spring.model.TransportDateByTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ITransportDateByTemplateRepository extends JpaRepository<TransportDateByTemplate, Integer> {

    /**
     * Gets all the template transport days and events
     */
    @Query("SELECT " +
            "   new com.transports.spring.dto.DtoTransportDateByTemplate(ftpp.id, ftpp.templateCode, ftpp.transportDate, ds.id, ds.name)" +
            "       FROM TransportDateByTemplate ftpp" +
            "           INNER JOIN DayOfTheWeek ds" +
            "               ON ftpp.dayOfTheWeekCode = ds.id" +
            "       WHERE " +
            "           ftpp.templateCode = :templateId" +
            "   UNION ( " +
            "       SELECT " +
            "           new com.transports.spring.dto.DtoTransportDateByTemplate(e.id, e.templateCode, e.date, ds.id, ds.name)" +
            "               FROM Event e" +
            "                   INNER JOIN DayOfTheWeek ds" +
            "                       ON FUNCTION('DAYOFWEEK', e.date) = ds.id" +
            "               WHERE " +
            "                   e.templateCode = :templateId" +
            "  ) ")
    List<DtoTransportDateByTemplate> findAllMonthDatesByTemplateId(@Param("templateId") int templateId);

    @Query("SELECT " +
            "   new TransportDateByTemplate(ftpp.id, ftpp.templateCode, ftpp.transportDate, ftpp.dayOfTheWeekCode, ftpp.eventName)" +
            "       FROM TransportDateByTemplate ftpp" +
            "       WHERE " +
            "           ftpp.transportDate = :transportDate")
    TransportDateByTemplate findByTransportDate(@Param("transportDate") Date transportDate);
}
