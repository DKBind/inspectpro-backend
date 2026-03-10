package com.inspectpro.organisation;

import com.inspectpro.common.request.OrganisationCreateRequest;
import com.inspectpro.common.response.OrganisationResponse;

public interface OrganisationService {
    OrganisationResponse createOrganisation(OrganisationCreateRequest request);
}
