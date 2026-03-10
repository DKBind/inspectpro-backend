package com.inspectpro.common.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.inspectpro.common.enums.PlanType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganisationResponse {
    
    private Long id;
    private UUID uuid;
    private String name;
    private String slug;
    private String domain;
    private PlanType planType;
    private boolean isActive;
    
    // Subscription details
    private UUID subscriptionId;
    
    private LocalDateTime createdAt;
}
