package com.inspectpro.common.request;

import com.inspectpro.common.enums.PlanType;
import lombok.Data;
import java.util.List;

@Data
public class OrganisationCreateRequest {
    
    // Org Details
    private String name;
    private String slug;
    private String domain;
    
    // Subscription Details
    private PlanType planType;
    
    // Custom Roles (Optional)
    private List<CustomRoleRequest> customRoles;
    
    @Data
    public static class CustomRoleRequest {
        private String name;
        private String description;
        private String designation;
    }
}
