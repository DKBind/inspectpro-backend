package com.inspectpro.common.entities;

import java.math.BigDecimal;
import java.util.UUID;

import com.inspectpro.common.enums.PlanType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "org_subscriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgSubscription extends BaseEntityCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Give it its own PK
    @Column(name = "org_sub_id")
    private UUID subscriptionId;

    /**
     * WHY @MapsId + @OneToOne:
     * org_subscriptions.subscription_id IS the FK and also the PK.
     * This is a shared-primary-key one-to-one relationship.
     * Avoids a separate surrogate key column in the detail table.
     * subscription_id = "join key" = the detail row's own PK.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    /** The org this subscription is for (denormalized for easier querying) */
    @Column(name = "org_id", nullable = false)
    private UUID orgId;

    /** Which platform plan: FREE, STARTER, PRO, ENTERPRISE */
    @Enumerated(EnumType.STRING)
    @Column(name = "platform_plan", nullable = false, length = 20)
    private PlanType platformPlan;

    /** What the organisation pays to the platform per billing cycle */
    @Column(name = "platform_price", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    BigDecimal platformPrice = BigDecimal.ZERO;

    @Column(name = "currency", length = 3)
    @Builder.Default
    String currency = "INR";

    /** Max number of franchises this org can create under their plan */
    @Column(name = "max_franchises")
    private Integer maxFranchises;

    /** Max total users (across org + all franchises) */
    @Column(name = "max_users")
    private Integer maxUsers;

    /** Notes from Super Admin (internal) */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
