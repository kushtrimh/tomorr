package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.dal.extension.TestRedisExtension;
import com.kushtrimh.tomorr.properties.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
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
