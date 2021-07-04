package com.kushtrimh.tomorr.spotify.limit;

import com.kushtrimh.tomorr.properties.SpotifyProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static com.kushtrimh.tomorr.spotify.limit.DefaultRequestLimitService.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith(MockitoExtension.class)
public class DefaultRequestLimitServiceTest {

    @Mock
    private RedisTemplate<String, Integer> integerRedisTemplate;
    @Mock
    private ValueOperations<String, Integer> integerValueOperations;
    @Mock
    private SpotifyProperties spotifyProperties;

    private DefaultRequestLimitService requestLimitService;

    @BeforeEach
    public void init() {
        when(integerRedisTemplate.opsForValue()).thenReturn(integerValueOperations);
        when(spotifyProperties.getRequestLimit()).thenReturn(450);
        requestLimitService = new DefaultRequestLimitService(spotifyProperties, integerRedisTemplate);
    }

    @Test
    public void init_WhenInitialized_SetRequestCounter() {
        requestLimitService.init();
        verify(integerValueOperations, times(1))
                .set(SENT_REQUESTS_COUNTER_KEY, 0);
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsBiggerThanLimit_ReturnFalse() {
        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(460);
        assertFalse(requestLimitService.canSendRequest());
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsEqualsToLimit_ReturnFalse() {
        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(450);
        assertFalse(requestLimitService.canSendRequest());
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsSmallerThanLimit_ReturnTrue() {
        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(350);
        assertTrue(requestLimitService.canSendRequest());
    }

    @Test
    public void getRemainingRequestLimit_WhenCalled_ReturnRemainingRequestLimit() {
        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(350);
        assertEquals(100, requestLimitService.getRemainingRequestLimit());

        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(372);
        assertEquals(78, requestLimitService.getRemainingRequestLimit());

        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(414);
        assertEquals(36, requestLimitService.getRemainingRequestLimit());

        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(450);
        assertEquals(0, requestLimitService.getRemainingRequestLimit());

        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(0);
        assertEquals(450, requestLimitService.getRemainingRequestLimit());
    }

    @Test
    public void getSentRequestsCounter_WhenCalled_ReturnRequestCounter() {
        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(250);
        assertEquals(250, requestLimitService.getSentRequestsCounter());

        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(0);
        assertEquals(0, requestLimitService.getSentRequestsCounter());

        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(43);
        assertEquals(43, requestLimitService.getSentRequestsCounter());

        when(integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY)).thenReturn(450);
        assertEquals(450, requestLimitService.getSentRequestsCounter());
    }

    @Test
    public void increment_WhenCalled_IncrementCounter() {
        requestLimitService.increment();
        verify(integerValueOperations, times(1)).increment(SENT_REQUESTS_COUNTER_KEY);
    }

    @Test
    public void reset_WhenCalled_ResetRequestCounter() {
        requestLimitService.reset();
        verify(integerValueOperations, times(1)).set(SENT_REQUESTS_COUNTER_KEY, 0);
    }
}