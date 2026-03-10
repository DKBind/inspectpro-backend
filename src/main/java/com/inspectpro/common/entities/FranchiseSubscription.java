package com.inspectpro.common.entities;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "franchise_subscriptions")
@Data
@Builder
public class FranchiseSubscription extends BaseEntityCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "franchise_subscription_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Column(name = "org_id", nullable = false)
    private UUID orgId;

    @Column(name = "franchise_id", nullable = false)
    private UUID franchiseId;

    @Column(name = "plan_name", nullable = false, length = 100)
    private String planName;

    @Column(name = "price_to_franchise", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal priceToFranchise = BigDecimal.ZERO;

    @Column(name = "currency", length = 3)
    @Builder.Default
    private String currency = "INR";

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(name = "allowed_modules", columnDefinition = "jsonb")
    private List<String> allowedModules;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
