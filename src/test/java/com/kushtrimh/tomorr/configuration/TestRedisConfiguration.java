package com.kushtrimh.tomorr.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kushtrimh.tomorr.dal.extension.TestRedisExtension;
import com.kushtrimh.tomorr.properties.RedisProperties;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
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

    @Bean
    public RedisTemplate<String, Task<ArtistTaskData>> redisArtistSyncTemplate(RedisConnectionFactory redisConnectionFactory) {
        var typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.kushtrimh.tomorr")
                .build();
        var mapper = new ObjectMapper();
        mapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL);
        mapper.registerModule(new JavaTimeModule());
        var serializer = new GenericJackson2JsonRedisSerializer(mapper);
        RedisTemplate<String, Task<ArtistTaskData>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        return template;
    }

}
