package com.kushtrimh.tomorr.artist.cache;

import com.kushtrimh.tomorr.configuration.TestRedisConfiguration;
import com.kushtrimh.tomorr.dal.extension.TestRedisExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.kushtrimh.tomorr.artist.cache.ArtistRedisCache.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = { @Tag("redis"), @Tag("integration") })
@ExtendWith({SpringExtension.class, MockitoExtension.class, TestRedisExtension.class})
@ContextConfiguration(classes = TestRedisConfiguration.class)
public class ArtistRedisCacheIntegrationTest {

    private final StringRedisTemplate stringRedisTemplate;
    private ArtistRedisCache artistRedisCache;

    @Autowired
    public ArtistRedisCacheIntegrationTest(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @BeforeEach
    public void init() {
        artistRedisCache = new ArtistRedisCache(stringRedisTemplate);
        stringRedisTemplate.delete(SEARCHED_ARTIST_IDS_KEY);
    }

    @Test
    public void getArtistIds_WhenArtistIdsAreNotInitialized_ReturnEmptyList() {
        assertTrue(artistRedisCache.getArtistIds().isEmpty());
    }

    @Test
    public void getArtistIds_WhenArtistIdsExist_ReturnArtistIds() {
        stringRedisTemplate.opsForZSet().add(SEARCHED_ARTIST_IDS_KEY, "artist1", Instant.now().getEpochSecond());
        stringRedisTemplate.opsForZSet().add(SEARCHED_ARTIST_IDS_KEY, "artist2", Instant.now().getEpochSecond());
        stringRedisTemplate.opsForZSet().add(SEARCHED_ARTIST_IDS_KEY, "artist3", Instant.now().getEpochSecond());
        Set<String> artistIds = artistRedisCache.getArtistIds();
        assertEquals(3, artistIds.size());
        assertTrue(artistIds.contains("artist1"));
        assertTrue(artistIds.contains("artist2"));
        assertTrue(artistIds.contains("artist3"));
        assertFalse(artistIds.contains("artist4"));
    }

    @Test
    public void putArtistIds_WhenArtistIdsContainsValues_CacheArtistIds() {
        List<String> artistIds = List.of("artist1", "artist2", "artist3");
        artistRedisCache.putArtistIds(artistIds);

        Set<String> returnedArtistIds = artistRedisCache.getArtistIds();
        assertEquals(artistIds.size(), returnedArtistIds.size());
        assertTrue(artistIds.contains("artist1"));
        assertTrue(artistIds.contains("artist2"));
        assertTrue(artistIds.contains("artist3"));
    }

    @Test
    public void putArtistIds_WhenCacheLimitIsExceededForArtistIds_RemoveOlderArtistIds() {
        List<String> firstArtistIds = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            firstArtistIds.add("artist" + i);
        }
        artistRedisCache.putArtistIds(firstArtistIds);

        List<String> secondArtistIds = new ArrayList<>();
        for (int i = 1000; i < 1500; i++) {
            secondArtistIds.add("artist" + i);
        }

        artistRedisCache.putArtistIds(secondArtistIds);
        Set<String> returnedArtistIds = artistRedisCache.getArtistIds();
        assertEquals(1000, returnedArtistIds.size());
    }

    @Test
    public void containsArtistId_WhenArtistIdExists_ReturnTrue() {
        stringRedisTemplate.opsForZSet().add(SEARCHED_ARTIST_IDS_KEY, "artist1", Instant.now().getEpochSecond());
        assertTrue(artistRedisCache.containsArtistId("artist1"));
    }

    @Test
    public void containsArtistId_WhenArtistIdDoesNotExist_ReturnFalse() {
        stringRedisTemplate.opsForZSet().add(SEARCHED_ARTIST_IDS_KEY, "artist2", Instant.now().getEpochSecond());
        assertFalse(artistRedisCache.containsArtistId("artist1"));
    }
}
