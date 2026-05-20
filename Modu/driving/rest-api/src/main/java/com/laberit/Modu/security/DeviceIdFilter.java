package com.laberit.Modu.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laberit.Modu.rest.advice.ErrorType;
import com.laberit.Modu.rest.advice.RestAuthenticationEntryPoint;
import com.laberit.Modu.rest.generated.model.ErrorResponse;
import com.laberit.Modu.rest.generated.model.ErrorResponseFieldsInner;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeviceIdFilter extends OncePerRequestFilter {
    private final AuthenticationEntryPoint authenticationEntryPoint;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("DeviceId ")) {
            String deviceId = header.substring(9);
            if (deviceId.matches("\\d{15}")) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        deviceId, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
                request.setAttribute(RestAuthenticationEntryPoint.AUTH_ERROR, ErrorType.VALIDATION_ERROR);
                String errorDetails = "Invalid IMEI format. Expected 15 digits.";
                request.setAttribute(RestAuthenticationEntryPoint.AUTH_ERROR_DETAILS, errorDetails);
                authenticationEntryPoint.commence(request, response, new BadCredentialsException(errorDetails));
                return;
            }

            filterChain.doFilter(request, response);
        }
    }

}
