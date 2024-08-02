package com.rishabh.SampleApplication.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rishabh.SampleApplication.model.dto.ErrorDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserAuthEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws ServletException, IOException {

        int status = HttpServletResponse.SC_UNAUTHORIZED;
        String message = "Unauthorized access";

        if (authException instanceof InsufficientAuthenticationException) {
            message = "Insufficient authentication details provided";
        } else if (authException instanceof BadCredentialsException) {
            message = "Invalid username or password";
        } else if (authException instanceof AccountExpiredException) {
            message = "Your account has expired";
        } else if (authException instanceof CredentialsExpiredException) {
            message = "Your credentials have expired";
        } else if (authException instanceof DisabledException) {
            message = "Your account is disabled";
        } else if (authException instanceof LockedException) {
            message = "Your account is locked";
        } else if (authException instanceof SessionAuthenticationException) {
            message = "Your account is already logged in elsewhere";
            status = HttpServletResponse.SC_CONFLICT;
        } else if (authException instanceof InternalAuthenticationServiceException) {
            message = "Internal error during authentication process";
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        } else if (authException instanceof AuthenticationServiceException) {
            message = "Unexpected error during authentication";
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        } else if (authException instanceof Exception) {
            message = "Specific message related to SomeOtherAuthenticationException";
            status = HttpServletResponse.SC_FORBIDDEN; // Example of changing status based on exception type
        }
        // Additional specific exceptions can be handled here

        SecurityContextHolder.clearContext();
        response.setStatus(status);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        OBJECT_MAPPER.writeValue(response.getOutputStream(), new ErrorDto(message));
    }
}
