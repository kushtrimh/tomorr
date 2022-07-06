package com.kushtrimh.tomorr.mail;

/**
 * @author Kushtrim Hajrizi
 */
public class MailException extends Exception {

    public MailException() {
    }

    public MailException(String message) {
        super(message);
    }

    public MailException(Throwable cause) {
        super(cause);
    }
}
