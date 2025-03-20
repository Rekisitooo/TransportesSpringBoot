package com.transports.spring.repository;

import com.transports.spring.model.TransportDateByTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface ITransportDateByTemplateRepository extends JpaRepository<TransportDateByTemplate, Integer> {

    @Query("SELECT " +
            "   new TransportDateByTemplate(ftpp.id, ftpp.templateCode, ftpp.transportDate, ftpp.dayOfTheWeekCode, ftpp.eventName)" +
            "       FROM TransportDateByTemplate ftpp" +
            "       WHERE " +
            "           ftpp.transportDate = :transportDate")
    TransportDateByTemplate findByTransportDate(@Param("transportDate") Date transportDate);
}
