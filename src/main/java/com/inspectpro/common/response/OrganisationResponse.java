package com.inspectpro.common.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganisationResponse {

    private UUID uuid;
    private String name;
    private String email;
    private String domain;
    private String planType;  // plain string — DB-driven, no enum

    // Lombok generates isActive() for Boolean fields named isActive → Jackson serialises as "active".
    // @JsonProperty forces the JSON key to be "isActive" as the frontend expects.
    @JsonProperty("isActive")
    private Boolean isActive;

    // Contact & Identity
    private String phoneNumber;
    private String contactedPersonName;

    // Tax identifiers
    private String gstin;
    private String pan;
    private String tan;

    // Status
    private Long statusId;
    private String statusName;
    private String statusColourCode;

    // Address
    private AddressInfo address;

    // Subscription
    private UUID subscriptionId;

    private LocalDateTime createdAt;

    @Data
    @Builder
    public static class AddressInfo {
        private Long id;
        private String addressLine1;
        private String addressLine2;
        private String street;
        private String district;
        private String state;
        private String country;
        private String pincode;
    }
}
