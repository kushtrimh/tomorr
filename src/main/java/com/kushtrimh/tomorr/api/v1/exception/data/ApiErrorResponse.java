package com.kushtrimh.tomorr.api.v1.exception.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiErrorResponse {
    private String message;
    private String violator;
    private List<ApiError> errors;
    private Instant timestamp = Instant.now();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getViolator() {
        return violator;
    }

    public void setViolator(String violator) {
        this.violator = violator;
    }

    public List<ApiError> getErrors() {
        return errors;
    }

    public void setErrors(List<ApiError> errors) {
        this.errors = errors;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ApiErrorResponse{" +
                "message='" + message + '\'' +
                ", violator='" + violator + '\'' +
                ", errors=" + errors +
                ", timestamp=" + timestamp +
                '}';
    }
}
