package com.inspectpro.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inspectpro.organisation.OrganisationJPARepo;
import com.inspectpro.organisation.SubscriptionJPARepo;
import com.inspectpro.role.CommonOrgRoleJPARepo;
import com.inspectpro.role.RoleJPARepo;
import com.inspectpro.user.UserJPARepo;

@Component
public class CentralService {

    @Autowired
    public HelperExtension helperExtension;

    @Autowired
    public ConstantExtension constantExtension;

    @Autowired
    public UserJPARepo userJPARepo;

    @Autowired
    public RoleJPARepo roleJPARepo;

    @Autowired
    public OrganisationJPARepo organisationJPARepo;

    @Autowired
    public CommonOrgRoleJPARepo commonOrgRoleJPARepo;

    @Autowired
    public SubscriptionJPARepo subscriptionJPARepo;

}
