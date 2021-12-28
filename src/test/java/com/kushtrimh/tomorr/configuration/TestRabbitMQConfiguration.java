package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.extension.TestRabbitMQExtension;
import com.kushtrimh.tomorr.properties.RabbitMQProperties;
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
}
