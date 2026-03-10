package com.inspectpro.organisation;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inspectpro.common.entities.OrgSubscription;

@Repository
public interface OrgSubscriptionJPARepo extends JpaRepository<OrgSubscription, UUID> {
}
