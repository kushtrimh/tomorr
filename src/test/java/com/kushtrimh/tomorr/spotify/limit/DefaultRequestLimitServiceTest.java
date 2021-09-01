package com.kushtrimh.tomorr.spotify.limit;

import com.kushtrimh.tomorr.properties.LimitProperties;
import com.kushtrimh.tomorr.properties.SpotifyProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

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
    private LimitProperties limitProperties;

    private DefaultRequestLimitService requestLimitService;

    @BeforeEach
    public void init() {
        when(integerRedisTemplate.opsForValue()).thenReturn(integerValueOperations);
        requestLimitService = new DefaultRequestLimitService(limitProperties, integerRedisTemplate);
    }

    @Test
    public void init_WhenInitialized_SetRequestCounter() {
        requestLimitService.init();
        verify(integerValueOperations, times(1))
                .setIfAbsent(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 0);
        verify(integerValueOperations, times(1))
                .setIfAbsent(LimitType.ARTIST_SEARCH.getCacheKey(), 0);
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsBiggerThanLimit_ReturnFalse() {
        when(limitProperties.getSpotify()).thenReturn(450);
        when(integerValueOperations.get(LimitType.SPOTIFY_EXTERNAL.getCacheKey())).thenReturn(460);
        assertFalse(requestLimitService.canSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsEqualsToLimit_ReturnFalse() {
        when(limitProperties.getSpotify()).thenReturn(450);
        when(integerValueOperations.get(LimitType.SPOTIFY_EXTERNAL.getCacheKey())).thenReturn(450);
        assertFalse(requestLimitService.canSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void canSendRequest_WhenRequestCounterIsSmallerThanLimit_ReturnTrue() {
        when(limitProperties.getSpotify()).thenReturn(450);
        when(integerValueOperations.get(LimitType.SPOTIFY_EXTERNAL.getCacheKey())).thenReturn(350);
        assertTrue(requestLimitService.canSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsBiggerThanLimit_ReturnTrue() {
        when(limitProperties.getSpotify()).thenReturn(450);
        when(integerValueOperations.get(LimitType.SPOTIFY_EXTERNAL.getCacheKey())).thenReturn(460);
        assertTrue(requestLimitService.cantSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsEqualsToLimit_ReturnTrue() {
        when(limitProperties.getSpotify()).thenReturn(450);
        when(integerValueOperations.get(LimitType.SPOTIFY_EXTERNAL.getCacheKey())).thenReturn(450);
        assertTrue(requestLimitService.cantSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void cantSendRequest_WhenRequestCounterIsSmallerThanLimit_ReturnFalse() {
        when(limitProperties.getSpotify()).thenReturn(450);
        when(integerValueOperations.get(LimitType.SPOTIFY_EXTERNAL.getCacheKey())).thenReturn(350);
        assertFalse(requestLimitService.cantSendRequest(LimitType.SPOTIFY_EXTERNAL));
    }

    @Test
    public void getRemainingRequestLimit_WhenCalled_ReturnRemainingRequestLimit() {
        when(limitProperties.getSpotify()).thenReturn(450);
        var limitType = LimitType.SPOTIFY_EXTERNAL;
        var key = limitType.getCacheKey();

        when(integerValueOperations.get(key)).thenReturn(350);
        assertEquals(100, requestLimitService.getRemainingRequestLimit(LimitType.SPOTIFY_EXTERNAL));

        when(integerValueOperations.get(key)).thenReturn(372);
        assertEquals(78, requestLimitService.getRemainingRequestLimit(limitType));

        when(integerValueOperations.get(key)).thenReturn(414);
        assertEquals(36, requestLimitService.getRemainingRequestLimit(limitType));

        when(integerValueOperations.get(key)).thenReturn(450);
        assertEquals(0, requestLimitService.getRemainingRequestLimit(limitType));

        when(integerValueOperations.get(key)).thenReturn(0);
        assertEquals(450, requestLimitService.getRemainingRequestLimit(limitType));
    }

    @Test
    public void getSentRequestsCounter_WhenCalled_ReturnRequestCounter() {
        var limitType = LimitType.SPOTIFY_EXTERNAL;
        var key = limitType.getCacheKey();

        when(integerValueOperations.get(key)).thenReturn(250);
        assertEquals(250, requestLimitService.getSentRequestsCounter(limitType));

        when(integerValueOperations.get(key)).thenReturn(0);
        assertEquals(0, requestLimitService.getSentRequestsCounter(limitType));

        when(integerValueOperations.get(key)).thenReturn(43);
        assertEquals(43, requestLimitService.getSentRequestsCounter(limitType));

        when(integerValueOperations.get(key)).thenReturn(450);
        assertEquals(450, requestLimitService.getSentRequestsCounter(limitType));
    }

    @Test
    public void increment_WhenCalled_IncrementCounter() {
        requestLimitService.increment(LimitType.SPOTIFY_EXTERNAL);
        verify(integerValueOperations, times(1)).increment(LimitType.SPOTIFY_EXTERNAL.getCacheKey());
    }

    @Test
    public void increment_WhenCalledForGlobal_DoNotIncrement() {
        requestLimitService.increment(LimitType.GLOBAL);
        verify(integerValueOperations, never()).increment(any(String.class));
    }

    @Test
    public void reset_WhenCalled_ResetRequestCounter() {
        requestLimitService.reset(LimitType.SPOTIFY_EXTERNAL);
        verify(integerValueOperations, times(1)).set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 0);
    }

    @Test
    public void reset_WhenCalledForGlobal_DoNotReset() {
        requestLimitService.reset(LimitType.GLOBAL);
        verify(integerValueOperations, never()).set(any(String.class), eq(0));
    }
}