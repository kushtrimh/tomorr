package com.kushtrimh.tomorr.mail.notification.retry;

import com.kushtrimh.tomorr.configuration.RabbitMQConfiguration;
import com.kushtrimh.tomorr.mail.notification.NotificationMailService;
import com.kushtrimh.tomorr.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class RabbitMQNotificationRetryListener implements NotificationRetryListener {
    private final Logger logger = LoggerFactory.getLogger(RabbitMQNotificationRetryListener.class);

    private final NotificationMailService notificationMailService;

    public RabbitMQNotificationRetryListener(NotificationMailService notificationMailService) {
        this.notificationMailService = notificationMailService;
    }

    @Override
    @RabbitListener(queues = RabbitMQConfiguration.NOTIFICATION_RETRY_QUEUE)
    public void handleNotificationRetry(Task<NotificationRetryData> task) {
        logger.info("Received notification retry task: {}", task);
        var notificationRetryData = task.getData();
        notificationMailService.send(
                notificationRetryData.getFrom(),
                notificationRetryData.getSubject(),
                notificationRetryData.getTemplateName(),
                notificationRetryData.getContextData(),
                notificationRetryData.getTo());
    }
}
