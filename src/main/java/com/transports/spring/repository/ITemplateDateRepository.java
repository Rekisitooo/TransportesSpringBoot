package com.transports.spring.repository;

import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.model.TransportDateByTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITemplateDateRepository extends JpaRepository<TransportDateByTemplate, Integer> {

    /**
     * Gets all the template transport days and events
     */
    @Query("SELECT " +
            "   new com.transports.spring.dto.DtoTemplateDate(ftpp.id, ftpp.templateCode, ftpp.transportDate, ds.id, ftpp.eventName, ds.name, 'transportDate')" +
            "       FROM TransportDateByTemplate ftpp" +
            "           INNER JOIN DayOfTheWeek ds" +
            "               ON ftpp.dayOfTheWeekCode = ds.id" +
            "       WHERE " +
            "           ftpp.templateCode = :templateId" +
            "   UNION ( " +
            "       SELECT " +
            "           new com.transports.spring.dto.DtoTemplateDate(e.id, e.templateCode, e.date, ds.id, e.eventName, ds.name, 'event')" +
            "               FROM Event e" +
            "                   INNER JOIN DayOfTheWeek ds" +
            "                       ON FUNCTION('DAYOFWEEK', e.date) = ds.id" +
            "               WHERE " +
            "                   e.templateCode = :templateId" +
            "  ) ")
    List<DtoTemplateDate> findAllMonthDatesByTemplateId(@Param("templateId") int templateId);
}
