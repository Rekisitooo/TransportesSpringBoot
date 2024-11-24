package com.transports.spring.repository;

import com.transports.spring.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITemplateRepository extends JpaRepository<Template, Integer> {
}
