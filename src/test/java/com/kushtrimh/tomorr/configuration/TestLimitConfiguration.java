package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.properties.LimitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
public class TestLimitConfiguration {

    @Bean
    public LimitProperties limitProperties() {
        LimitProperties limitProperties = new LimitProperties();
        limitProperties.setSpotifySearch(100);
        limitProperties.setSpotifySync(450);
        return limitProperties;
    }
}
