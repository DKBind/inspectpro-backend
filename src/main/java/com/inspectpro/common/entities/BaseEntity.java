package com.inspectpro.common.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

public @Data class BaseEntity implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", length = 19)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", length = 19)
    private LocalDateTime updatedDate = LocalDateTime.now();

    @Column(name = "is_deleted", columnDefinition = " boolean DEFAULT 'false'")
    private Boolean isDeleted = false;

}
