package com.inspectpro.helper;

import org.springframework.stereotype.Component;

@Component
public class ConstantExtension {

    public static final String ORGANISATION = "ORGANISATION";
    public static final String ROLE = "ROLE";

    public static final String FREE = "FREE";
    public static final String STARTER = "STARTER";
    public static final String PRO = "PRO";
    public static final String ENTERPRISE = "ENTERPRISE";

    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";
    public static final String SUSPENDED = "SUSPENDED";
    public static final String PENDING_SETUP = "PENDING_SETUP";

    public static final long ACTIVE_STATUS_ID = 1;
    public static final long INACTIVE_STATUS_ID = 2;

}
