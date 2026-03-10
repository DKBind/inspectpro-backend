package com.inspectpro.common.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.inspectpro.common.enums.SubscriptionStatus;
import com.inspectpro.common.enums.SubscriptionType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Subscription extends BaseEntityCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "subscription_id", updatable = false, nullable = false)
    private UUID subscriptionId;

    /**
     * WHO this subscription belongs to:
     * ORGANISATION → org_id of the Organisation
     * FRANCHISE → org_id of the Franchise
     * CUSTOMER → org_id of the Franchise (customer belongs to a franchise)
     */
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    /**
     * WHO created/manages this subscription:
     * ORGANISATION → NULL (platform/Super Admin, no org_id)
     * FRANCHISE → org_id of the parent Organisation
     * CUSTOMER → org_id of the Franchise
     */
    @Column(name = "created_by_org_id")
    private UUID createdByOrgId;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type", nullable = false, length = 20)
    private SubscriptionType subscriptionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    SubscriptionStatus status = SubscriptionStatus.TRIAL;

    // @Enumerated(EnumType.STRING)
    // @Column(name = "billing_cycle", nullable = false, length = 20)
    // @Builder.Default BillingCycle billingCycle = BillingCycle.MONTHLY;

    // @Column(name = "trial_ends_at") private LocalDateTime trialEndsAt;
    @Column(name = "current_period_start")
    private LocalDateTime currentPeriodStart;

    @Column(name = "current_period_end")
    private LocalDateTime currentPeriodEnd;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrgSubscription> orgSubscription;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FranchiseSubscription> franchiseSubscription;

    // helpers
    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE || status == SubscriptionStatus.TRIAL;
    }
}
