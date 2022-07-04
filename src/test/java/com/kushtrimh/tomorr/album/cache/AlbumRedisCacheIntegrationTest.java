package com.kushtrimh.tomorr.album.cache;

import com.kushtrimh.tomorr.configuration.RedisConfiguration;
import com.kushtrimh.tomorr.configuration.TestRedisConfiguration;
import com.kushtrimh.tomorr.extension.TestRedisExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = {@Tag("redis"), @Tag("integration")})
@ExtendWith({SpringExtension.class, MockitoExtension.class, TestRedisExtension.class})
@ContextConfiguration(classes = {TestRedisConfiguration.class, RedisConfiguration.class})
class AlbumRedisCacheIntegrationTest {

    private final RedisTemplate<String, Integer> template;

    private AlbumRedisCache albumRedisCache;

    @Autowired
    public AlbumRedisCacheIntegrationTest(RedisTemplate<String, Integer> template) {
        this.template = template;
    }

    @BeforeEach
    public void init() {
        albumRedisCache = new AlbumRedisCache(template);
    }

    @Test
    public void isNotificationSent_WhenKeyDoesNotExists_ReturnFalse() {
        assertFalse(albumRedisCache.isNotificationSent("album1"));
    }

    @Test
    public void isNotificationSent_WhenKeyExists_ReturnTrue() {
        var albumName = "Tomorr the new album";
        var albumNameHash = "a997257e590b3aeaa7cc6734a6e13573";
        template.opsForValue().set(AlbumRedisCache.NOTIFIED_ALBUM_KEY + ":" + albumNameHash, 1);
        assertTrue(albumRedisCache.isNotificationSent(albumName));
    }
}