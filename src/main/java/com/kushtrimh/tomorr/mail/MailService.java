package com.kushtrimh.tomorr.mail;

/**
 * @author Kushtrim Hajrizi
 */
public interface MailService {

    void send(String from, String subject, String content, String... to) throws MailException;
}
