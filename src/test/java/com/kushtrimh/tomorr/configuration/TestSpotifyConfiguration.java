package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.properties.SpotifyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
@Import({SpotifyConfiguration.class})
public class TestSpotifyConfiguration {

    @Bean
    public SpotifyProperties spotifyProperties() {
        SpotifyProperties spotifyProperties = new SpotifyProperties();
        spotifyProperties.setClientId("client-id-value");
        spotifyProperties.setClientSecret("client-secret-value");
        spotifyProperties.setApiUrl("http://localhost");
        spotifyProperties.setAuthUrl("http://localhost/auth");
        spotifyProperties.setUserAgent("tomorr-test/0.1");
        return spotifyProperties;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
