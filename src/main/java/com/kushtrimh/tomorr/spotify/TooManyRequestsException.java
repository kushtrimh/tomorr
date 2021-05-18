package com.kushtrimh.tomorr.spotify;

/**
 * @author Kushtrim Hajrizi
 */
public class TooManyRequestsException extends Exception {

    /**
     * The value in seconds after which we can retry to make requests again
     */
    private int retryAfter;

    public TooManyRequestsException(int retryAfter) {
        this.retryAfter = retryAfter;
    }

    public TooManyRequestsException(String message) {
        super(message);
    }

    public int getRetryAfter() {
        return retryAfter;
    }
}
