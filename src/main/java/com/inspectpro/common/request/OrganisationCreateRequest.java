package com.inspectpro.common.request;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrganisationCreateRequest {

    // Core
    private String name;
    private String email;
    private String domain;

    /** UUID of an existing global subscription plan to assign on creation. */
    private UUID subscriptionId;

    /** When the org's subscription period should start. Defaults to now if null. */
    private LocalDateTime subscriptionStartDate;

    // Contact & Identity
    private String phoneNumber;
    private String contactedPersonName;

    // Tax identifiers
    private String gstin;
    private String pan;
    private String tan;

    // Address
    private AddressRequest address;

    // Roles (optional)
    private List<CustomRoleRequest> customRoles;

    @Data
    public static class AddressRequest {
        private String addressLine1;
        private String addressLine2;
        private String street;
        private String district;
        private String state;
        private String country;
        private String pincode;
    }

    @Data
    public static class CustomRoleRequest {
        private String name;
        private String description;
        private String designation;
    }
}
