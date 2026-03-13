package com.inspectpro.common.request;

import lombok.Data;

import java.math.BigDecimal;

/** DTO for creating / updating a global subscription plan. */
@Data
public class SubscriptionRequest {
    private String planName;
    private BigDecimal price;
    private Integer durationMonths;
    private Long statusId;   // optional on create; defaults to ACTIVE
    private String notes;
}
