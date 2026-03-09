package com.inspectpro.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "organisations")
public class Organisations {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "domain")
    private String domain;

    @Column(name = "plan_type")
    private String planType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(columnDefinition = "jsonb")
    private String settings;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "uuid", unique = true, columnDefinition = "char(36)")
    @JdbcTypeCode(Types.VARCHAR)
    private UUID uuid;

    @Column(name = "is_flag", columnDefinition = " int DEFAULT '1'")
    private int isFlag = 1;

}
