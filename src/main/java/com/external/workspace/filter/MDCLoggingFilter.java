package com.external.workspace.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Order(1)
@Component
public class MDCLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "request-id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestId = getRequestId(request);
        try {
            MDC.put(REQUEST_ID, requestId);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            MDC.remove(REQUEST_ID);
        }
    }

    private String getRequestId(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(REQUEST_ID))
                .orElse(UUID.randomUUID().toString());
    }
}
