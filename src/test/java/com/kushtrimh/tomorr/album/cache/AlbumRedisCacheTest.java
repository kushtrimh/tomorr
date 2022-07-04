package com.kushtrimh.tomorr.album.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({MockitoExtension.class})
class AlbumRedisCacheTest {

    @Mock
    private RedisTemplate<String, Integer> template;

    private AlbumRedisCache albumRedisCache;

    @BeforeEach
    public void init() {
        albumRedisCache = new AlbumRedisCache(template);
    }

    @Test
    public void isNotificationSent_WhenAlbumNameIsNull_ReturnFalse() {
        assertFalse(albumRedisCache.isNotificationSent(null));
    }

    @Test
    public void isNotificationSent_WhenAlbumNameIsBlank_ReturnFalse() {
        assertFalse(albumRedisCache.isNotificationSent("   "));
    }

    @Test
    public void isNotificationSent_WhenNotificationHasNotBeenSentForAlbum_ReturnFalse() {
        var albumName = "Tomorr the new album";
        var albumNameHash = "a997257e590b3aeaa7cc6734a6e13573";
        when(template.hasKey(AlbumRedisCache.NOTIFIED_ALBUM_KEY + ":" + albumNameHash)).thenReturn(false);
        assertFalse(albumRedisCache.isNotificationSent(albumName));
    }

    @Test
    public void isNotificationSent_WhenNotificationHasBeenSentForAlbum_ReturnTrue() {
        var albumName = "Tomorr the new album";
        var albumNameHash = "a997257e590b3aeaa7cc6734a6e13573";
        when(template.hasKey(AlbumRedisCache.NOTIFIED_ALBUM_KEY + ":" + albumNameHash)).thenReturn(true);
        assertTrue(albumRedisCache.isNotificationSent(albumName));
    }
}