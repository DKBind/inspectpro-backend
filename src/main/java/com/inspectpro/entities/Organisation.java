package com.inspectpro.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "organisations")
@Data
public class Organisation {

    @Id
    @Column(name = "org_id", length = 36)
    private String orgId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String domain;

    @Column(name = "plan_type", nullable = false)
    private String planType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(columnDefinition = "jsonb")
    private String settings;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
