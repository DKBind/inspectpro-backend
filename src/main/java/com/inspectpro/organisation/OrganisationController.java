package com.inspectpro.organisation;

import com.inspectpro.common.request.OrganisationCreateRequest;
import com.inspectpro.common.request.RequestInterceptorReq;
import com.inspectpro.common.response.OrganisationResponse;
import com.inspectpro.config.RequestInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> createOrganisation(@RequestInterceptor RequestInterceptorReq req,
            @RequestBody OrganisationCreateRequest request) {

        String userUuid = req.getUuid();
        return organisationService.createOrganisation(request, userUuid);
    }
}
