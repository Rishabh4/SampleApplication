package com.rishabh.SampleApplication.repository;

import com.rishabh.SampleApplication.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String name);
}