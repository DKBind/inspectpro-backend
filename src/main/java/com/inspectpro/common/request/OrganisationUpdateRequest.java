package com.inspectpro.common.request;

import lombok.Data;

@Data
public class OrganisationUpdateRequest {

    private String name;
    private String email;
    private String domain;
    private String planType;  // plain string — no enum dependency

    private String phoneNumber;
    private String contactedPersonName;
    private String gstin;
    private String pan;
    private String tan;

    private AddressRequest address;

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
}
