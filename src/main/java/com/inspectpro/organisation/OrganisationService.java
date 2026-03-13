package com.inspectpro.organisation;

import com.inspectpro.common.entities.CommonOrgRole;
import com.inspectpro.common.entities.OrgSubscription;
import com.inspectpro.common.entities.OrganisationAddress;
import com.inspectpro.common.entities.Organisations;
import com.inspectpro.common.entities.Role;
import com.inspectpro.common.entities.Status;
import com.inspectpro.common.entities.Subscription;
import com.inspectpro.common.enums.RoleScope;
import com.inspectpro.common.request.OrganisationCreateRequest;
import com.inspectpro.common.request.OrganisationStatusUpdateRequest;
import com.inspectpro.common.request.OrganisationUpdateRequest;
import com.inspectpro.common.response.OrganisationResponse;
import com.inspectpro.common.response.PageResponse;
import com.inspectpro.helper.CentralService;
import com.inspectpro.helper.ConstantKey;
import com.inspectpro.helper.ResponseEntityObject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganisationService extends CentralService {

    private final OrgSubscriptionJPARepo orgSubscriptionJPARepo;

    // ─── Create ───────────────────────────────────────────────────────────────

    @Transactional
    public ResponseEntity<Object> createOrganisation(OrganisationCreateRequest request, String userUuid) {
        try {
            if (organisationJPARepo.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(
                        new ResponseEntityObject<>(false, "Email already exists"));
            }

            // Validate subscription plan
            if (request.getSubscriptionId() == null) {
                return ResponseEntity.badRequest().body(
                        new ResponseEntityObject<>(false, "subscriptionId is required"));
            }
            Subscription plan = subscriptionJPARepo.findById(request.getSubscriptionId()).orElse(null);
            if (plan == null) {
                return ResponseEntity.badRequest().body(
                        new ResponseEntityObject<>(false, "Subscription plan not found"));
            }

            LocalDateTime now = LocalDateTime.now();

            // Step A: Resolve active status from DB
            Status activeStatus = statusJPARepo.findById(ConstantKey.ACTIVE_STATUS_ID).orElse(null);
            if (activeStatus == null) {
                return ResponseEntity.internalServerError().body(
                        new ResponseEntityObject<>(false, "Active status not configured in DB"));
            }

            // Step B: Create Organisation
            Organisations org = new Organisations();
            org.setName(request.getName());
            org.setEmail(request.getEmail());
            org.setDomain(request.getDomain());
            org.setPlanType(plan.getPlanName());   // DB-driven — taken from the subscription plan
            org.setIsActive(true);
            org.setStatus(activeStatus);
            org.setPhoneNumber(request.getPhoneNumber());
            org.setContactedPersonName(request.getContactedPersonName());
            org.setGstin(request.getGstin());
            org.setPan(request.getPan());
            org.setTan(request.getTan());

            if (request.getAddress() != null) {
                org.setAddress(buildAddress(request.getAddress()));
            }

            Organisations savedOrg = organisationJPARepo.save(org);

            // Step C: Seed Roles
            List<Role> rolesToSave = new ArrayList<>();
            List<CommonOrgRole> commonRoles = commonOrgRoleJPARepo.findByIsActiveTrue();
            for (CommonOrgRole cr : commonRoles) {
                rolesToSave.add(createRole(cr.getName(), cr.getDescription(), cr.getDesignation(), true, savedOrg, now));
            }
            if (request.getCustomRoles() != null && !request.getCustomRoles().isEmpty()) {
                for (OrganisationCreateRequest.CustomRoleRequest cr : request.getCustomRoles()) {
                    rolesToSave.add(createRole(cr.getName(), cr.getDescription(), cr.getDesignation(), false, savedOrg, now));
                }
            }
            if (!rolesToSave.isEmpty()) {
                roleJPARepo.saveAll(rolesToSave);
            }

            // Step D: Assign subscription plan to the org
            LocalDateTime periodStart = request.getSubscriptionStartDate() != null
                    ? request.getSubscriptionStartDate() : now;
            LocalDateTime periodEnd = (plan.getDurationMonths() != null)
                    ? periodStart.plusMonths(plan.getDurationMonths()) : null;

            OrgSubscription orgSubscription = new OrgSubscription();
            orgSubscription.setSubscription(plan);
            orgSubscription.setOrgId(savedOrg.getId());
            orgSubscription.setPlatformPlan(plan.getPlanName());
            orgSubscription.setPlatformPrice(plan.getPrice() != null ? plan.getPrice() : BigDecimal.ZERO);
            orgSubscription.setCurrency(plan.getCurrency() != null ? plan.getCurrency() : "INR");
            orgSubscription.setPeriodStart(periodStart);
            orgSubscription.setPeriodEnd(periodEnd);
            orgSubscriptionJPARepo.save(orgSubscription);

            return ResponseEntity.ok(
                    new ResponseEntityObject<>(true, "Organisation Created Successfully.", toResponse(savedOrg)));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to create organisation: " + e.getMessage()));
        }
    }

    // ─── Get All (Paginated) ──────────────────────────────────────────────────

    public ResponseEntity<Object> getOrganisations(int page, int size) {
        try {
            Page<Organisations> orgPage = organisationJPARepo.findAllNonDeleted(PageRequest.of(page, size));

            List<OrganisationResponse> content = orgPage.getContent().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());

            PageResponse<OrganisationResponse> pageResponse = PageResponse.<OrganisationResponse>builder()
                    .content(content)
                    .totalElements(orgPage.getTotalElements())
                    .totalPages(orgPage.getTotalPages())
                    .currentPage(orgPage.getNumber())
                    .pageSize(orgPage.getSize())
                    .build();

            return ResponseEntity.ok(
                    new ResponseEntityObject<>(true, "Organisations fetched.",
                            orgPage.getTotalElements(), pageResponse));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to fetch organisations: " + e.getMessage()));
        }
    }

    // ─── Get By UUID ──────────────────────────────────────────────────────────

    public ResponseEntity<Object> getOrganisationByUuid(UUID uuid) {
        try {
            Organisations org = organisationJPARepo.findById(uuid).orElse(null);
            if (org == null || Boolean.TRUE.equals(org.getIsDeleted())) {
                return ResponseEntity.status(404).body(
                        new ResponseEntityObject<>(false, "Organisation not found"));
            }
            return ResponseEntity.ok(new ResponseEntityObject<>(true, "Organisation fetched.", toResponse(org)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to fetch organisation: " + e.getMessage()));
        }
    }

    // ─── Update ───────────────────────────────────────────────────────────────

    @Transactional
    public ResponseEntity<Object> updateOrganisation(UUID uuid, OrganisationUpdateRequest request) {
        try {
            Organisations org = organisationJPARepo.findById(uuid).orElse(null);
            if (org == null || Boolean.TRUE.equals(org.getIsDeleted())) {
                return ResponseEntity.status(404).body(
                        new ResponseEntityObject<>(false, "Organisation not found"));
            }

            if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(org.getEmail())) {
                if (organisationJPARepo.existsByEmailAndIdNot(request.getEmail(), uuid)) {
                    return ResponseEntity.badRequest().body(
                            new ResponseEntityObject<>(false, "Email already in use by another organisation"));
                }
                org.setEmail(request.getEmail());
            }

            if (request.getName() != null) org.setName(request.getName());
            if (request.getDomain() != null) org.setDomain(request.getDomain());
            if (request.getPlanType() != null) org.setPlanType(request.getPlanType());
            if (request.getPhoneNumber() != null) org.setPhoneNumber(request.getPhoneNumber());
            if (request.getContactedPersonName() != null) org.setContactedPersonName(request.getContactedPersonName());
            if (request.getGstin() != null) org.setGstin(request.getGstin());
            if (request.getPan() != null) org.setPan(request.getPan());
            if (request.getTan() != null) org.setTan(request.getTan());

            if (request.getAddress() != null) {
                OrganisationAddress address = org.getAddress() != null ? org.getAddress() : new OrganisationAddress();
                OrganisationUpdateRequest.AddressRequest a = request.getAddress();
                if (a.getAddressLine1() != null) address.setAddressLine1(a.getAddressLine1());
                if (a.getAddressLine2() != null) address.setAddressLine2(a.getAddressLine2());
                if (a.getStreet() != null) address.setStreet(a.getStreet());
                if (a.getDistrict() != null) address.setDistrict(a.getDistrict());
                if (a.getState() != null) address.setState(a.getState());
                if (a.getCountry() != null) address.setCountry(a.getCountry());
                if (a.getPincode() != null) address.setPincode(a.getPincode());
                org.setAddress(address);
            }

            Organisations saved = organisationJPARepo.save(org);
            return ResponseEntity.ok(
                    new ResponseEntityObject<>(true, "Organisation updated successfully.", toResponse(saved)));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to update organisation: " + e.getMessage()));
        }
    }

    // ─── Delete (Soft) ────────────────────────────────────────────────────────

    @Transactional
    public ResponseEntity<Object> deleteOrganisation(UUID uuid) {
        try {
            Organisations org = organisationJPARepo.findById(uuid).orElse(null);
            if (org == null || Boolean.TRUE.equals(org.getIsDeleted())) {
                return ResponseEntity.status(404).body(
                        new ResponseEntityObject<>(false, "Organisation not found"));
            }
            org.setIsDeleted(true);
            org.setIsActive(false);
            organisationJPARepo.save(org);
            return ResponseEntity.ok(new ResponseEntityObject<>(true, "Organisation deleted successfully."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to delete organisation: " + e.getMessage()));
        }
    }

    // ─── Update Status ────────────────────────────────────────────────────────

    @Transactional
    public ResponseEntity<Object> updateOrganisationStatus(UUID uuid, OrganisationStatusUpdateRequest request) {
        try {
            Organisations org = organisationJPARepo.findById(uuid).orElse(null);
            if (org == null || Boolean.TRUE.equals(org.getIsDeleted())) {
                return ResponseEntity.status(404).body(
                        new ResponseEntityObject<>(false, "Organisation not found"));
            }
            Status status = statusJPARepo.findById(request.getStatusId()).orElse(null);
            if (status == null) {
                return ResponseEntity.badRequest().body(
                        new ResponseEntityObject<>(false, "Status not found"));
            }
            org.setStatus(status);
            org.setIsActive(!"INACTIVE".equalsIgnoreCase(status.getName()));
            Organisations saved = organisationJPARepo.save(org);
            return ResponseEntity.ok(
                    new ResponseEntityObject<>(true, "Organisation status updated successfully.", toResponse(saved)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    new ResponseEntityObject<>(false, "Failed to update status: " + e.getMessage()));
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private OrganisationResponse toResponse(Organisations org) {
        OrganisationResponse.OrganisationResponseBuilder builder = OrganisationResponse.builder()
                .uuid(org.getId())
                .name(org.getName())
                .email(org.getEmail())
                .domain(org.getDomain())
                .planType(org.getPlanType())   // plain String — DB-driven
                .isActive(Boolean.TRUE.equals(org.getIsActive()))
                .phoneNumber(org.getPhoneNumber())
                .contactedPersonName(org.getContactedPersonName())
                .gstin(org.getGstin())
                .pan(org.getPan())
                .tan(org.getTan())
                .createdAt(org.getCreatedDate());

        if (org.getStatus() != null) {
            builder.statusId(org.getStatus().getId())
                    .statusName(org.getStatus().getName())
                    .statusColourCode(org.getStatus().getColourCode());
        }

        if (org.getAddress() != null) {
            OrganisationAddress a = org.getAddress();
            builder.address(OrganisationResponse.AddressInfo.builder()
                    .id(a.getId())
                    .addressLine1(a.getAddressLine1())
                    .addressLine2(a.getAddressLine2())
                    .street(a.getStreet())
                    .district(a.getDistrict())
                    .state(a.getState())
                    .country(a.getCountry())
                    .pincode(a.getPincode())
                    .build());
        }

        return builder.build();
    }

    private OrganisationAddress buildAddress(OrganisationCreateRequest.AddressRequest a) {
        OrganisationAddress address = new OrganisationAddress();
        address.setAddressLine1(a.getAddressLine1());
        address.setAddressLine2(a.getAddressLine2());
        address.setStreet(a.getStreet());
        address.setDistrict(a.getDistrict());
        address.setState(a.getState());
        address.setCountry(a.getCountry());
        address.setPincode(a.getPincode());
        return address;
    }

    private Role createRole(String name, String description, String designation,
            boolean systemRole, Organisations org, LocalDateTime now) {
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
