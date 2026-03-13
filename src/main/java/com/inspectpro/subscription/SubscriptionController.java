package com.inspectpro.subscription;

import com.inspectpro.common.request.OrgSubscriptionRequest;
import com.inspectpro.common.request.RequestInterceptorReq;
import com.inspectpro.common.request.SubscriptionRequest;
import com.inspectpro.config.RequestInterceptor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * Global subscription-plan endpoints  →  /subscriptions
 * Org-level subscription endpoints    →  /organisations/{orgUuid}/subscription
 */
@RestController
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // ─── Global plan endpoints ────────────────────────────────────────────────

    @GetMapping("/subscriptions/statuses")
    public ResponseEntity<Object> getStatuses(@RequestInterceptor RequestInterceptorReq req) {
        return subscriptionService.getSubscriptionStatuses();
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<Object> listSubscriptions(@RequestInterceptor RequestInterceptorReq req) {
        return subscriptionService.listSubscriptions();
    }

    @GetMapping("/subscriptions/active")
    public ResponseEntity<Object> listActiveSubscriptions(@RequestInterceptor RequestInterceptorReq req) {
        return subscriptionService.listActiveSubscriptions();
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<Object> createSubscription(
            @RequestInterceptor RequestInterceptorReq req,
            @RequestBody SubscriptionRequest request) {
        return subscriptionService.createSubscription(request);
    }

    @GetMapping("/subscriptions/{id}")
    public ResponseEntity<Object> getSubscription(
            @RequestInterceptor RequestInterceptorReq req,
            @PathVariable UUID id) {
        return subscriptionService.getSubscription(id);
    }

    @PutMapping("/subscriptions/{id}")
    public ResponseEntity<Object> updateSubscription(
            @RequestInterceptor RequestInterceptorReq req,
            @PathVariable UUID id,
            @RequestBody SubscriptionRequest request) {
        return subscriptionService.updateSubscription(id, request);
    }

    @PatchMapping("/subscriptions/{id}/status")
    public ResponseEntity<Object> toggleSubscriptionStatus(
            @RequestInterceptor RequestInterceptorReq req,
            @PathVariable UUID id) {
        return subscriptionService.toggleStatus(id);
    }

    @DeleteMapping("/subscriptions/{id}")
    public ResponseEntity<Object> deleteSubscription(
            @RequestInterceptor RequestInterceptorReq req,
            @PathVariable UUID id) {
        return subscriptionService.deleteSubscription(id);
    }

    // ─── Org-level endpoints ──────────────────────────────────────────────────

    @GetMapping("/organisations/{orgUuid}/subscription")
    public ResponseEntity<Object> getOrgSubscription(
            @RequestInterceptor RequestInterceptorReq req,
            @PathVariable UUID orgUuid) {
        return subscriptionService.getOrgSubscription(orgUuid);
    }

    @PostMapping("/organisations/{orgUuid}/subscription")
    public ResponseEntity<Object> assignOrgSubscription(
            @RequestInterceptor RequestInterceptorReq req,
            @PathVariable UUID orgUuid,
            @RequestBody OrgSubscriptionRequest request) {
        return subscriptionService.createOrUpdateOrgSubscription(orgUuid, request);
    }
}
