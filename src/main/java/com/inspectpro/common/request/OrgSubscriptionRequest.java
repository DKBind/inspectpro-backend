package com.inspectpro.common.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/** DTO for assigning / updating an existing subscription plan to an organisation. */
@Data
public class OrgSubscriptionRequest {
    private UUID subscriptionId;       // required – pick an existing global plan
    private BigDecimal priceOverride;  // optional override of the plan's price
    private String currency;
    private Integer maxUsers;
    private String notes;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
}
