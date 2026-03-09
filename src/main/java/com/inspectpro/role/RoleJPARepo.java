package com.inspectpro.role;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inspectpro.common.entities.Role;

public interface RoleJPARepo extends JpaRepository<Role, Long> {

}
