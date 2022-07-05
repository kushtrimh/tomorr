package com.kushtrimh.tomorr.mail.notification;

import com.kushtrimh.tomorr.mail.MailException;
import com.kushtrimh.tomorr.mail.MailService;
import com.kushtrimh.tomorr.mail.notification.retry.NotificationRetryService;
import com.kushtrimh.tomorr.properties.MailProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith(MockitoExtension.class)
class DefaultNotificationMailServiceTest {

    @Mock
    private MailService mailService;
    @Mock
    private ITemplateEngine templateEngine;
    @Mock
    private MailProperties mailProperties;
    @Mock
    private NotificationRetryService notificationRetryService;

    private DefaultNotificationMailService spotifyMailService;

    @BeforeEach
    public void init() {
        spotifyMailService = new DefaultNotificationMailService(mailService, templateEngine, mailProperties, notificationRetryService);
    }

    @Test
    public void send_WhenDataIsValid_ShouldSendEmailSuccessfully() throws MailException {
        var from = "noreply@tomorrlocal.com";
        var subject = "some subject";
        var templateName = "test-template";
        var content = "<html><body><p th:text=\"${artistName}\"></p><p>content</p><p th:text=\"${albumName}\"></p></body></html>";
        var to1 = "to1@tomorrlocal.com";
        var to2 = "to2@tomorrlocal.com";
        Map<String, Object> additionalData = Map.of(
                "artistName", "some-artist-name",
                "albumName", "some-album-name"
        );

        var fromCaptor = ArgumentCaptor.forClass(String.class);
        var subjectCaptor = ArgumentCaptor.forClass(String.class);
        var contentCaptor = ArgumentCaptor.forClass(String.class);
        var toCaptor = ArgumentCaptor.forClass(String.class);

        when(templateEngine.process(eq(templateName), any(Context.class)))
                .thenReturn(content);

        spotifyMailService.send(from, subject, templateName, additionalData, to1, to2);

        verify(mailService, times(1)).send(
                fromCaptor.capture(),
                subjectCaptor.capture(),
                contentCaptor.capture(),
                toCaptor.capture());

        List<String> capturedTo = toCaptor.getAllValues();
        assertAll(
                () -> assertEquals(from, fromCaptor.getValue()),
                () -> assertEquals(subject, subjectCaptor.getValue()),
                () -> assertEquals(content, contentCaptor.getValue()),
                () -> assertEquals(to1, capturedTo.get(0)),
                () -> assertEquals(to2, capturedTo.get(1))
        );
    }
}