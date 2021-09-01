package com.kushtrimh.tomorr.spotify.limit;

import com.kushtrimh.tomorr.configuration.TestRedisConfiguration;
import com.kushtrimh.tomorr.configuration.TestSpotifyConfiguration;
import com.kushtrimh.tomorr.dal.extension.TestRedisExtension;
import com.kushtrimh.tomorr.properties.SpotifyProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = { @Tag("redis"), @Tag("integration") })
@ContextConfiguration(classes = {TestSpotifyConfiguration.class, TestRedisConfiguration.class})
@ExtendWith({SpringExtension.class, MockitoExtension.class, TestRedisExtension.class})
public class DefaultRequestLimitServiceIntegrationTest {

    private final SpotifyProperties spotifyProperties;
    private final RedisTemplate<String, Integer> integerRedisTemplate;
    private final ValueOperations<String, Integer> integerValueOperations;

    private DefaultRequestLimitService requestLimitService;

    @Autowired
    public DefaultRequestLimitServiceIntegrationTest(SpotifyProperties spotifyProperties, RedisTemplate<String, Integer> integerRedisTemplate) {
        this.spotifyProperties = spotifyProperties;
        this.integerRedisTemplate = integerRedisTemplate;
        this.integerValueOperations = integerRedisTemplate.opsForValue();
    }

    @BeforeEach
    public void init() {
        requestLimitService = new DefaultRequestLimitService(spotifyProperties, integerRedisTemplate);
        integerValueOperations.set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 0);
        integerValueOperations.set(LimitType.ARTIST_SEARCH.getCacheKey(), 0);
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsBiggerThanLimit_ReturnFalse() {
        integerValueOperations.set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 475);
        assertFalse(requestLimitService.canSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsEqualsToLimit_ReturnFalse() {
        integerValueOperations.set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 450);
        assertFalse(requestLimitService.canSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsSmallerThanLimit_ReturnTrue() {
        integerValueOperations.set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 25);
        assertTrue(requestLimitService.canSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsBiggerThanLimit_ReturnTrue() {
        integerValueOperations.set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 475);
        assertTrue(requestLimitService.cantSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsEqualsToLimit_ReturnTrue() {
        integerValueOperations.set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 450);
        assertTrue(requestLimitService.cantSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsSmallerThanLimit_ReturnFalse() {
        integerValueOperations.set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 25);
        assertFalse(requestLimitService.cantSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void getRemainingRequestLimit_WhenCalled_ReturnRemainingRequestLimit() {
        integerValueOperations.set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 340);
        assertEquals(110, requestLimitService.getRemainingRequestLimit(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void getSentRequestsCounter_WhenCalled_ReturnRequestCounter() {
        integerValueOperations.set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 154);
        assertEquals(154, requestLimitService.getSentRequestsCounter(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void increment_WhenCalled_IncrementCounter() {
        String key = LimitType.SPOTIFY_EXTERNAL.getCacheKey();
        assertEquals(0, integerValueOperations.get(key));
        requestLimitService.increment(LimitType.SPOTIFY_EXTERNAL);
        assertEquals(1, integerValueOperations.get(key));
    }

    @Test
    public void reset_WhenCalled_ResetCounter() {
        String key = LimitType.SPOTIFY_EXTERNAL.getCacheKey();
        integerValueOperations.set(key, 235);
        assertEquals(235, integerValueOperations.get(key));
        requestLimitService.reset(LimitType.SPOTIFY_EXTERNAL);
        assertEquals(0, integerValueOperations.get(key));
    }
}
