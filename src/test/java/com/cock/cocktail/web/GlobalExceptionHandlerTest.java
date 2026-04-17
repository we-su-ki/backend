package com.cock.cocktail.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("MethodArgumentNotValidException 처리 - 첫 번째 필드 에러 메시지 반환")
    void shouldHandleValidationException() {
        var bindingResult = mock(BindingResult.class);
        var fieldError = new FieldError("request", "query", "query must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        var exception = new MethodArgumentNotValidException(null, bindingResult);

        var response = handler.handleValidationException(exception);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().error()).isEqualTo("Bad Request"),
                () -> assertThat(response.getBody().message()).isEqualTo("query must not be blank"),
                () -> assertThat(response.getBody().timestamp()).isNotNull()
        );
    }

    @Test
    @DisplayName("MethodArgumentNotValidException 처리 - 필드 에러 없으면 기본 메시지")
    void shouldReturnDefaultMessageWhenNoFieldErrors() {
        var bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        var exception = new MethodArgumentNotValidException(null, bindingResult);

        var response = handler.handleValidationException(exception);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().message()).isEqualTo("Validation failed")
        );
    }

    @Test
    @DisplayName("MissingServletRequestParameterException 처리 - 400 Bad Request")
    void shouldHandleMissingParameter() {
        var exception = new MissingServletRequestParameterException("query", "String");

        var response = handler.handleMissingParameter(exception);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().error()).isEqualTo("Bad Request"),
                () -> assertThat(response.getBody().message()).isEqualTo("Required parameter 'query' is missing"),
                () -> assertThat(response.getBody().timestamp()).isNotNull()
        );
    }

    @Test
    @DisplayName("IllegalArgumentException 처리 - 400 Bad Request")
    void shouldHandleIllegalArgumentException() {
        var exception = new IllegalArgumentException("query must not be blank");

        var response = handler.handleIllegalArgumentException(exception);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().error()).isEqualTo("Bad Request"),
                () -> assertThat(response.getBody().message()).isEqualTo("query must not be blank"),
                () -> assertThat(response.getBody().timestamp()).isNotNull()
        );
    }

    @Test
    @DisplayName("Exception 처리 - 500 Internal Server Error")
    void shouldHandleGenericException() {
        var exception = new RuntimeException("Unexpected error");

        var response = handler.handleGenericException(exception);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().error()).isEqualTo("Internal Server Error"),
                () -> assertThat(response.getBody().message()).isEqualTo("An unexpected error occurred"),
                () -> assertThat(response.getBody().timestamp()).isNotNull()
        );
    }
}
