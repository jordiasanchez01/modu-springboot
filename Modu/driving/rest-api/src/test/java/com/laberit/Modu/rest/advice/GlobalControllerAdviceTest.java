package com.laberit.Modu.rest.advice;

import com.laberit.Modu.domain.exceptions.*;
import com.laberit.Modu.rest.generated.model.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalControllerAdviceTest {

    @InjectMocks
    private GlobalControllerAdvice advice;

    // ── helpers ──────────────────────────────────────────────────────────────

    private void assertErrorType(ErrorResponse response, String expectedType) {
        assertThat(response.getType()).isEqualTo(expectedType);
        assertThat(response.getMessage()).isNotNull();
        assertThat(response.getFields()).isNullOrEmpty();
    }

    // ── 409 CONFLICT handlers ────────────────────────────────────────────────

    @Nested
    @DisplayName("CONFLICT exceptions")
    class ConflictExceptions {

        @Test
        void shouldReturnConflict_forCartEmptyException() {
            ErrorResponse result = advice.handleCartEmpty(new CartEmptyException());
            assertErrorType(result, "CONFLICT");
            assertThat(result.getMessage()).isEqualTo(new CartEmptyException().getMessage());
        }

        @Test
        void shouldReturnConflict_forCategoryAlreadyExistsException() {
            ErrorResponse result = advice.handleCategoryAlreadyExists(new CategoryAlreadyExistsException());
            assertErrorType(result, "CONFLICT");
        }

        @Test
        void shouldReturnConflict_forOrderNotPaidException() {
            ErrorResponse result = advice.handleOrderNotPaid(new OrderNotPaidException());
            assertErrorType(result, "CONFLICT");
        }

        @Test
        void shouldReturnConflict_forProductVariantNotAvailableException() {
            ErrorResponse result = advice.handleProductVariableNotAvailable(new ProductVariantNotAvailableException());
            assertErrorType(result, "CONFLICT");
        }
    }

    // ── 404 NOT_FOUND handlers ───────────────────────────────────────────────

    @Nested
    @DisplayName("NOT_FOUND exceptions")
    class NotFoundExceptions {

        @Test
        void shouldReturnNotFound_forCartItemNotFoundException() {
            ErrorResponse result = advice.handleCartItemNotFound(new CartItemNotFoundException());
            assertErrorType(result, "NOT_FOUND");
        }

        @Test
        void shouldReturnNotFound_forCartNotFoundException() {
            ErrorResponse result = advice.handleCartNotFound(new CartNotFoundException());
            assertErrorType(result, "NOT_FOUND");
            assertThat(result.getMessage()).isEqualTo(new CartNotFoundException().getMessage());
        }

        @Test
        void shouldReturnNotFound_forCategoryNotFoundException() {
            ErrorResponse result = advice.handleCategoryNotFound(new CategoryNotFoundException());
            assertErrorType(result, "NOT_FOUND");
        }

        @Test
        void shouldReturnNotFound_forOrderNotFoundException() {
            ErrorResponse result = advice.handleOrderNotFound(new OrderNotFoundException());
            assertErrorType(result, "NOT_FOUND");
        }

        @Test
        void shouldReturnNotFound_forProductNotFoundException() {
            ErrorResponse result = advice.handleProductNotFound(new ProductNotFoundException());
            assertErrorType(result, "NOT_FOUND");
        }

        @Test
        void shouldReturnNotFound_forProductVariantNotFoundException() {
            ErrorResponse result = advice.handleProductVariableNotFound(new ProductVariantNotFoundException());
            assertErrorType(result, "NOT_FOUND");
        }
    }

    // ── 400 validation handlers ──────────────────────────────────────────────

    @Nested
    @DisplayName("Validation exceptions")
    class ValidationExceptions {

        @Test
        void shouldReturnValidationError_forMethodArgumentNotValidException() {
            MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError = new FieldError("addItemRequest", "variantId", "must not be null");
            when(ex.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

            ErrorResponse result = advice.handleValidation(ex);

            assertThat(result.getType()).isEqualTo("VALIDATION_ERROR");
            assertThat(result.getFields()).hasSize(1);
            assertThat(result.getFields().get(0).getField()).isEqualTo("variantId");
            assertThat(result.getFields().get(0).getMessage()).isEqualTo("must not be null");
        }

        @Test
        void shouldReturnValidationError_withAllFieldErrors_forMethodArgumentNotValidException() {
            MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            when(ex.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getFieldErrors()).thenReturn(List.of(
                    new FieldError("request", "variantId", "must not be null"),
                    new FieldError("request", "quantity", "must be greater than 0")
            ));

            ErrorResponse result = advice.handleValidation(ex);

            assertThat(result.getFields()).hasSize(2);
            assertThat(result.getFields()).extracting("field")
                    .containsExactly("variantId", "quantity");
        }

        @Test
        void shouldReturnValidationError_forConstraintViolationException() {
            ConstraintViolation<?> violation = mock(ConstraintViolation.class);
            Path path = mock(Path.class);
            // Property path format from Hibernate: "methodName.parameterName"
            when(path.toString()).thenReturn("checkoutCart.shippingCosts");
            when(violation.getPropertyPath()).thenReturn(path);
            when(violation.getMessage()).thenReturn("must be positive");

            ErrorResponse result = advice.handleConstraintViolation(
                    new ConstraintViolationException(Set.of(violation)));

            assertThat(result.getType()).isEqualTo("VALIDATION_ERROR");
            assertThat(result.getFields()).hasSize(1);
            assertThat(result.getFields().get(0).getField()).isEqualTo("shippingCosts");
            assertThat(result.getFields().get(0).getMessage()).isEqualTo("must be positive");
        }

        @Test
        void shouldReturnValidationError_forMethodArgumentTypeMismatchException() {
            MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
            when(ex.getName()).thenReturn("orderId");
            when(ex.getMessage()).thenReturn("Failed to convert value 'abc' to required type 'Long'");

            ErrorResponse result = advice.handleMethodArgumentTypeMismatch(ex);

            assertThat(result.getType()).isEqualTo("VALIDATION_ERROR");
            assertThat(result.getFields()).hasSize(1);
            assertThat(result.getFields().get(0).getField()).isEqualTo("orderId");
        }
    }

    // ── 500 fallback handler ─────────────────────────────────────────────────

    @Nested
    @DisplayName("Generic exception fallback")
    class GenericException {

        @Test
        void shouldReturnInternalError_forUnhandledException() {
            ErrorResponse result = advice.handleGeneric(new RuntimeException("unexpected"));

            assertThat(result.getType()).isEqualTo("INTERNAL_ERROR");
            assertThat(result.getMessage()).isEqualTo("unexpected");
            assertThat(result.getFields()).isNullOrEmpty();
        }
    }
}
