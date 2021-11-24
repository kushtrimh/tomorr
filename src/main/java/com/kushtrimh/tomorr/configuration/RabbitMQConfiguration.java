package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.properties.RabbitMQProperties;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
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

import java.util.Map;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
public class RabbitMQConfiguration {

    @Bean
    public CachingConnectionFactory connectionFactory(RabbitMQProperties properties) {
        return new CachingConnectionFactory(properties.getHost(), properties.getPort());
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
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
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
