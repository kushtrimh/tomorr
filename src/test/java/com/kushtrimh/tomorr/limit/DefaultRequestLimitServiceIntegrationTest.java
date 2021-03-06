package com.kushtrimh.tomorr.limit;

import com.kushtrimh.tomorr.configuration.TestLimitConfiguration;
import com.kushtrimh.tomorr.configuration.TestRedisConfiguration;
import com.kushtrimh.tomorr.configuration.TestSpotifyConfiguration;
import com.kushtrimh.tomorr.extension.TestRedisExtension;
import com.kushtrimh.tomorr.properties.LimitProperties;
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
@Tags(value = {@Tag("redis"), @Tag("integration")})
@ContextConfiguration(classes = {TestSpotifyConfiguration.class,
        TestRedisConfiguration.class, TestLimitConfiguration.class})
@ExtendWith({SpringExtension.class, MockitoExtension.class, TestRedisExtension.class})
public class DefaultRequestLimitServiceIntegrationTest {

    private final LimitProperties limitProperties;
    private final RedisTemplate<String, Integer> integerRedisTemplate;
    private final ValueOperations<String, Integer> integerValueOperations;

    private DefaultRequestLimitService requestLimitService;

    @Autowired
    public DefaultRequestLimitServiceIntegrationTest(LimitProperties limitProperties, RedisTemplate<String, Integer> integerRedisTemplate) {
        this.limitProperties = limitProperties;
        this.integerRedisTemplate = integerRedisTemplate;
        this.integerValueOperations = integerRedisTemplate.opsForValue();
    }

    @BeforeEach
    public void init() {
        requestLimitService = new DefaultRequestLimitService(limitProperties, integerRedisTemplate);
        integerValueOperations.set(LimitType.SPOTIFY_SYNC.getCacheKey(), 0);
        integerValueOperations.set(LimitType.SPOTIFY_SEARCH.getCacheKey(), 0);
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsBiggerThanLimit_ReturnFalse() {
        integerValueOperations.set(LimitType.SPOTIFY_SYNC.getCacheKey(), 475);
        assertFalse(requestLimitService.canSendRequest(LimitType.SPOTIFY_SYNC));
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsEqualsToLimit_ReturnFalse() {
        integerValueOperations.set(LimitType.SPOTIFY_SYNC.getCacheKey(), 450);
        assertFalse(requestLimitService.canSendRequest(LimitType.SPOTIFY_SYNC));
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsSmallerThanLimit_ReturnTrue() {
        integerValueOperations.set(LimitType.SPOTIFY_SYNC.getCacheKey(), 25);
        assertTrue(requestLimitService.canSendRequest(LimitType.SPOTIFY_SYNC));
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsBiggerThanLimit_ReturnTrue() {
        integerValueOperations.set(LimitType.SPOTIFY_SYNC.getCacheKey(), 475);
        assertTrue(requestLimitService.cantSendRequest(LimitType.SPOTIFY_SYNC));
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsEqualsToLimit_ReturnTrue() {
        integerValueOperations.set(LimitType.SPOTIFY_SYNC.getCacheKey(), 450);
        assertTrue(requestLimitService.cantSendRequest(LimitType.SPOTIFY_SYNC));
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsSmallerThanLimit_ReturnFalse() {
        integerValueOperations.set(LimitType.SPOTIFY_SYNC.getCacheKey(), 25);
        assertFalse(requestLimitService.cantSendRequest(LimitType.SPOTIFY_SYNC));
    }

    @Test
    public void getRemainingRequestLimit_WhenCalled_ReturnRemainingRequestLimit() {
        integerValueOperations.set(LimitType.SPOTIFY_SYNC.getCacheKey(), 340);
        assertEquals(110, requestLimitService.getRemainingRequestLimit(LimitType.SPOTIFY_SYNC));
    }

    @Test
    public void getSentRequestsCounter_WhenCalled_ReturnRequestCounter() {
        integerValueOperations.set(LimitType.SPOTIFY_SYNC.getCacheKey(), 154);
        assertEquals(154, requestLimitService.getSentRequestsCounter(LimitType.SPOTIFY_SYNC));
    }

    @Test
    public void increment_WhenCalled_IncrementCounter() {
        String key = LimitType.SPOTIFY_SYNC.getCacheKey();
        assertEquals(0, integerValueOperations.get(key));
        requestLimitService.increment(LimitType.SPOTIFY_SYNC);
        assertEquals(1, integerValueOperations.get(key));
    }

    @Test
    public void reset_WhenCalled_ResetCounter() {
        String key = LimitType.SPOTIFY_SYNC.getCacheKey();
        integerValueOperations.set(key, 235);
        assertEquals(235, integerValueOperations.get(key));
        requestLimitService.reset(LimitType.SPOTIFY_SYNC);
        assertEquals(0, integerValueOperations.get(key));
    }

    @Test
    public void resetAll_WhenCalled_ResetAllCounters() {
        integerValueOperations.set(LimitType.SPOTIFY_SYNC.getCacheKey(), 100);
        integerValueOperations.set(LimitType.SPOTIFY_SEARCH.getCacheKey(), 50);
        assertAll(
                () -> assertEquals(100, integerRedisTemplate.opsForValue().get(LimitType.SPOTIFY_SYNC.getCacheKey())),
                () -> assertEquals(50, integerRedisTemplate.opsForValue().get(LimitType.SPOTIFY_SEARCH.getCacheKey()))
        );
        requestLimitService.resetAll();
        assertAll(
                () -> assertEquals(0, integerRedisTemplate.opsForValue().get(LimitType.SPOTIFY_SYNC.getCacheKey())),
                () -> assertEquals(0, integerRedisTemplate.opsForValue().get(LimitType.SPOTIFY_SEARCH.getCacheKey()))
        );
    }

    @Test
    public void tryFor_WhenCantSendRequest_ReturnFalse() {
        LimitType limitType = LimitType.SPOTIFY_SYNC;
        integerValueOperations.set(limitType.getCacheKey(), 450);
        assertFalse(requestLimitService.tryFor(limitType));
    }

    @Test
    public void tryFor_WheCanSendRequest_ReturnTrueAndIncrement() {
        LimitType limitType = LimitType.SPOTIFY_SYNC;
        integerValueOperations.set(limitType.getCacheKey(), 350);
        boolean canSend = requestLimitService.tryFor(limitType);
        int count = integerValueOperations.get(limitType.getCacheKey());
        assertAll(
                () -> assertTrue(canSend),
                () -> assertEquals(351, count)
        );
    }
}
