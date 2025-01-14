package com.transports.spring.repository;

import com.transports.spring.model.InvolvedByTransport;
import com.transports.spring.model.WeeklyTransportDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInvolvedByTransportRepository extends JpaRepository<InvolvedByTransport, Integer> {

}
