package com.laberit.Modu.authentification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laberit.Modu.rest.advice.ErrorType;
import com.laberit.Modu.rest.generated.model.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DeviceIdFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("DeviceId ")) {
            String deviceId = header.substring(9);
            request.setAttribute("deviceId", deviceId);
        } else {
            ErrorType errorType = ErrorType.AUTHENTIFICATION_NEEDED;
            ErrorResponse body = new ErrorResponse()
                    .type(errorType.name())
                    .message(errorType.getMessage())
                    .fields(null);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getOutputStream(), body);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui")
                || path.startsWith("/docs/v3")
                || path.startsWith(("/products"))
                || path.startsWith("/categories");
    }
}
