package com.kushtrimh.tomorr.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kushtrimh.tomorr.properties.SpotifyProperties;
import com.kushtrimh.tomorr.spotify.util.SpotifyApiUriBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
public class TestSpotifyConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter mappingConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return new MappingJackson2HttpMessageConverter(mapper);
    }

    @Bean
    public RestTemplate restTemplate(MappingJackson2HttpMessageConverter mappingConverter) {
        return new RestTemplateBuilder()
                .additionalMessageConverters(mappingConverter)
                .additionalMessageConverters(new FormHttpMessageConverter())
                .build();
    }

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
    public SpotifyApiUriBuilder spotifyApiUriBuilder(SpotifyProperties spotifyProperties) {
        return new SpotifyApiUriBuilder(spotifyProperties.getApiUrl());
    }
}
