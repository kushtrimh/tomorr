package com.kushtrimh.tomorr.mail.notification.retry;

import com.kushtrimh.tomorr.configuration.RabbitMQConfiguration;
import com.kushtrimh.tomorr.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultNotificationRetryService implements NotificationRetryService {

    private final Logger logger = LoggerFactory.getLogger(DefaultNotificationRetryService.class);

    private final RabbitTemplate rabbitTemplate;

    public DefaultNotificationRetryService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void retryNotification(NotificationRetryData notificationRetryData) {
        if (notificationRetryData == null) {
            return;
        }
        logger.info("Retrying notification: {}", notificationRetryData);
        var task = new Task<>(notificationRetryData);
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.NOTIFICATION_RETRY_QUEUE, task);
    }
}
