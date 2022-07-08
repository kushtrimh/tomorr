package com.kushtrimh.tomorr.mail.notification.retry;

import com.kushtrimh.tomorr.configuration.TestRabbitMQConfiguration;
import com.kushtrimh.tomorr.extension.TestRabbitMQExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = {@Tag("rabbitmq"), @Tag("integration")})
@ContextConfiguration(classes = {TestRabbitMQConfiguration.class})
@ExtendWith({SpringExtension.class, TestRabbitMQExtension.class})
class RabbitMQNotificationRetryServiceIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private RabbitMQNotificationRetryService service;

    @BeforeEach
    public void init() {
        service = new RabbitMQNotificationRetryService(rabbitTemplate);
    }

    @Test
    public void retryNotification_WhenNotificationRetryDataIsValid_SendNotificationDataToQueue() {
        var notificationRetryData = new NotificationRetryData(
                "from", "subject", "templateName", Map.of("param", "paramValue"), List.of("to1", "to2"), 2, TimeUnit.SECONDS);
        assertDoesNotThrow(() -> service.retryNotification(notificationRetryData));
    }
}