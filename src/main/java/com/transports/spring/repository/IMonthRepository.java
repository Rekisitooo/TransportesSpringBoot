package com.transports.spring.repository;

import com.transports.spring.model.Month;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMonthRepository extends JpaRepository<Month, Integer> {

}
