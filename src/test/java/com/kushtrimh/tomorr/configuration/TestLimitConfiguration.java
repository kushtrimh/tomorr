package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.limit.DefaultRequestLimitService;
import com.kushtrimh.tomorr.limit.RequestLimitService;
import com.kushtrimh.tomorr.properties.LimitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
@Import({TestRedisConfiguration.class})
public class TestLimitConfiguration {

    @Bean
    public LimitProperties limitProperties() {
        LimitProperties limitProperties = new LimitProperties();
        limitProperties.setSpotifySearch(100);
        limitProperties.setSpotifySync(450);
        return limitProperties;
    }

    @Bean
    public RequestLimitService requestLimitService(
            LimitProperties limitProperties,  RedisTemplate<String, Integer> template) {
        return new DefaultRequestLimitService(limitProperties, template);
    }
}
