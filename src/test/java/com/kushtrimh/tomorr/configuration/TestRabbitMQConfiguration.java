package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.extension.TestRabbitMQExtension;
import com.kushtrimh.tomorr.mail.notification.retry.NotificationRetryData;
import com.kushtrimh.tomorr.properties.RabbitMQProperties;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author Kushtrim Hajrizi
 */
@TestConfiguration
@Import({RabbitMQConfiguration.class})
public class TestRabbitMQConfiguration {

    @Bean
    public RabbitMQProperties rabbitMQProperties() {
        RabbitMQProperties properties = new RabbitMQProperties();
        properties.setHost(TestRabbitMQExtension.getRabbitMQContainer().getHost());
        properties.setPort(TestRabbitMQExtension.getRabbitMQContainer().getFirstMappedPort());
        properties.setUsername("guest");
        properties.setPassword("guest");
        return properties;
    }

    @Bean
    public TestRabbitTemplate artistTaskTestRabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter rabbitJsonMessageConverter) {
        TestRabbitTemplate rabbitTemplate = new TestRabbitTemplate(connectionFactory);
        rabbitTemplate.setRoutingKey("artistSyncTest");
        rabbitTemplate.setMessageConverter(rabbitJsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue testQueue() {
        return new Queue("artistSyncTest", true);
    }

    @Bean
    public Queue retryNotificationQueue() {
        return new Queue("notificationRetryTest", true);
    }

    @RabbitListener(queues = "artistSyncTest")
    public String artistSyncListener(Task<ArtistTaskData> artistTask) {
        return artistTask.getData().getArtistId();
    }

    @RabbitListener(queues = "notificationRetryTest")
    public String retryNotificationListener(Task<NotificationRetryData> notificationRetryTask) {
        return notificationRetryTask.getData().getSubject();
    }
}
