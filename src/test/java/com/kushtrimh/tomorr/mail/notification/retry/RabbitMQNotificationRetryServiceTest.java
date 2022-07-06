package com.kushtrimh.tomorr.mail.notification.retry;

import com.kushtrimh.tomorr.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.HashMap;

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
                "from", "subject", "templateName", new HashMap<>(), new ArrayList<>());

        var message = mock(Message.class);
        lenient().when(message.getMessageProperties()).thenReturn(mock(MessageProperties.class));

        doAnswer(invocation -> {
            MessagePostProcessor messagePostProcessor = invocation.getArgument(2, MessagePostProcessor.class);
            messagePostProcessor.postProcessMessage(message);
            return null;
        }).when(rabbitTemplate).convertAndSend(
                anyString(), any(Task.class), any(MessagePostProcessor.class));

        service.retryNotification(notificationRetryData);
        verify(message.getMessageProperties(), times(1)).setDelay(300000);
    }
}