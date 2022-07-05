package com.kushtrimh.tomorr.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultMailService implements MailService {

    private final Logger logger = LoggerFactory.getLogger(DefaultMailService.class);

    private final JavaMailSender mailSender;

    public DefaultMailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    @Override
    public void send(String from, String subject, String content, String... to) throws MailException {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        var mailMessage = new MimeMessageHelper(mimeMessage);
        try {
            mailMessage.setFrom(from);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(content, true);
        } catch (MessagingException e) {
            logger.error("Could not send mail", e);
            throw new MailException(e);
        }
        mailSender.send(mimeMessage);
    }
}
