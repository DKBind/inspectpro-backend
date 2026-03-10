package com.inspectpro.organisation;

import com.inspectpro.common.request.OrganisationCreateRequest;
import com.inspectpro.common.response.OrganisationResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/organisations")
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationService organisationService;

    @PostMapping
    public ResponseEntity<OrganisationResponse> createOrganisation(@RequestBody OrganisationCreateRequest request) {
        OrganisationResponse response = organisationService.createOrganisation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
