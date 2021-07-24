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

import static com.kushtrimh.tomorr.spotify.limit.DefaultRequestLimitService.*;
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
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 0);
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsBiggerThanLimit_ReturnFalse() {
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 475);
        assertFalse(requestLimitService.canSendRequest());
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsEqualsToLimit_ReturnFalse() {
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 450);
        assertFalse(requestLimitService.canSendRequest());
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsSmallerThanLimit_ReturnTrue() {
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 25);
        assertTrue(requestLimitService.canSendRequest());
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsBiggerThanLimit_ReturnTrue() {
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 475);
        assertTrue(requestLimitService.cantSendRequest());
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsEqualsToLimit_ReturnTrue() {
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 450);
        assertTrue(requestLimitService.cantSendRequest());
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsSmallerThanLimit_ReturnFalse() {
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 25);
        assertFalse(requestLimitService.cantSendRequest());
    }

    @Test
    public void getRemainingRequestLimit_WhenCalled_ReturnRemainingRequestLimit() {
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 340);
        assertEquals(110, requestLimitService.getRemainingRequestLimit());
    }

    @Test
    public void getSentRequestsCounter_WhenCalled_ReturnRequestCounter() {
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 154);
        assertEquals(154, requestLimitService.getSentRequestsCounter());
    }

    @Test
    public void increment_WhenCalled_IncrementCounter() {
        assertEquals(0, integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY));
        requestLimitService.increment();
        assertEquals(1, integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY));
    }

    @Test
    public void reset_WhenCalled_ResetCounter() {
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 235);
        assertEquals(235, integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY));
        requestLimitService.reset();
        assertEquals(0, integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY));
    }
}
