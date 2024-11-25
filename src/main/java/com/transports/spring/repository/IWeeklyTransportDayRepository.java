package com.transports.spring.repository;

import com.transports.spring.model.WeeklyTransportDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWeeklyTransportDayRepository extends JpaRepository<WeeklyTransportDay, Integer> {

    @Query("SELECT new WeeklyTransportDay(id, description, dayOfTheWeek, isActive, userCode, groupCode) FROM WeeklyTransportDay WHERE isActive = TRUE")
    List<WeeklyTransportDay> findActiveWeeklyTransportDayList();
}
