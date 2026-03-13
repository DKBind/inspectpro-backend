package com.inspectpro.subscription;

import com.inspectpro.common.entities.OrgSubscription;
import com.inspectpro.common.entities.Status;
import com.inspectpro.common.entities.Subscription;
import com.inspectpro.common.request.OrgSubscriptionRequest;
import com.inspectpro.common.request.SubscriptionRequest;
import com.inspectpro.common.response.OrgSubscriptionResponse;
import com.inspectpro.common.response.SubscriptionResponse;
import com.inspectpro.common.response.SubscriptionResponse.StatusInfo;
import com.inspectpro.helper.CentralService;
import com.inspectpro.helper.ConstantKey;
import com.inspectpro.helper.ResponseEntityObject;
import com.inspectpro.organisation.OrgSubscriptionJPARepo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionService extends CentralService {

    private static final String STATUS_TYPE = "SUBSCRIPTION";

    // ─── Global plan: statuses ─────────────────────────────────────────────────

    public ResponseEntity<Object> getSubscriptionStatuses() {
        try {
            List<StatusInfo> statuses = statusJPARepo.findByType(STATUS_TYPE).stream()
                    .map(this::toStatusInfo)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ResponseEntityObject<>(true, "Statuses fetched.", statuses));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to fetch statuses: " + e.getMessage()));
        }
    }

    // ─── Global plan: list all ─────────────────────────────────────────────────

    public ResponseEntity<Object> listSubscriptions() {
        try {
            List<SubscriptionResponse> list = subscriptionJPARepo.findAll().stream()
                    .map(this::toSubscriptionResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ResponseEntityObject<>(true, "Subscriptions fetched.", list));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to fetch subscriptions: " + e.getMessage()));
        }
    }

    // ─── Global plan: list active only ────────────────────────────────────────

    public ResponseEntity<Object> listActiveSubscriptions() {
        try {
            List<SubscriptionResponse> list = subscriptionJPARepo.findAll().stream()
                    .filter(sub -> sub.getStatus() != null
                            // && sub.getStatus().getId() != null
                            && sub.getStatus().getId() == ConstantKey.ACTIVE_STATUS_ID)
                    .map(this::toSubscriptionResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ResponseEntityObject<>(true, "Active subscriptions fetched.", list));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to fetch active subscriptions: " + e.getMessage()));
        }
    }

    // ─── Global plan: create ───────────────────────────────────────────────────

    @Transactional
    public ResponseEntity<Object> createSubscription(SubscriptionRequest request) {
        try {
            if (request.getPlanName() == null || request.getPlanName().isBlank())
                return ResponseEntity.badRequest().body(
                        new ResponseEntityObject<>(false, "Plan name is required."));

            // Default to ACTIVE status when no statusId is provided
            Long statusId = request.getStatusId() != null ? request.getStatusId() : ConstantKey.ACTIVE_STATUS_ID;
            Status status = resolveStatus(statusId);
            if (status == null)
                return ResponseEntity.badRequest().body(
                        new ResponseEntityObject<>(false, "Status not found."));

            Subscription sub = new Subscription();
            sub.setPlanName(request.getPlanName());
            sub.setPrice(request.getPrice());
            sub.setCurrency("INR");
            sub.setStatus(status);
            sub.setDurationMonths(request.getDurationMonths());
            if (request.getNotes() != null)
                sub.setNotes(request.getNotes());

            subscriptionJPARepo.save(sub);
            return ResponseEntity
                    .ok(new ResponseEntityObject<>(true, "Subscription created.", toSubscriptionResponse(sub)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to create subscription: " + e.getMessage()));
        }
    }

    // ─── Global plan: get by id ───────────────────────────────────────────────

    public ResponseEntity<Object> getSubscription(UUID id) {
        try {
            Subscription sub = subscriptionJPARepo.findById(id).orElse(null);
            if (sub == null)
                return ResponseEntity.status(404).body(
                        new ResponseEntityObject<>(false, "Subscription not found."));
            return ResponseEntity
                    .ok(new ResponseEntityObject<>(true, "Subscription fetched.", toSubscriptionResponse(sub)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to fetch subscription: " + e.getMessage()));
        }
    }

    // ─── Global plan: update ──────────────────────────────────────────────────

    @Transactional
    public ResponseEntity<Object> updateSubscription(UUID id, SubscriptionRequest request) {
        try {
            Subscription sub = subscriptionJPARepo.findById(id).orElse(null);
            if (sub == null)
                return ResponseEntity.status(404).body(
                        new ResponseEntityObject<>(false, "Subscription not found."));

            if (request.getPlanName() != null && !request.getPlanName().isBlank())
                sub.setPlanName(request.getPlanName());
            if (request.getPrice() != null)
                sub.setPrice(request.getPrice());
            if (request.getDurationMonths() != null)
                sub.setDurationMonths(request.getDurationMonths());
            if (request.getStatusId() != null) {
                Status status = resolveStatus(request.getStatusId());
                if (status != null)
                    sub.setStatus(status);
            }
            if (request.getNotes() != null)
                sub.setNotes(request.getNotes());

            subscriptionJPARepo.save(sub);
            return ResponseEntity
                    .ok(new ResponseEntityObject<>(true, "Subscription updated.", toSubscriptionResponse(sub)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to update subscription: " + e.getMessage()));
        }
    }

    // ─── Global plan: toggle status ───────────────────────────────────────────

    @Transactional
    public ResponseEntity<Object> toggleStatus(UUID id) {
        try {
            Subscription sub = subscriptionJPARepo.findById(id).orElse(null);
            if (sub == null)
                return ResponseEntity.status(404).body(
                        new ResponseEntityObject<>(false, "Subscription not found."));

            Long currentId = sub.getStatus() != null ? sub.getStatus().getId() : null;
            long newStatusId = (currentId != null && currentId == ConstantKey.INACTIVE_STATUS_ID)
                    ? ConstantKey.ACTIVE_STATUS_ID
                    : ConstantKey.INACTIVE_STATUS_ID;

            Status newStatus = resolveStatus(newStatusId);
            if (newStatus != null)
                sub.setStatus(newStatus);

            subscriptionJPARepo.save(sub);
            return ResponseEntity.ok(new ResponseEntityObject<>(true, "Status toggled.", toSubscriptionResponse(sub)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to toggle status: " + e.getMessage()));
        }
    }

    // ─── Global plan: delete ──────────────────────────────────────────────────

    @Transactional
    public ResponseEntity<Object> deleteSubscription(UUID id) {
        try {
            if (!subscriptionJPARepo.existsById(id))
                return ResponseEntity.status(404).body(
                        new ResponseEntityObject<>(false, "Subscription not found."));
            subscriptionJPARepo.deleteById(id);
            return ResponseEntity.ok(new ResponseEntityObject<>(true, "Subscription deleted."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to delete subscription: " + e.getMessage()));
        }
    }

    // ─── Org subscription: get ────────────────────────────────────────────────

    public ResponseEntity<Object> getOrgSubscription(UUID orgId) {
        try {
            OrgSubscription orgSub = orgSubscriptionJPARepo
                    .findTopByOrgIdOrderByCreatedDateDesc(orgId).orElse(null);
            if (orgSub == null)
                return ResponseEntity.status(404).body(
                        new ResponseEntityObject<>(false, "No subscription found for this organisation."));
            return ResponseEntity.ok(new ResponseEntityObject<>(true, "Subscription fetched.", toOrgResponse(orgSub)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to fetch subscription: " + e.getMessage()));
        }
    }

    // ─── Org subscription: assign / update ────────────────────────────────────

    @Transactional
    public ResponseEntity<Object> createOrUpdateOrgSubscription(UUID orgId, OrgSubscriptionRequest request) {
        try {
            if (request.getSubscriptionId() == null)
                return ResponseEntity.badRequest().body(
                        new ResponseEntityObject<>(false, "subscriptionId is required."));

            Subscription plan = subscriptionJPARepo.findById(request.getSubscriptionId()).orElse(null);
            if (plan == null)
                return ResponseEntity.badRequest().body(
                        new ResponseEntityObject<>(false, "Subscription plan not found."));

            OrgSubscription existing = orgSubscriptionJPARepo
                    .findTopByOrgIdOrderByCreatedDateDesc(orgId).orElse(null);

            if (existing != null) {
                existing.setSubscription(plan);
                existing.setPlatformPlan(plan.getPlanName());
                existing.setPlatformPrice(request.getPriceOverride() != null ? request.getPriceOverride()
                        : (plan.getPrice() != null ? plan.getPrice() : BigDecimal.ZERO));
                if (request.getCurrency() != null)
                    existing.setCurrency(request.getCurrency());
                if (request.getMaxUsers() != null)
                    existing.setMaxUsers(request.getMaxUsers());
                if (request.getNotes() != null)
                    existing.setNotes(request.getNotes());
                if (request.getPeriodStart() != null)
                    existing.setPeriodStart(request.getPeriodStart());
                if (request.getPeriodEnd() != null)
                    existing.setPeriodEnd(request.getPeriodEnd());
                orgSubscriptionJPARepo.save(existing);
                return ResponseEntity
                        .ok(new ResponseEntityObject<>(true, "Subscription updated.", toOrgResponse(existing)));
            } else {
                OrgSubscription orgSub = new OrgSubscription();
                orgSub.setSubscription(plan);
                orgSub.setOrgId(orgId);
                orgSub.setPlatformPlan(plan.getPlanName());
                orgSub.setPlatformPrice(request.getPriceOverride() != null ? request.getPriceOverride()
                        : (plan.getPrice() != null ? plan.getPrice() : BigDecimal.ZERO));
                orgSub.setCurrency(plan.getCurrency() != null ? plan.getCurrency() : "INR");
                orgSub.setMaxUsers(request.getMaxUsers());
                orgSub.setNotes(request.getNotes());
                if (request.getPeriodStart() != null)
                    orgSub.setPeriodStart(request.getPeriodStart());
                if (request.getPeriodEnd() != null)
                    orgSub.setPeriodEnd(request.getPeriodEnd());
                orgSubscriptionJPARepo.save(orgSub);
                return ResponseEntity
                        .ok(new ResponseEntityObject<>(true, "Subscription assigned.", toOrgResponse(orgSub)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to save subscription: " + e.getMessage()));
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private Status resolveStatus(Long statusId) {
        return statusId != null ? statusJPARepo.findById(statusId).orElse(null) : null;
    }

    private StatusInfo toStatusInfo(Status s) {
        if (s == null)
            return null;
        return StatusInfo.builder().id(s.getId()).name(s.getName()).colourCode(s.getColourCode()).build();
    }

    private boolean isActive(Status s) {
        if (s == null)
            return false;
        String name = s.getName();
        return name != null && (name.equalsIgnoreCase("ACTIVE") || name.equalsIgnoreCase("TRIAL"));
    }

    private SubscriptionResponse toSubscriptionResponse(Subscription sub) {
        return SubscriptionResponse.builder()
                .id(sub.getId())
                .planName(sub.getPlanName())
                .price(sub.getPrice())
                .currency(sub.getCurrency())
                .status(toStatusInfo(sub.getStatus()))
                .durationMonths(sub.getDurationMonths())
                .notes(sub.getNotes())
                .build();
    }

    private OrgSubscriptionResponse toOrgResponse(OrgSubscription orgSub) {
        Subscription sub = orgSub.getSubscription();
        Status status = sub != null ? sub.getStatus() : null;
        return OrgSubscriptionResponse.builder()
                .orgSubscriptionId(orgSub.getId())
                .subscriptionId(sub != null ? sub.getId() : null)
                .planName(orgSub.getPlatformPlan() != null ? orgSub.getPlatformPlan()
                        : (sub != null ? sub.getPlanName() : null))
                .price(orgSub.getPlatformPrice())
                .currency(orgSub.getCurrency())
                .status(toStatusInfo(status))
                .maxUsers(orgSub.getMaxUsers())
                .periodStart(orgSub.getPeriodStart())
                .periodEnd(orgSub.getPeriodEnd())
                .notes(orgSub.getNotes())
                .active(isActive(status))
                .build();
    }
}
