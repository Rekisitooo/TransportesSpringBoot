package com.transports.spring.repository;

import com.transports.spring.model.TemplateTransportDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITemplateTransportDateRepository extends JpaRepository<TemplateTransportDate, Integer> {

    @Query("");
    List<TemplateTransportDate> findAllMonthDatesByTemplateId(final int templateId);
}
