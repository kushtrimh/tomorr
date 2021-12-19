package com.kushtrimh.tomorr.api.v1.exception;

import com.kushtrimh.tomorr.api.v1.exception.data.ApiErrorResponse;
import com.kushtrimh.tomorr.exception.AlreadyExistsException;
import com.kushtrimh.tomorr.exception.DoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiExceptionHandlerTest {

    @Mock
    private MessageSource messageSource;

    private ApiExceptionHandler apiExceptionHandler;

    @BeforeEach
    public void init() {
        apiExceptionHandler = new ApiExceptionHandler(messageSource);
    }

    @Test
    public void handleDoesNotExistException_WhenExceptionHappens_ReturnApiErrorResponse() {
        var resource = "artist";
        var message = "Does not exist";
        when(messageSource.getMessage("api.error.does-not-exist", null, LocaleContextHolder.getLocale()))
                .thenReturn(message);
        ResponseEntity<ApiErrorResponse> apiErrorResponseEntity = apiExceptionHandler.handleDoesNotExistException(
                new DoesNotExistException(resource));
        assertEquals(HttpStatus.NOT_FOUND, apiErrorResponseEntity.getStatusCode());
        ApiErrorResponse apiError = apiErrorResponseEntity.getBody();
        assertAll(
                () -> assertEquals(message, apiError.getMessage()),
                () -> assertEquals(resource, apiError.getViolator()),
                () -> assertNull(apiError.getErrors())
        );
    }

    @Test
    public void handleAlreadyExistsException_WhenExceptionHappens_ReturnApiErrorResponse() {
        var message = "Already exists";
        when(messageSource.getMessage("api.error.already-exists", null, LocaleContextHolder.getLocale()))
                .thenReturn(message);
        ResponseEntity<ApiErrorResponse> apiErrorResponseEntity = apiExceptionHandler.handleAlreadyExistsException(
                new AlreadyExistsException());
        assertEquals(HttpStatus.CONFLICT, apiErrorResponseEntity.getStatusCode());
        ApiErrorResponse apiError = apiErrorResponseEntity.getBody();
        assertAll(
                () -> assertEquals(message, apiError.getMessage()),
                () -> assertNull(apiError.getViolator()),
                () -> assertNull(apiError.getErrors())
        );
    }

    @Test
    public void handleMethodArgumentNotValidException_WhenExceptionHappens_ReturnApiErrorResponse() {
        var mainMessage = "Validation error";
        var message = "Parameter is required";
        var bindingResult = mock(BindingResult.class);
        var fieldError = mock(FieldError.class);

        when(fieldError.getField()).thenReturn("artistId");
        when(fieldError.getCode()).thenReturn("NotBlank");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        when(messageSource.getMessage("api.error.validation-error", null, LocaleContextHolder.getLocale()))
                .thenReturn(mainMessage);
        when(messageSource.getMessage("validation.NotBlank", null, LocaleContextHolder.getLocale()))
                .thenReturn(message);
        MethodArgumentNotValidException e = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<ApiErrorResponse> apiErrorResponseEntity = apiExceptionHandler.handleMethodArgumentNotValidException(e);
        ApiErrorResponse apiErrorResponse = apiErrorResponseEntity.getBody();
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, apiErrorResponseEntity.getStatusCode()),
                () -> assertEquals(mainMessage, apiErrorResponse.getMessage()),
                () -> assertEquals(1, apiErrorResponse.getErrors().size()),
                () -> assertNull(apiErrorResponse.getViolator()),
                () -> assertEquals(message, apiErrorResponse.getErrors().get(0).getMessage()),
                () -> assertEquals("artistId", apiErrorResponse.getErrors().get(0).getParameter())
        );
    }

    @Test
    public void handleException_WhenExceptionHappens_ReturnApiErrorResponse() {
        var message = "Something wrong happened, please try again";
        when(messageSource.getMessage("api.error.general-problem", null, LocaleContextHolder.getLocale()))
                .thenReturn(message);
        ResponseEntity<ApiErrorResponse> apiErrorResponseEntity = apiExceptionHandler.handleException(new Exception());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiErrorResponseEntity.getStatusCode());
        ApiErrorResponse apiError = apiErrorResponseEntity.getBody();
        assertAll(
                () -> assertEquals(message, apiError.getMessage()),
                () -> assertNull(apiError.getViolator()),
                () -> assertNull(apiError.getErrors())
        );
    }

}