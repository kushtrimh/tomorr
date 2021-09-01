package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.dal.extension.TestRedisExtension;
import com.kushtrimh.tomorr.properties.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
public class TestRedisConfiguration {

    @Bean
    public RedisProperties redisProperties() {
        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setHost(TestRedisExtension.getRedisContainer().getHost());
        redisProperties.setPort(TestRedisExtension.getRedisContainer().getFirstMappedPort());
        return redisProperties;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(
                redisProperties.getHost(),
                redisProperties.getPort());
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public RedisTemplate<String, Integer> integerRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        return template;
    }
}
