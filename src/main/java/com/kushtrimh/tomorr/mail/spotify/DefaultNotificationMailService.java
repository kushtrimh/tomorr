package com.kushtrimh.tomorr.mail.spotify;

import com.kushtrimh.tomorr.album.Album;
import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.mail.MailException;
import com.kushtrimh.tomorr.mail.MailService;
import com.kushtrimh.tomorr.properties.MailProperties;
import com.kushtrimh.tomorr.user.User;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultNotificationMailService implements NotificationMailService {

    private final MailService mailService;
    private final ITemplateEngine mailTemplateEngine;
    private final MailProperties mailProperties;

    public DefaultNotificationMailService(
            MailService mailService,
            ITemplateEngine mailTemplateEngine,
            MailProperties mailProperties) {
        this.mailService = mailService;
        this.mailTemplateEngine = mailTemplateEngine;
        this.mailProperties = mailProperties;
    }

    public void sendNewReleaseNotification(Album album, Artist artist, List<User> users) throws MailException {
        Map<String, Object> contextData = Map.of(
                "artistName", artist.name(),
                "albumName", album.name(),
                "type", album.albumType().name().toLowerCase());
        String subject = "New release: " + album.name();
        send(mailProperties.getFrom(), subject, "new-release-notification", contextData,
                users.stream().map(User::address).toArray(String[]::new));
    }

    public void send(String from, String subject, String templateName, Map<String, Object> contextData, String... to)
            throws MailException {
        var context = new Context(Locale.ENGLISH);
        context.setVariable("subject", subject);
        context.setVariable("from", from);
        context.setVariables(contextData);

        String content = mailTemplateEngine.process(templateName, context);
        mailService.send(from, subject, content, to);
    }
}
