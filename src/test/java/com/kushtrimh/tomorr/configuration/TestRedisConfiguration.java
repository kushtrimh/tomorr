package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.extension.TestRedisExtension;
import com.kushtrimh.tomorr.properties.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author Kushtrim Hajrizi
 */
@TestConfiguration
@Import({RedisConfiguration.class})
public class TestRedisConfiguration {

    @Bean
    public RedisProperties redisProperties() {
        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setHost(TestRedisExtension.getRedisContainer().getHost());
        redisProperties.setPort(TestRedisExtension.getRedisContainer().getFirstMappedPort());
        return redisProperties;
    }
}
