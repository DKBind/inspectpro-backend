package com.inspectpro.common.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "organisation_address")
@Data
public class OrganisationAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "street")
    private String street;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "pincode")
    private String pincode;
}
