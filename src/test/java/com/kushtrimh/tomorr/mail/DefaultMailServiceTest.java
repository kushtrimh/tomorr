package com.kushtrimh.tomorr.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith(MockitoExtension.class)
class DefaultMailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    private DefaultMailService mailService;

    @BeforeEach
    public void init() {
        mailService = new DefaultMailService(javaMailSender);
    }

    @Test
    public void send_WhenFromIsEmpty_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> mailService.send(null, "subject", "content", "to"));
    }

    @Test
    public void send_WhenToIsEmpty_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> mailService.send("from", "subject", "content", null));
    }

    @Test
    public void send_WhenDataIsValid_SendSuccessfully() throws MessagingException {
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        var from = "noreploy@tomorrlocal.com";
        var subject = "Tomorr subject";
        var content = "<html><body><p>Some email</p></body></html>";
        var to1 = "to1@tomorrlocal.com";
        var to2 = "to2@tomorrlocal.com";

        Address[] recipients = new InternetAddress[]{new InternetAddress(to1), new InternetAddress(to2)};

        mailService.send(from, subject, content, to1, to2);
        verify(javaMailSender, times(1)).send(captor.capture());
        MimeMessage message = captor.getValue();
        assertAll(
                () -> assertEquals(new InternetAddress[]{new InternetAddress(from)}[0], message.getFrom()[0]),
                () -> assertEquals(subject, message.getSubject()),
                () -> assertEquals(content, message.getContent()),
                () -> assertEquals(recipients[0], message.getAllRecipients()[0]),
                () -> assertEquals(recipients[1], message.getAllRecipients()[1])
        );
    }
}