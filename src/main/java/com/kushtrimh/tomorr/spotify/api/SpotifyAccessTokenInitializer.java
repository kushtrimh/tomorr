package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.SpotifyCacheKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class SpotifyAccessTokenInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(SpotifyAccessTokenInitializer.class);

    private final StringRedisTemplate stringRedisTemplate;
    private final SpotifyApiClient spotifyApiClient;

    public SpotifyAccessTokenInitializer(StringRedisTemplate stringRedisTemplate, SpotifyApiClient spotifyApiClient) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.spotifyApiClient = spotifyApiClient;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String accessToken = stringRedisTemplate.opsForValue().get(SpotifyCacheKeys.ACCESS_TOKEN_KEY);
        if (accessToken != null && !accessToken.isBlank()) {
            logger.info("Token already present, initialization skipped");
            return;
        }
        logger.info("Initializing token");
        try {
            spotifyApiClient.refreshAccessToken();
        } catch (SpotifyApiException e) {
            logger.error("Could not refresh token at initialization", e);
        }
    }
}
