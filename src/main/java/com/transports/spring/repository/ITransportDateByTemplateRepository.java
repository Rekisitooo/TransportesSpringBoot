package com.transports.spring.repository;

import com.transports.spring.model.TransportDateByTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransportDateByTemplateRepository extends JpaRepository<TransportDateByTemplate, Integer> {

    @Query("SELECT " +
            "   new TransportDateByTemplate(ftpp.id, ftpp.templateCode, ftpp.transportDate, ftpp.dayOfTheWeekCode, ds.name)" +
            "       FROM TransportDateByTemplate ftpp" +
            "           INNER JOIN DayOfTheWeek ds" +
            "               ON ftpp.dayOfTheWeekCode = ds.id" +
            "       WHERE " +
            "           ftpp.templateCode = :templateId" +
            "       ORDER BY ftpp.transportDate ASC")
    List<TransportDateByTemplate> findAllMonthDatesByTemplateId(@Param("templateId") int templateId);
}
