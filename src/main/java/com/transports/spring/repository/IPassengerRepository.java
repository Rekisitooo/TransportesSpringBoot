package com.transports.spring.repository;

import com.transports.spring.model.AbstractInvolved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPassengerRepository extends JpaRepository<AbstractInvolved, Integer> {
}
