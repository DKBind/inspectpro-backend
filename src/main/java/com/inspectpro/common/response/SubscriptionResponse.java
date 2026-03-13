package com.inspectpro.common.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/** Response for a global subscription plan. */
@Data
@Builder
public class SubscriptionResponse {
    private UUID id;
    private String planName;
    private BigDecimal price;
    private String currency;
    private StatusInfo status;
    private Integer durationMonths;
    private String notes;

    @Data
    @Builder
    public static class StatusInfo {
        private Long id;
        private String name;
        private String colourCode;
    }
}
