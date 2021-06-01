package com.kushtrimh.tomorr.api.v1.exception;

import com.kushtrimh.tomorr.api.v1.exception.data.ApiError;
import com.kushtrimh.tomorr.exception.AlreadyExistsException;
import com.kushtrimh.tomorr.exception.DoesNotExistException;
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

    @ExceptionHandler(value = DoesNotExistException.class)
    public ResponseEntity<ApiError> handleDoesNotExistException(DoesNotExistException e) {
        return createApiError("", null, Collections.emptyList(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExistsException(DoesNotExistException e) {
        return createApiError("", e.getResource(), Collections.emptyList(), HttpStatus.CONFLICT);

    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> violations = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).toList();
        return createApiError("", null, violations, HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * @param message
     * @param violator
     * @param violations
     * @param status
     * @return
     */
    private ResponseEntity<ApiError> createApiError(String message,
                                                    String violator,
                                                    List<String> violations,
                                                    HttpStatus status) {
        var apiError = new ApiError();
        apiError.setError(message);
        apiError.setViolator(violator);
        apiError.setViolations(violations);
        return new ResponseEntity<>(apiError, status);
    }
}
