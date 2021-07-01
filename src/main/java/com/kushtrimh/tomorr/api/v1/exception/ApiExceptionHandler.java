package com.kushtrimh.tomorr.api.v1.exception;

import com.kushtrimh.tomorr.api.v1.exception.data.ApiError;
import com.kushtrimh.tomorr.exception.AlreadyExistsException;
import com.kushtrimh.tomorr.exception.DoesNotExistException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
@ControllerAdvice
public class ApiExceptionHandler {

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = DoesNotExistException.class)
    public ResponseEntity<ApiError> handleDoesNotExistException(DoesNotExistException e) {
        return createApiError("api.error.does-not-exist", e.getResource(), Collections.emptyList(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExistsException(AlreadyExistsException e) {
        return createApiError("api.error.already-exists", null, Collections.emptyList(), HttpStatus.CONFLICT);

    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> violations = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).toList();
        return createApiError("api.error.invalid-data", null, violations, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiError> handleException() {
        return createApiError("api.error.general-problem", null,
                Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * @param messageCode
     * @param violator
     * @param violations
     * @param status
     * @return
     */
    private ResponseEntity<ApiError> createApiError(String messageCode,
                                                    String violator,
                                                    List<String> violations,
                                                    HttpStatus status) {
        var apiError = new ApiError();
        apiError.setError(messageSource.getMessage(messageCode, null, LocaleContextHolder.getLocale()));
        apiError.setViolator(violator);
        apiError.setViolations(violations);
        return new ResponseEntity<>(apiError, status);
    }
}
