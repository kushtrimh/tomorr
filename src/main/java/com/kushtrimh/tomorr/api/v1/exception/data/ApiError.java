package com.kushtrimh.tomorr.api.v1.exception.data;

/**
 * @author Kushtrim Hajrizi
 */
public class ApiError {
    private String parameter;
    private String message;

    private ApiError(String parameter, String message) {
        this.parameter = parameter;
        this.message = message;
    }

    public static ApiError fromMessage(String message) {
        return new ApiError(null, message);
    }

    public static ApiError fromParameterAndMessage(String parameter, String message) {
        return new ApiError(parameter, message);
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "parameter='" + parameter + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
