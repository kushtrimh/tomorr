package com.kushtrimh.tomorr.mail.notification;

import com.kushtrimh.tomorr.album.Album;
import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.mail.MailException;
import com.kushtrimh.tomorr.mail.MailService;
import com.kushtrimh.tomorr.mail.notification.retry.NotificationRetryData;
import com.kushtrimh.tomorr.mail.notification.retry.NotificationRetryService;
import com.kushtrimh.tomorr.properties.MailProperties;
import com.kushtrimh.tomorr.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateEngineException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultNotificationMailService implements NotificationMailService {

    private final Logger logger = LoggerFactory.getLogger(DefaultNotificationMailService.class);

    private final MailService mailService;
    private final ITemplateEngine mailTemplateEngine;
    private final MailProperties mailProperties;
    private final NotificationRetryService notificationRetryService;

    public DefaultNotificationMailService(
            MailService mailService,
            ITemplateEngine mailTemplateEngine,
            MailProperties mailProperties,
            NotificationRetryService notificationRetryService) {
        this.mailService = mailService;
        this.mailTemplateEngine = mailTemplateEngine;
        this.mailProperties = mailProperties;
        this.notificationRetryService = notificationRetryService;
    }

    public void sendNewReleaseNotification(Album album, Artist artist, List<User> users) {
        Map<String, Object> contextData = Map.of(
                "artistName", artist.name(),
                "albumName", album.name(),
                "type", album.albumType().name().toLowerCase(),
                "albumCover", album.imageHref());
        String subject = "New release: " + album.name();
        send(mailProperties.getFrom(), subject, "new-release-notification", contextData,
                users.stream().map(User::address).toList());
    }

    public void send(String from, String subject, String templateName, Map<String, Object> contextData, List<String> to) {
        logger.info("Sending notification email to with subject {}, using template {} and data {}",
                subject, templateName, contextData);
        var context = new Context(Locale.ENGLISH);
        context.setVariable("subject", subject);
        context.setVariable("from", from);
        context.setVariables(contextData);

        for (String toAddress : to) {
            String content = "";
            try {
                content = mailTemplateEngine.process(templateName, context);
                mailService.send(from, subject, content, toAddress);
            } catch (MailException | TemplateEngineException e) {
                logger.error("Failed to send notification email {}", content, e);
                notificationRetryService.retryNotification(
                        new NotificationRetryData(from, subject, templateName, contextData, List.of(toAddress), 2, TimeUnit.MINUTES));
            }
        }
    }
}
