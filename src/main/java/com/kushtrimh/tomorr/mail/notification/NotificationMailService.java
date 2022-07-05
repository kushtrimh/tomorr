package com.kushtrimh.tomorr.mail.notification;

import com.kushtrimh.tomorr.album.Album;
import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.user.User;

import java.util.List;
import java.util.Map;

/**
 * @author Kushtrim Hajrizi
 */
public interface NotificationMailService {

    void sendNewReleaseNotification(Album album, Artist artist, List<User> users);

    void send(String from, String subject, String templateName, Map<String, Object> contextData, String... to);
}
