package com.laberit.Modu.rest.advice;

import com.laberit.Modu.rest.generated.model.ErrorResponse;
import com.laberit.Modu.rest.generated.model.ErrorResponseFieldsInner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public static final String AUTH_ERROR = "auth_error";
    public static final String AUTH_ERROR_DETAILS = "auth_error_details";

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        ErrorType errorType = (ErrorType) request.getAttribute(AUTH_ERROR);
        String details = (String) request.getAttribute(AUTH_ERROR_DETAILS);

        if (errorType == null) errorType = ErrorType.AUTHENTIFICATION_REQUIRED;

        ErrorResponse responseBody = new ErrorResponse()
                .type(errorType.name())
                .message(errorType.getMessage());

        if (details != null) {
            ErrorResponseFieldsInner field = new ErrorResponseFieldsInner();
            field.setField("Authorization");
            field.setMessage(details);
            responseBody.setFields(List.of(field));
        }

        int status = (errorType == ErrorType.VALIDATION_ERROR)
                ? HttpServletResponse.SC_BAD_REQUEST
                : HttpServletResponse.SC_UNAUTHORIZED;

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), responseBody);
    }
}
