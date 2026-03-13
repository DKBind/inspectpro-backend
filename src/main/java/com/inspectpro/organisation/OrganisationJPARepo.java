package com.inspectpro.organisation;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inspectpro.common.entities.Organisations;

public interface OrganisationJPARepo extends JpaRepository<Organisations, UUID> {

    boolean existsByEmail(String email);

    // Exclude the current org's own email when checking uniqueness on update
    boolean existsByEmailAndIdNot(String email, UUID id);

    @Query("SELECT o FROM Organisations o WHERE o.isDeleted = false OR o.isDeleted IS NULL ORDER BY o.createdDate DESC")
    Page<Organisations> findAllNonDeleted(Pageable pageable);
}
