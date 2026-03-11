package com.inspectpro.organisation;

import com.inspectpro.common.entities.CommonOrgRole;
import com.inspectpro.common.entities.OrgSubscription;
import com.inspectpro.common.entities.Organisations;
import com.inspectpro.common.entities.Role;
import com.inspectpro.common.entities.Subscription;
import com.inspectpro.common.enums.PlanType;
import com.inspectpro.common.enums.RoleScope;
import com.inspectpro.common.enums.SubscriptionStatus;
import com.inspectpro.common.request.OrganisationCreateRequest;
import com.inspectpro.common.response.OrganisationResponse;
import com.inspectpro.helper.CentralService;
import com.inspectpro.helper.ResponseEntityObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganisationService extends CentralService {

    @Transactional
    public ResponseEntity<Object> createOrganisation(
            OrganisationCreateRequest request,
            String userUuid) {

        try {

            LocalDateTime now = LocalDateTime.now();

            // Step A: Create Organisation
            Organisations org = new Organisations();
            org.setName(request.getName());
            org.setSlug(request.getSlug());
            org.setDomain(request.getDomain());
            org.setPlanType(request.getPlanType() != null ? request.getPlanType().name() : null);

            Organisations savedOrg = organisationJPARepo.save(org);

            // Step B + C: Seed Roles
            List<Role> rolesToSave = new ArrayList<>();

            List<CommonOrgRole> commonRoles = commonOrgRoleJPARepo.findByIsActiveTrue();

            for (CommonOrgRole cr : commonRoles) {
                rolesToSave.add(createRole(
                        cr.getName(),
                        cr.getDescription(),
                        cr.getDesignation(),
                        true,
                        savedOrg,
                        now));
            }

            if (request.getCustomRoles() != null && !request.getCustomRoles().isEmpty()) {
                for (OrganisationCreateRequest.CustomRoleRequest cr : request.getCustomRoles()) {
                    rolesToSave.add(createRole(
                            cr.getName(),
                            cr.getDescription(),
                            cr.getDesignation(),
                            false,
                            savedOrg,
                            now));
                }
            }

            if (!rolesToSave.isEmpty()) {
                roleJPARepo.saveAll(rolesToSave);
            }

            // Step D: Create Subscription
            PlanType planType = request.getPlanType() != null ? request.getPlanType() : PlanType.FREE;

            OrgSubscription orgSubscription = new OrgSubscription();
            orgSubscription.setPlatformPlan(planType);
            orgSubscription.setPlatformPrice(BigDecimal.ZERO);
            orgSubscription.setCurrency("INR");

            Subscription subscription = new Subscription();
            subscription.setStatus(SubscriptionStatus.TRIAL);
            subscription.setCurrentPeriodStart(now);
            subscription.setCurrentPeriodEnd(now.plusDays(30));

            orgSubscription.setSubscription(subscription);
            subscription.setOrgSubscription(List.of(orgSubscription));

            subscriptionJPARepo.save(subscription);

            OrganisationResponse orgResponse = OrganisationResponse.builder()
                    .uuid(savedOrg.getId())
                    .name(savedOrg.getName())
                    .slug(savedOrg.getSlug())
                    .domain(savedOrg.getDomain())
                    .planType(planType)
                    .isActive(savedOrg.getIsActive())
                    .createdAt(now)
                    .build();

            return ResponseEntity.ok(
                    new ResponseEntityObject<>(true, "Organisation Created Successfully.", orgResponse));

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to create organisation: " + e.getMessage()));
        }
    }

    private Role createRole(
            String name,
            String description,
            String designation,
            boolean systemRole,
            Organisations org,
            LocalDateTime now) {

        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setDesignation(designation);
        role.setSystemRole(systemRole);
        role.setScope(RoleScope.ORGANISATION);
        role.setOrganisation(org);
        role.setCreatedAt(now);

        return role;
    }
}
