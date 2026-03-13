package com.inspectpro.organisation;

import com.inspectpro.common.request.OrganisationCreateRequest;
import com.inspectpro.common.request.OrganisationStatusUpdateRequest;
import com.inspectpro.common.request.OrganisationUpdateRequest;
import com.inspectpro.common.request.RequestInterceptorReq;
import com.inspectpro.config.RequestInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RestController
@RequestMapping("/organisations")
@RequiredArgsConstructor
public class OrganisationController {

    private static final Logger log = LoggerFactory.getLogger(OrganisationController.class);

    private final OrganisationService organisationService;

    @PostMapping
    public ResponseEntity<Object> createOrganisation(
            @RequestInterceptor RequestInterceptorReq req,
            @RequestBody OrganisationCreateRequest request) {
        log.info(
                "CREATE ORG REQUEST → name={}, email={}, phone={}, contactPerson={}, gstin={}, pan={}, tan={}, address={}",
                request.getName(), request.getEmail(),
                request.getPhoneNumber(), request.getContactedPersonName(),
                request.getGstin(), request.getPan(), request.getTan(),
                request.getAddress());
        return organisationService.createOrganisation(request, req.getUuid());
    }

    @GetMapping
    public ResponseEntity<Object> getOrganisations(
            @RequestInterceptor RequestInterceptorReq req,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return organisationService.getOrganisations(page, size);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Object> getOrganisationByUuid(
            @RequestInterceptor RequestInterceptorReq req,
            @PathVariable UUID uuid) {
        return organisationService.getOrganisationByUuid(uuid);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Object> updateOrganisation(
            @RequestInterceptor RequestInterceptorReq req,
            @PathVariable UUID uuid,
            @RequestBody OrganisationUpdateRequest request) {
        return organisationService.updateOrganisation(uuid, request);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Object> deleteOrganisation(
            @RequestInterceptor RequestInterceptorReq req,
            @PathVariable UUID uuid) {
        return organisationService.deleteOrganisation(uuid);
    }

    @PatchMapping("/{uuid}/status")
    public ResponseEntity<Object> updateOrganisationStatus(
            @RequestInterceptor RequestInterceptorReq req,
            @PathVariable UUID uuid,
            @RequestBody OrganisationStatusUpdateRequest request) {
        return organisationService.updateOrganisationStatus(uuid, request);
    }
}
