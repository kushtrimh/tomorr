package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.configuration.TestRedisConfiguration;
import com.kushtrimh.tomorr.extension.TestRedisExtension;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.SpotifyCacheKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = {@Tag("redis"), @Tag("integration")})
@ExtendWith({SpringExtension.class, MockitoExtension.class, TestRedisExtension.class})
@ContextConfiguration(classes = TestRedisConfiguration.class)
public class SpotifyAccessTokenInitializerIntegrationTest {

    @Mock
    private DefaultSpotifyApiClient spotifyApiClient;

    private final StringRedisTemplate stringRedisTemplate;
    private final ApplicationContext context;

    private SpotifyAccessTokenInitializer initializer;

    @Autowired
    public SpotifyAccessTokenInitializerIntegrationTest(ApplicationContext context, StringRedisTemplate stringRedisTemplate) {
        this.context = context;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @BeforeEach
    public void init() {
        initializer = new SpotifyAccessTokenInitializer(stringRedisTemplate, spotifyApiClient);
    }

    @Test
    public void onApplicationEvent_WhenAccessTokenIsNotPresentInRedis_RefreshToken()
            throws SpotifyApiException {
        stringRedisTemplate.delete(SpotifyCacheKeys.ACCESS_TOKEN_KEY);
        initializer.onApplicationEvent(new ContextRefreshedEvent(context));
        verify(spotifyApiClient, times(1)).refreshAccessToken();
    }

    @Test
    public void onApplicationEvent_WhenAccessTokenIsAlreadyPresentInRedis_DoNothing()
            throws SpotifyApiException {
        stringRedisTemplate.opsForValue().set(SpotifyCacheKeys.ACCESS_TOKEN_KEY, "token-token");
        initializer.onApplicationEvent(new ContextRefreshedEvent(context));
        verify(spotifyApiClient, times(0)).refreshAccessToken();
    }
}
