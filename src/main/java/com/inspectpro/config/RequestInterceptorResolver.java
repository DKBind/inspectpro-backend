package com.inspectpro.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.inspectpro.common.request.RequestInterceptorReq;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class RequestInterceptorResolver implements HandlerMethodArgumentResolver{

     @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestInterceptor.class) != null
                && parameter.getParameterType().equals(RequestInterceptorReq.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        RequestInterceptorReq dto = new RequestInterceptorReq();

        dto.setEmail((String) request.getAttribute("email"));
        dto.setUuid((String) request.getAttribute("uuid"));
        dto.setRole((String) request.getAttribute("role"));
        dto.setRoleId((String) request.getAttribute("roleId"));
//        dto.setLogoutTime((String) request.getAttribute("logoutTime"));
//        dto.setWarningBeforeTime((String) request.getAttribute("warningBeforeTime"));

//        Object logoutAtAttr = request.getAttribute("logoutAt");
//        if (logoutAtAttr instanceof Number) {
//            dto.setLogoutAt(((Number) logoutAtAttr).longValue());
//        }

        return dto;
    }
    
}
