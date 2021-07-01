package com.kushtrimh.tomorr.exception;

/**
 * @author Kushtrim Hajrizi
 */
public class DoesNotExistException extends RuntimeException {
    private final String resource;

    public DoesNotExistException(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
