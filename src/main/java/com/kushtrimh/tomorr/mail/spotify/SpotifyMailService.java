package com.kushtrimh.tomorr.mail.spotify;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * @author Kushtrim Hajrizi
 */
public interface SpotifyMailService {

    void send(String from, String subject, String templateName, Map<String, Object> additionalData, String... to)
            throws MessagingException;
}
