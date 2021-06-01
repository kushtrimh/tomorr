package com.kushtrimh.tomorr.api.v1.exception.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiError {
    private String error;
    private String violator;
    private List<String> violations;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getViolator() {
        return violator;
    }

    public void setViolator(String violator) {
        this.violator = violator;
    }

    public List<String> getViolations() {
        return violations;
    }

    public void setViolations(List<String> violations) {
        this.violations = violations;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "error='" + error + '\'' +
                ", violator='" + violator + '\'' +
                ", violations=" + violations +
                '}';
    }
}
