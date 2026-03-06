package com.inspectpro.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "modules")
@Data
public class Module {

    @Id
    @Column(name = "module_id", length = 36)
    private String moduleId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String key;

    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "parent_module_id")
    private Module parentModule;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
