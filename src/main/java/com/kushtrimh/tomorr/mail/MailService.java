package com.kushtrimh.tomorr.mail;

import javax.mail.MessagingException;

/**
 * @author Kushtrim Hajrizi
 */
public interface MailService {

    void send(String from, String subject, String content, String... to) throws MessagingException;
}
