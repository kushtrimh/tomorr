package com.kushtrimh.tomorr.mail.notification.retry;

import com.kushtrimh.tomorr.configuration.RabbitMQConfiguration;
import com.kushtrimh.tomorr.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({MockitoExtension.class})
class RabbitMQNotificationRetryServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private RabbitMQNotificationRetryService service;

    @BeforeEach
    public void init() {
        service = new RabbitMQNotificationRetryService(rabbitTemplate);
    }

    @Test
    public void retryNotification_WhenNotificationRetryDataIsNull_DoNothing() {
        service.retryNotification(null);
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(Task.class));
    }

    @Test
    public void retryNotification_WhenNotificationIsSentWithDelay_SentNotificationSuccessfully() {
        var notificationRetryData = new NotificationRetryData(
                "from", "subject", "templateName", new HashMap<>(), new ArrayList<>(), 3, TimeUnit.SECONDS);

        service.retryNotification(notificationRetryData).join();
        verify(rabbitTemplate, times(1)).convertAndSend(
                RabbitMQConfiguration.NOTIFICATION_RETRY_QUEUE, new Task<>(notificationRetryData));
    }
}