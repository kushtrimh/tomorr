package com.kushtrimh.tomorr.api.v1.exception;

import com.kushtrimh.tomorr.api.v1.exception.data.ApiError;
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

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.Set;

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
        ResponseEntity<ApiError> apiErrorResponseEntity = apiExceptionHandler.handleDoesNotExistException(
                new DoesNotExistException(resource));
        assertEquals(HttpStatus.NOT_FOUND, apiErrorResponseEntity.getStatusCode());
        ApiError apiError = apiErrorResponseEntity.getBody();
        assertEquals(message, apiError.getError());
        assertEquals(resource, apiError.getViolator());
        assertTrue(apiError.getViolations().isEmpty());
    }

    @Test
    public void handleAlreadyExistsException_WhenExceptionHappens_ReturnApiErrorResponse() {
        var message = "Already exists";
        when(messageSource.getMessage("api.error.already-exists", null, LocaleContextHolder.getLocale()))
                .thenReturn(message);
        ResponseEntity<ApiError> apiErrorResponseEntity = apiExceptionHandler.handleAlreadyExistsException(
                new AlreadyExistsException());
        assertEquals(HttpStatus.CONFLICT, apiErrorResponseEntity.getStatusCode());
        ApiError apiError = apiErrorResponseEntity.getBody();
        assertEquals(message, apiError.getError());
        assertNull(apiError.getViolator());
        assertTrue(apiError.getViolations().isEmpty());
    }

    @Test
    public void handleConstraintViolationException_WhenExceptionHappens_ReturnApiErrorResponse() {
        var message = "Invalid data";
        var invalidArtistId = "Invalid artist id";
        var invalidUserId = "Invalid user id";
        when(messageSource.getMessage("api.error.invalid-data", null, LocaleContextHolder.getLocale()))
                .thenReturn(message);
        Set<ConstraintViolation<?>> violations = Set.of(
                new ConstraintViolationStub(invalidArtistId),
                new ConstraintViolationStub(invalidUserId)
        );
        var ex = new ConstraintViolationException(message, violations);
        ResponseEntity<ApiError> apiErrorResponseEntity = apiExceptionHandler.handleConstraintViolationException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, apiErrorResponseEntity.getStatusCode());
        ApiError apiError = apiErrorResponseEntity.getBody();
        assertEquals(message, apiError.getError());
        assertNull(apiError.getViolator());
        assertEquals(2, apiError.getViolations().size());
        assertTrue(apiError.getViolations().contains(invalidArtistId));
        assertTrue(apiError.getViolations().contains(invalidUserId));
    }

    @Test
    public void handleException_WhenExceptionHappens_ReturnApiErrorResponse() {
        var message = "Something wrong happened, please try again";
        when(messageSource.getMessage("api.error.general-problem", null, LocaleContextHolder.getLocale()))
                .thenReturn(message);
        ResponseEntity<ApiError> apiErrorResponseEntity = apiExceptionHandler.handleException();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiErrorResponseEntity.getStatusCode());
        ApiError apiError = apiErrorResponseEntity.getBody();
        assertEquals(message, apiError.getError());
        assertNull(apiError.getViolator());
        assertTrue(apiError.getViolations().isEmpty());
    }

    private record ConstraintViolationStub(String message) implements ConstraintViolation<Object> {

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Override
        public Object getRootBean() {
            return null;
        }

        @Override
        public Class<Object> getRootBeanClass() {
            return null;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[0];
        }

        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Path getPropertyPath() {
            return null;
        }

        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public <U> U unwrap(Class<U> type) {
            return null;
        }
    }

}