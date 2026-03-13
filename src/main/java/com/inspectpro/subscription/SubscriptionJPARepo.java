package com.inspectpro.subscription;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inspectpro.common.entities.Subscription;

public interface SubscriptionJPARepo extends JpaRepository<Subscription, UUID> {

}
