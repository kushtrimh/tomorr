package com.kushtrimh.tomorr.limit;

import com.kushtrimh.tomorr.properties.LimitProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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
        assertEquals(100, requestLimitService.getRemainingRequestLimit(limitType));

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
    public void increment_WhenCalledWithNull_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> requestLimitService.increment(null));
    }

    @Test
    public void reset_WhenCalled_ResetRequestCounter() {
        requestLimitService.reset(LimitType.SPOTIFY_EXTERNAL);
        verify(integerValueOperations, times(1)).set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 0);
    }

    @Test
    public void reset_WhenCalledWithNull_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> requestLimitService.reset(null));
    }

    @Test
    public void resetAll_WhenCalled_ResetAllCounters() {
        RedisOperations<String, Integer> operations = mock(RedisOperations.class);
        ValueOperations<String, Integer> valueOperations = mock(ValueOperations.class);
        when(operations.opsForValue()).thenReturn(valueOperations);
        when(integerRedisTemplate.executePipelined(any(SessionCallback.class)))
                .thenAnswer(invocation -> {
                    SessionCallback<?> sessionCallback = invocation.getArgument(0, SessionCallback.class);
                    return sessionCallback.execute(operations);
                });
        requestLimitService.resetAll();
        verify(valueOperations, times(1)).set(LimitType.SPOTIFY_EXTERNAL.getCacheKey(), 0);
        verify(valueOperations, times(1)).set(LimitType.ARTIST_SEARCH.getCacheKey(), 0);
    }

    @Test
    public void tryFor_WhenUsingNullAsLimitType_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> requestLimitService.tryFor(null));
    }

    @Test
    public void tryFor_WhenCantSendRequest_ReturnFalse() {
        assertTryFor(false, never(), 450);
    }

    @Test
    public void tryFor_WheCanSendRequest_ReturnTrueAndIncrement() {
        assertTryFor(true, times(1), 400);
    }

    private void assertTryFor(boolean canSendRequest, VerificationMode verificationMode, int requestCount) {
        LimitType limitType = LimitType.SPOTIFY_EXTERNAL;
        String cacheKey = limitType.getCacheKey();
        when(limitProperties.getSpotify()).thenReturn(450);
        when(integerValueOperations.get(cacheKey)).thenReturn(requestCount);
        assertEquals(canSendRequest, requestLimitService.tryFor(limitType));
        verify(integerValueOperations, verificationMode).increment(cacheKey);
    }
}