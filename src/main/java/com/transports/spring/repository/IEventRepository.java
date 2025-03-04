package com.transports.spring.repository;

import com.transports.spring.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT " +
            "   new com.transports.spring.model.Event(e.id, e.templateCode, e.date, e.eventName)" +
            "       FROM Event e" +
            "       WHERE " +
            "           e.templateCode = :templateId")
    List<Event> findAllEventsByTemplateId(@Param("templateId") int templateId);
}
