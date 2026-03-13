package com.inspectpro.common.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/** Response for an organisation's subscription assignment. */
@Data
@Builder
public class OrgSubscriptionResponse {
    private UUID orgSubscriptionId;
    private UUID subscriptionId;
    private String planName;
    private BigDecimal price;
    private String currency;
    private SubscriptionResponse.StatusInfo status;
    private Integer maxUsers;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String notes;
    private boolean active;
}
