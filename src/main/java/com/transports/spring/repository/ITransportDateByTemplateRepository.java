package com.transports.spring.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.transports.spring.model.TransportDateByTemplate;

@Repository
public interface ITransportDateByTemplateRepository extends JpaRepository<TransportDateByTemplate, Integer> {

    @Query("SELECT " +
            "   new TransportDateByTemplate(ftpp.id, ftpp.templateCode, ftpp.transportDate, ftpp.dayOfTheWeekCode, ftpp.eventName)" +
            "       FROM TransportDateByTemplate ftpp" +
            "       WHERE " +
            "           ftpp.transportDate = :transportDate")
    TransportDateByTemplate findByTransportDate(@Param("transportDate") Date transportDate);

    @Query("SELECT " +
            "   new TransportDateByTemplate(ftpp.id, ftpp.templateCode, ftpp.transportDate, ftpp.dayOfTheWeekCode, ftpp.eventName)" +
            "       FROM TransportDateByTemplate ftpp" +
            "       WHERE " +
            "           ftpp.transportDate >= :initialDate AND ftpp.transportDate <= :finalDate")
    List<TransportDateByTemplate> findTransportsDatesOnMonths(@Param("initialDate") Date initialDate, @Param("finalDate") Date finalDate);
}
