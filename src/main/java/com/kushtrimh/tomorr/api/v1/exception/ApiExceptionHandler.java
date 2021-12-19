package com.kushtrimh.tomorr.api.v1.exception;

import com.kushtrimh.tomorr.api.v1.exception.data.ApiError;
import com.kushtrimh.tomorr.api.v1.exception.data.ApiErrorResponse;
import com.kushtrimh.tomorr.exception.AlreadyExistsException;
import com.kushtrimh.tomorr.exception.DoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
@ControllerAdvice
public class ApiExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = DoesNotExistException.class)
    public ResponseEntity<ApiErrorResponse> handleDoesNotExistException(DoesNotExistException e) {
        return createApiError(HttpStatus.NOT_FOUND, "api.error.does-not-exist", e.getResource(), null);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleAlreadyExistsException(AlreadyExistsException e) {
        return createApiError(HttpStatus.CONFLICT, "api.error.already-exists", null, null);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ApiError> errors = e.getBindingResult().getFieldErrors()
                .stream().map(err -> ApiError.fromParameterAndMessage(
                        err.getField(), "validation." + err.getCode())).toList();
        return createApiError(HttpStatus.BAD_REQUEST, "api.error.validation-error", null, errors);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        logger.error("General error during API request", e);
        return createApiError(HttpStatus.INTERNAL_SERVER_ERROR, "api.error.general-problem", null, null);
    }

    /**
     * @param status
     * @param message
     * @param violator
     * @param errors
     * @return
     */
    private ResponseEntity<ApiErrorResponse> createApiError(
            HttpStatus status,
            String message,
            String violator,
            List<ApiError> errors) {
        Locale locale = LocaleContextHolder.getLocale();
        Optional.ofNullable(errors).ifPresent(errs -> errs.forEach(error -> error.setMessage(
                messageSource.getMessage(error.getMessage(), null, locale))));
        var apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setMessage(messageSource.getMessage(message, null, locale));
        apiErrorResponse.setViolator(violator);
        apiErrorResponse.setErrors(errors);
        return new ResponseEntity<>(apiErrorResponse, status);
    }
}
