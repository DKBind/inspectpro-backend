package com.inspectpro.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @Column(name = "role_id", length = 36)
    private String roleId;

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organisation organisation;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "is_system")
    private Boolean isSystem = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
