package com.kushtrimh.tomorr.rabbitmq;

import com.kushtrimh.tomorr.configuration.TestRabbitMQConfiguration;
import com.kushtrimh.tomorr.extension.TestRabbitMQExtension;
import com.kushtrimh.tomorr.mail.notification.retry.NotificationRetryData;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
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
public class RabbitMQListenerIntegrationTest {

    @Autowired
    private TestRabbitTemplate testRabbitTemplate;

    @Test
    public void convertSendAndReceive_WhenSendingArtistUsingRabbitMQ_SendAndReceiveSuccessfully() {
        Task<ArtistTaskData> artistTask = new Task<>(ArtistTaskData.fromArtistId("artistId", TaskType.SYNC));
        Object response = testRabbitTemplate.convertSendAndReceive("artistSyncTest", artistTask);
        assertEquals(artistTask.getData().getArtistId(), response);
    }

    @Test
    public void convertSendAndReceive_WhenSendingNotificationRetryUsingRabbitMQ_SendAndReceiveSuccessfully() {
        var notificationRetryData = new NotificationRetryData(
                "no-reply@tomorrlocal.com", "subject-tomorr", "template-name", Map.of("param", "paramValue"), List.of("to1", "to2"), 1, TimeUnit.SECONDS);
        Task<NotificationRetryData> notificationRetryTask = new Task<>(notificationRetryData);
        Object response = testRabbitTemplate.convertSendAndReceive("notificationRetryTest", notificationRetryTask);
        assertEquals(notificationRetryData.getSubject(), response);
    }
}
