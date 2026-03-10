package com.inspectpro.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "module")
public @Data class Module extends BaseEntityCustom {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", columnDefinition = "varchar(50)")
    private String name;

    @Column(name = "description", columnDefinition = "varchar(250)")
    private String description;

    @Column(name = "category", columnDefinition = "varchar(250)")
    private String category;

    @Column(name = "icon", columnDefinition = "varchar(250)")
    private String icon;

    @Column(name = "route", columnDefinition = "varchar(250)")
    private String route;

    @Column(name = "priority")
    private int priority;

    @Column(name = "s_no")
    private int sNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "is_production")
    private int isProduction;

    @Column(name = "type", columnDefinition = "varchar(100)")
    private String type;
}
