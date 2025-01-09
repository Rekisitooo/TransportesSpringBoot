package com.transports.spring.repository;

import com.transports.spring.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserGroupRepository extends JpaRepository<UserGroup, Integer> {
}
