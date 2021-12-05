package com.kushtrimh.tomorr.mail.spotify;

import com.kushtrimh.tomorr.mail.MailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.Locale;
import java.util.Map;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultSpotifyMailService implements SpotifyMailService {

    private final MailService mailService;
    private final TemplateEngine mailTemplateEngine;

    public DefaultSpotifyMailService(MailService mailService, @Qualifier("mailTemplateEngine") TemplateEngine mailTemplateEngine) {
        this.mailService = mailService;
        this.mailTemplateEngine = mailTemplateEngine;
    }

    public void send(String from, String subject, String templateName, Map<String, Object> additionalData, String... to)
            throws MessagingException {
        Context context = new Context(Locale.ENGLISH);
        context.setVariable("subject", subject);
        context.setVariable("from", from);
        context.setVariables(additionalData);

        String content = mailTemplateEngine.process(templateName, context);
        mailService.send(from, subject, content, to);
    }
}
