package com.kushtrimh.tomorr.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kushtrimh.tomorr.properties.SpotifyProperties;
import com.kushtrimh.tomorr.spotify.api.SpotifyApiClient;
import com.kushtrimh.tomorr.spotify.http.DefaultSpotifyHttpClient;
import com.kushtrimh.tomorr.spotify.util.SpotifyApiUriBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
public class SpotifyConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter mappingConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return new MappingJackson2HttpMessageConverter(mapper);
    }

    @Bean
    public SpotifyApiUriBuilder spotifyApiUriBuilder(SpotifyProperties spotifyProperties) {
        return new SpotifyApiUriBuilder(spotifyProperties.getApiUrl());
    }

    @Bean
    public SpotifyApiClient spotifyApiClient(SpotifyProperties spotifyProperties,
                                             SpotifyApiUriBuilder spotifyApiUriBuilder,
                                             MappingJackson2HttpMessageConverter mappingConverter) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .additionalMessageConverters(mappingConverter)
                .build();

        DefaultSpotifyHttpClient httpClient = new DefaultSpotifyHttpClient
                .Builder(spotifyApiUriBuilder, spotifyProperties.getAuthUrl())
                .userAgent(spotifyProperties.getUserAgent())
                .restTemplate(restTemplate)
                .build();
        return new SpotifyApiClient(httpClient, spotifyProperties);
    }
}