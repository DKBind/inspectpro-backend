package com.inspectpro.common.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@SuppressWarnings("serial")
@MappedSuperclass
public @Data class BaseEntityCustom implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", length = 19)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", length = 19)
    private LocalDateTime updatedDate = LocalDateTime.now();

    @Column(name = "is_deleted", columnDefinition = " boolean DEFAULT 'false'")
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "created_by", updatable = false)
    @JsonIgnore
    private Users createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    @JsonIgnore
    private Users updatedBy;

}
