package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.extension.TestRabbitMQExtension;
import com.kushtrimh.tomorr.properties.RabbitMQProperties;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
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
    public TestRabbitTemplate artistTaskRabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter rabbitJsonMessageConverter) {
        TestRabbitTemplate rabbitTemplate = new TestRabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(rabbitJsonMessageConverter);
        return rabbitTemplate;
    }

    @RabbitListener(queues = "artistSync")
    public String artistSyncListener(Task<ArtistTaskData> artistTask) {
        return artistTask.getData().getArtistId();
    }
}
