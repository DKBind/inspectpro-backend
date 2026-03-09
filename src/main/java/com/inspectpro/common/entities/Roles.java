package com.inspectpro.common.entities;

import com.inspectpro.common.enums.RoleScope;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "CommonRoles")
@Table(name = "common_roles")
public class Roles {

    @Id
    @Column(name = "role_id", length = 36)
    private Long roleId;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "varchar(250)")
    private String description;

    @Column(name = "designation", columnDefinition = "varchar(250)")
    private String designation;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private RoleScope scope; // default ORGANISATION

    @Column(name = "is_system")
    private boolean systemRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id")
    private Organisations organisation;

    @OneToMany(mappedBy = "role")
    private Set<RolePermission> permissions;

}
