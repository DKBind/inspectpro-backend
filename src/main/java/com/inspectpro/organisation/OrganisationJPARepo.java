package com.inspectpro.organisation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inspectpro.common.entities.Organisations;

public interface OrganisationJPARepo extends JpaRepository<Organisations, Long> {

}
