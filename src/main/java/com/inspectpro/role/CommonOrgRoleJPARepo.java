package com.inspectpro.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inspectpro.common.entities.CommonOrgRole;
import java.util.List;

@Repository
public interface CommonOrgRoleJPARepo extends JpaRepository<CommonOrgRole, Long> {
    List<CommonOrgRole> findByIsActiveTrue();
}
