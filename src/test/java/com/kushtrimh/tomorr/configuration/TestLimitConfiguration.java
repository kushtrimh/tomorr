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
        limitProperties.setArtistSearch(100);
        limitProperties.setSpotify(450);
        return limitProperties;
    }
}
