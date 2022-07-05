package com.kushtrimh.tomorr.mail.spotify;

import com.kushtrimh.tomorr.album.Album;
import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.mail.MailException;
import com.kushtrimh.tomorr.user.User;

import java.util.List;
import java.util.Map;

/**
 * @author Kushtrim Hajrizi
 */
public interface NotificationMailService {

    void sendNewReleaseNotification(Album album, Artist artist, List<User> users) throws MailException;

    void send(String from, String subject, String templateName, Map<String, Object> contextData, String... to)
            throws MailException;
}
