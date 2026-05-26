package com.laberit.Modu.security;

import com.laberit.Modu.rest.advice.ErrorType;
import com.laberit.Modu.rest.advice.RestAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceIdFilter extends OncePerRequestFilter {
    private final AuthenticationEntryPoint authenticationEntryPoint;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String deviceId = request.getHeader("Authorization");
        if (deviceId != null) {
            if (deviceId.length()==16 || deviceId.length() == 32) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(deviceId, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
                request.setAttribute(RestAuthenticationEntryPoint.AUTH_ERROR, ErrorType.VALIDATION_ERROR);
                String errorDetails = "Invalid device ID format. Device ID length should be 16 or 32 characters.";
                request.setAttribute(RestAuthenticationEntryPoint.AUTH_ERROR_DETAILS, errorDetails);
                authenticationEntryPoint.commence(request, response, new BadCredentialsException(errorDetails));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
