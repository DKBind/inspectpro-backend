package com.inspectpro.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "domain")
    private String domain;

    @Column(name = "plan_type")
    private String planType;

    @Column(name = "is_active")
    private Boolean isActive = true;

}
