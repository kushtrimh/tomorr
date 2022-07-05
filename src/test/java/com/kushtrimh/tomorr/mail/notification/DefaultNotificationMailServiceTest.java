package com.kushtrimh.tomorr.mail.notification;

import com.kushtrimh.tomorr.album.Album;
import com.kushtrimh.tomorr.album.AlbumType;
import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.mail.MailException;
import com.kushtrimh.tomorr.mail.MailService;
import com.kushtrimh.tomorr.mail.notification.retry.NotificationRetryData;
import com.kushtrimh.tomorr.mail.notification.retry.NotificationRetryService;
import com.kushtrimh.tomorr.properties.MailProperties;
import com.kushtrimh.tomorr.user.User;
import com.kushtrimh.tomorr.user.UserType;
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

    private DefaultNotificationMailService notificationMailService;

    @BeforeEach
    public void init() {
        notificationMailService = new DefaultNotificationMailService(
                mailService, templateEngine, mailProperties, notificationRetryService);
    }

    @Test
    public void sendNewReleaseNotification_WhenDataIsValid_SendNotificationSuccessfully() throws MailException {
        var album = new Album("album1", "album1-name", AlbumType.ALBUM, 10, "2020-01-01", "image-url");
        var artist = new Artist("artist1", "artist1-name", "artist1-image-url", 100);
        var users = List.of(new User("user1", UserType.EMAIL), new User("user2", UserType.EMAIL));
        var from = "noreply@tomorrlocal.com";

        var content = "content";

        var contextCaptor = ArgumentCaptor.forClass(Context.class);

        when(templateEngine.process(eq("new-release-notification"), contextCaptor.capture())).thenReturn(content);
        when(mailProperties.getFrom()).thenReturn(from);

        notificationMailService.sendNewReleaseNotification(album, artist, users);

        verify(mailService, times(1)).send(
                from, "New release: " + album.name(), content, users.get(0).address(), users.get(1).address());

        var context = contextCaptor.getValue();
        assertAll(
                () -> assertEquals("artist1-name", context.getVariable("artistName")),
                () -> assertEquals("album1-name", context.getVariable("albumName")),
                () -> assertEquals("album", context.getVariable("type"))
        );
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

        notificationMailService.send(from, subject, templateName, additionalData, List.of(to1, to2));

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

    @Test
    public void send_WhenEmailCouldNotBeSent_AddNotificationDataToRetryQueue() throws MailException {
        var from = "noreply@tomorrlocal.com";
        var subject = "some subject";
        var content = "body";
        var templateName = "test-template";
        var to1 = "to1@tomorrlocal.com";
        var to2 = "to2@tomorrlocal.com";
        Map<String, Object> additionalData = Map.of(
                "artistName", "some-artist-name",
                "albumName", "some-album-name"
        );

        var notificationRetryDataCaptor = ArgumentCaptor.forClass(NotificationRetryData.class);

        when(templateEngine.process(eq(templateName), any(Context.class)))
                .thenReturn(content);
        doThrow(new MailException()).when(mailService).send(from, subject, content, to1, to2);

        notificationMailService.send(from, subject, templateName, additionalData, List.of(to1, to2));

        verify(notificationRetryService, times(1))
                .retryNotification(notificationRetryDataCaptor.capture());

        var notificationData = notificationRetryDataCaptor.getValue();
        assertAll(
                () -> assertEquals(from, notificationData.getFrom()),
                () -> assertEquals(subject, notificationData.getSubject()),
                () -> assertEquals(templateName, notificationData.getTemplateName()),
                () -> assertEquals(to1, notificationData.getTo().get(0)),
                () -> assertEquals(to2, notificationData.getTo().get(1)),
                () -> assertEquals(additionalData, notificationData.getContextData())
        );
    }
}