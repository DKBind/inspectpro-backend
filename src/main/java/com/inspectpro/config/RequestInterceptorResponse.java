package com.inspectpro.config;

import lombok.Data;

public @Data class RequestInterceptorResponse {

    private String uuid;
    private String email;
    private String role;
    private String roleId;
}
