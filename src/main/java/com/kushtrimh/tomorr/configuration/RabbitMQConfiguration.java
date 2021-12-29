package com.kushtrimh.tomorr.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kushtrimh.tomorr.properties.RabbitMQProperties;
import com.kushtrimh.tomorr.task.Task;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
public class RabbitMQConfiguration {

    @Bean
    public CachingConnectionFactory connectionFactory(RabbitMQProperties properties)
            throws NoSuchAlgorithmException, KeyManagementException {
        com.rabbitmq.client.ConnectionFactory cf = new com.rabbitmq.client.ConnectionFactory();
        cf.setHost(properties.getHost());
        cf.setPort(properties.getPort());
        cf.setUsername(properties.getUsername());
        cf.setPassword(properties.getPassword());
        if (properties.isUseSsl()) {
            cf.useSslProtocol();
        }
        return new CachingConnectionFactory(cf);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate artistTaskRabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setRoutingKey("artistSync");
        return rabbitTemplate;
    }

    @Bean
    public Queue queue() {
        return new Queue("artistSync", true);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(ClassMapper classMapper) {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);
        converter.setClassMapper(classMapper);
        return converter;
    }

    @Bean
    public ClassMapper classMapper() {
        DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
        Map<String, Class<?>> classMapping = Map.of(
                "task", Task.class
        );
        defaultClassMapper.setIdClassMapping(classMapping);
        return defaultClassMapper;
    }
}
