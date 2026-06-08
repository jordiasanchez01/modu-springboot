package com.laberit.Modu.security;

import com.laberit.Modu.rest.advice.ErrorType;
import com.laberit.Modu.rest.advice.RestAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceIdFilterTest {

    @Mock private AuthenticationEntryPoint authenticationEntryPoint;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @InjectMocks
    private DeviceIdFilter filter;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("Valid device IDs")
    class ValidDeviceIds {

        @ParameterizedTest(name = "device ID of length {0}")
        @ValueSource(strings = {
                "1234567890123456",           // exactly 16 chars
                "12345678901234567890123456789012" // exactly 32 chars
        })
        void shouldSetAuthentication_andProceedThroughChain(String deviceId) throws Exception {
            when(request.getHeader("Authorization")).thenReturn(deviceId);

            filter.doFilterInternal(request, response, filterChain);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            assertThat(auth).isNotNull();
            assertThat(auth.getName()).isEqualTo(deviceId);
            assertThat(auth.isAuthenticated()).isTrue();
            verify(filterChain).doFilter(request, response);
            verifyNoInteractions(authenticationEntryPoint);
        }
    }

    @Nested
    @DisplayName("Missing Authorization header")
    class MissingHeader {

        @Test
        void shouldNotSetAuthentication_butStillProceedThroughChain() throws Exception {
            when(request.getHeader("Authorization")).thenReturn(null);

            filter.doFilterInternal(request, response, filterChain);

            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
            verify(filterChain).doFilter(request, response);
            verifyNoInteractions(authenticationEntryPoint);
        }
    }

    @Nested
    @DisplayName("Invalid device ID format")
    class InvalidDeviceId {

        @ParameterizedTest(name = "device ID of length {0}")
        @ValueSource(strings = {
                "short",          // too short
                "12345678901234567",// 17 chars — not 16 or 32
                "this-is-exactly-31-chars-long!!" // 31 chars
        })
        void shouldRejectRequest_andNotProceedThroughChain(String deviceId) throws Exception {
            when(request.getHeader("Authorization")).thenReturn(deviceId);

            filter.doFilterInternal(request, response, filterChain);

            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
            verify(filterChain, never()).doFilter(any(), any());
            verify(authenticationEntryPoint).commence(eq(request), eq(response), any());
        }

        @Test
        void shouldSetValidationErrorAttribute_onRequest() throws Exception {
            when(request.getHeader("Authorization")).thenReturn("bad");

            filter.doFilterInternal(request, response, filterChain);

            verify(request).setAttribute(
                    eq(RestAuthenticationEntryPoint.AUTH_ERROR),
                    eq(ErrorType.VALIDATION_ERROR));
        }

        @Test
        void shouldSetErrorDetailsAttribute_withHelpfulMessage() throws Exception {
            when(request.getHeader("Authorization")).thenReturn("bad");

            filter.doFilterInternal(request, response, filterChain);

            verify(request).setAttribute(
                    eq(RestAuthenticationEntryPoint.AUTH_ERROR_DETAILS),
                    contains("16 or 32"));
        }
    }
}
