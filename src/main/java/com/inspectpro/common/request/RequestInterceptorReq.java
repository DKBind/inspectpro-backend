package com.inspectpro.common.request;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class RequestInterceptorReq {

    private String uuid;
    private String name;
    private String email;
    private String role;
    private String roleId;
    private String statusResponse;
    private String httpCustomStatus;
    private int isStatusValid;

    private HttpStatus httpStatus;

}
