package com.kushtrimh.tomorr.mail.notification.retry;

import com.kushtrimh.tomorr.mail.notification.NotificationMailService;
import com.kushtrimh.tomorr.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({MockitoExtension.class})
class RabbitMQNotificationRetryListenerTest {

    @Mock
    private NotificationMailService notificationMailService;

    private RabbitMQNotificationRetryListener service;

    @BeforeEach
    public void init() {
        service = new RabbitMQNotificationRetryListener(notificationMailService);
    }

    @Test
    public void handleNotificationRetry_WhenTaskIsReceived_SendNotification() {
        var from = "from";
        var subject = "subject";
        var templateName = "templateName";
        Map<String, Object> contextData = Map.of("param", "paramValue");
        var to = List.of("to1", "to2");
        var task = new Task<>(new NotificationRetryData(
                from, subject, templateName, contextData, to, 2, TimeUnit.SECONDS));
        service.handleNotificationRetry(task);

        verify(notificationMailService, times(1)).send(
                from, subject, templateName, contextData, to);
    }
}