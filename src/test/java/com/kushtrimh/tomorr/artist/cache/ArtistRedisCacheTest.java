package com.kushtrimh.tomorr.artist.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.kushtrimh.tomorr.artist.cache.ArtistRedisCache.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith(MockitoExtension.class)
public class ArtistRedisCacheTest {

    @Mock
    private ZSetOperations<String, String> zSetOperations;
    @Mock
    private StringRedisTemplate stringRedisTemplate;

    private ArtistRedisCache artistRedisCache;

    @BeforeEach
    public void init() {
        artistRedisCache = new ArtistRedisCache(stringRedisTemplate);
    }

    @Test
    public void getArtistIds_WhenArtistIdsAreEmpty_ReturnEmptyList() {
        when(stringRedisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(stringRedisTemplate.opsForZSet().range(SEARCHED_ARTIST_IDS_KEY, 0, -1))
                .thenReturn(new HashSet<>());
        Set<String> artistIds = artistRedisCache.getArtistIds();
        assertNotNull(artistIds);
        assertTrue(artistIds.isEmpty());
    }

    @Test
    public void getArtistIds_WhenArtistIdsExists_ReturnArtistIds() {
        var artistIds = Set.of("artist1", "artist2", "artist3");
        when(stringRedisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(stringRedisTemplate.opsForZSet().range(SEARCHED_ARTIST_IDS_KEY, 0, -1))
                .thenReturn(artistIds);
        Set<String> returnedArtistIds = artistRedisCache.getArtistIds();
        assertNotNull(returnedArtistIds);
        assertEquals(artistIds, returnedArtistIds);
    }

    @Test
    public void putArtistIds_WhenArtistIdsListIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> artistRedisCache.putArtistIds(null));
    }

    @Test
    public void putArtistIds_WhenArtistIdsListContainsValues_CacheArtistIds() {
        var artistIds = List.of("artist1", "artist2", "artist3");
        StringRedisConnection redisConnection = mock(StringRedisConnection.class);
        when(stringRedisTemplate.executePipelined(any(RedisCallback.class)))
                .thenAnswer(invocation -> {
                    Object[] args = invocation.getArguments();
                    RedisCallback<?> callback = (RedisCallback<?>) args[0];
                    return callback.doInRedis(redisConnection);
                });
        artistRedisCache.putArtistIds(artistIds);

        verify(redisConnection, times(1)).zAdd(
                eq(SEARCHED_ARTIST_IDS_KEY), any(Double.class), eq("artist1"));
        verify(redisConnection, times(1)).zAdd(
                eq(SEARCHED_ARTIST_IDS_KEY), any(Double.class), eq("artist2"));
        verify(redisConnection, times(1)).zAdd(
                eq(SEARCHED_ARTIST_IDS_KEY), any(Double.class), eq("artist3"));
        verify(redisConnection, never()).zAdd(
                eq(SEARCHED_ARTIST_IDS_KEY), any(Double.class), eq("artist4"));

        verify(redisConnection, times(1))
                .zRemRange(SEARCHED_ARTIST_IDS_KEY, 0, -1001);
    }

    @Test
    public void putArtistIds_WhenArtistIdsAreEmpty_ReturnImmediately() {
        List<String> artistIds = Collections.emptyList();
        artistRedisCache.putArtistIds(artistIds);
        verify(stringRedisTemplate, never()).executePipelined(any(RedisCallback.class));
    }

    @Test
    public void containsArtistId_WhenArtistIdIsNull_ReturnFalse() {
        assertFalse(artistRedisCache.containsArtistId(null));
    }

    @Test
    public void containsArtistId_WhenArtistIdDoesNotExistInCache_ReturnFalse() {
        var artistId = "artist1";
        when(stringRedisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(stringRedisTemplate.opsForZSet().score(SEARCHED_ARTIST_IDS_KEY, artistId))
            .thenReturn(null);
        assertFalse(artistRedisCache.containsArtistId(artistId));
    }

    @Test
    public void containsArtistId_WhenArtistIdDoesExistInCache_ReturnTrue() {
        var artistId = "artist1";
        when(stringRedisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(stringRedisTemplate.opsForZSet().score(SEARCHED_ARTIST_IDS_KEY, artistId))
                .thenReturn(1.0);
        assertTrue(artistRedisCache.containsArtistId(artistId));
    }
}