package com.kushtrimh.tomorr.mail.notification.retry;

import com.kushtrimh.tomorr.configuration.RabbitMQConfiguration;
import com.kushtrimh.tomorr.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class RabbitMQNotificationRetryService implements NotificationRetryService {

    private final Logger logger = LoggerFactory.getLogger(RabbitMQNotificationRetryService.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQNotificationRetryService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public CompletableFuture<Void> retryNotification(NotificationRetryData notificationRetryData) {
        if (notificationRetryData == null) {
            return CompletableFuture.completedFuture(null);
        }
        logger.info("Retrying notification: {}", notificationRetryData);
        var task = new Task<>(notificationRetryData);
        return CompletableFuture.runAsync(() -> rabbitTemplate.convertAndSend(RabbitMQConfiguration.NOTIFICATION_RETRY_QUEUE, task),
                CompletableFuture.delayedExecutor(notificationRetryData.getDelay(), notificationRetryData.getDelayTimeUnit()));
    }
}
