package com.inspectpro.organisation;

import com.inspectpro.common.entities.CommonOrgRole;
import com.inspectpro.common.entities.OrgSubscription;
import com.inspectpro.common.entities.Organisations;
import com.inspectpro.common.entities.Role;
import com.inspectpro.common.entities.Subscription;
import com.inspectpro.common.enums.PlanType;
import com.inspectpro.common.enums.RoleScope;
import com.inspectpro.common.enums.SubscriptionStatus;
import com.inspectpro.common.enums.SubscriptionType;
import com.inspectpro.common.request.OrganisationCreateRequest;
import com.inspectpro.common.response.OrganisationResponse;
import com.inspectpro.role.CommonOrgRoleJPARepo;
import com.inspectpro.role.RoleJPARepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {

    private final OrganisationJPARepo organisationJPARepo;
    private final CommonOrgRoleJPARepo commonOrgRoleJPARepo;
    private final RoleJPARepo roleJPARepo;
    private final SubscriptionJPARepo subscriptionJPARepo;

    @Override
    @Transactional
    public OrganisationResponse createOrganisation(OrganisationCreateRequest request) {
        // Step A: Insert the new record into the Organisations table
        Organisations org = new Organisations();
        org.setName(request.getName());
        org.setSlug(request.getSlug());
        org.setDomain(request.getDomain());
        org.setPlanType(request.getPlanType() != null ? request.getPlanType().name() : null);
        org.setUuid(UUID.randomUUID());
        org.setCreatedAt(LocalDateTime.now());
        org.setUpdatedAt(LocalDateTime.now());

        Organisations savedOrg = organisationJPARepo.save(org);

        // Step B (Role Seeding): Automatically fetch all records from common_org_role
        // and clone/insert them into the Role table for the new Org ID
        List<CommonOrgRole> commonRoles = commonOrgRoleJPARepo.findByIsActiveTrue();
        if (commonRoles != null && !commonRoles.isEmpty()) {
            for (CommonOrgRole cr : commonRoles) {
                Role newRole = new Role();
                newRole.setName(cr.getName());
                newRole.setDescription(cr.getDescription());
                newRole.setDesignation(cr.getDesignation());
                newRole.setSystemRole(true);
                newRole.setScope(RoleScope.ORGANISATION);
                newRole.setOrganisation(savedOrg);
                newRole.setCreatedAt(LocalDateTime.now());
                roleJPARepo.save(newRole);
            }
        }

        // Step C (Custom Roles): Optional array of custom roles during creation
        if (request.getCustomRoles() != null && !request.getCustomRoles().isEmpty()) {
            for (OrganisationCreateRequest.CustomRoleRequest customReq : request.getCustomRoles()) {
                Role customRole = new Role();
                customRole.setName(customReq.getName());
                customRole.setDescription(customReq.getDescription());
                customRole.setDesignation(customReq.getDesignation());
                customRole.setSystemRole(false);
                customRole.setScope(RoleScope.ORGANISATION);
                customRole.setOrganisation(savedOrg);
                customRole.setCreatedAt(LocalDateTime.now());
                roleJPARepo.save(customRole);
            }
        }

        // Step D (Subscription): Initialize a record in Subscriptions and
        // OrgSubscription
        Subscription subscription = new Subscription();
        subscription.setOwnerId(savedOrg.getUuid());
        subscription.setSubscriptionType(SubscriptionType.ORGANISATION);
        subscription.setStatus(SubscriptionStatus.TRIAL);
        subscription.setCurrentPeriodStart(LocalDateTime.now());

        OrgSubscription orgSubscription = new OrgSubscription();
        orgSubscription.setOrgId(savedOrg.getUuid());
        orgSubscription.setPlatformPlan(request.getPlanType() != null ? request.getPlanType() : PlanType.FREE);
        orgSubscription.setPlatformPrice(BigDecimal.ZERO);
        orgSubscription.setCurrency("INR");

        // Setup bi-directional one-to-one
        orgSubscription.setSubscription(subscription);
        // subscription.setOrgSubscription(orgSubscription);
        subscription.setOrgSubscription(List.of(orgSubscription));

        Subscription savedSubscription = subscriptionJPARepo.save(subscription);

        // Prepare and return response
        return OrganisationResponse.builder()
                .id(savedOrg.getId())
                .uuid(savedOrg.getUuid())
                .name(savedOrg.getName())
                .slug(savedOrg.getSlug())
                .domain(savedOrg.getDomain())
                .planType(request.getPlanType())
                .isActive(savedOrg.getIsActive() != null ? savedOrg.getIsActive() : true)
                .subscriptionId(savedSubscription.getSubscriptionId())
                .createdAt(savedOrg.getCreatedAt())
                .build();
    }
}
