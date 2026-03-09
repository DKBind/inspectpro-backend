package com.inspectpro.common.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.inspectpro.common.enums.PlanType;
import com.inspectpro.common.enums.SubscriptionStatus;
import com.inspectpro.common.enums.SubscriptionType;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "subscription_id", updatable = false, nullable = false)
    private Long subscriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private Organisations organisation;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type", nullable = false, length = 20)
    private SubscriptionType subscriptionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false, length = 20)
    private PlanType planType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private SubscriptionStatus status = SubscriptionStatus.TRIAL;

    // @Enumerated(EnumType.STRING)
    // @Column(name = "billing_cycle", nullable = false, length = 20)
    // @Builder.Default
    // private BillingCycle billingCycle = BillingCycle.MONTHLY;

    @Column(name = "price_per_cycle", precision = 10, scale = 2)
    private BigDecimal pricePerCycle;

    /**
     * ISO 4217 currency code. e.g. "INR", "USD", "EUR"
     * //
     */
    // @Column(name = "currency", length = 3)
    // @Builder.Default
    // private String currency = "INR";

    // ─────────────────────────────────────────
    // BILLING PERIOD
    // ─────────────────────────────────────────

    @Column(name = "trial_ends_at")
    private LocalDateTime trialEndsAt;

    @Column(name = "current_period_start")
    private LocalDateTime currentPeriodStart;

    @Column(name = "current_period_end")
    private LocalDateTime currentPeriodEnd;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "managed_by", nullable = false)
    private UUID managedBy;

    @Column(name = "last_modified_by")
    private UUID lastModifiedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public boolean isOrganisationSubscription() {
        return this.subscriptionType == SubscriptionType.ORGANISATION;
    }

    public boolean isFranchiseSubscription() {
        return this.subscriptionType == SubscriptionType.FRANCHISE;
    }

    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE
                || this.status == SubscriptionStatus.TRIAL;
    }

    public boolean isCancellable() {
        return this.status == SubscriptionStatus.ACTIVE
                || this.status == SubscriptionStatus.TRIAL
                || this.status == SubscriptionStatus.PAST_DUE;
    }

}
