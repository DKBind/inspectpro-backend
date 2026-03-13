package com.inspectpro.common.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "organisations")
@Data
public class Organisations extends BaseEntityCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "domain")
    private String domain;

    @Column(name = "plan_type")
    private String planType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Contact & Identity
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "contacted_person_name")
    private String contactedPersonName;

    // Tax identifiers
    @Column(name = "gstin", length = 15)
    private String gstin;

    @Column(name = "pan", length = 10)
    private String pan;

    @Column(name = "tan", length = 10)
    private String tan;

    // Status
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    // Address (one organisation → one address)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private OrganisationAddress address;
}
