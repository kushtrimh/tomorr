package com.kushtrimh.tomorr.album.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class AlbumRedisCache implements AlbumCache {

    protected static final String NOTIFIED_ALBUM_KEY = "notifiedAlbum";

    private final RedisTemplate<String, Integer> template;

    public AlbumRedisCache(RedisTemplate<String, Integer> template) {
        this.template = template;
    }

    @Override
    public boolean isNotificationSent(String albumName) {
        if (albumName == null || albumName.isBlank()) {
            return false;
        }
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        var hashedAlbumName = HexFormat.of().formatHex(
                messageDigest.digest(albumName.getBytes(StandardCharsets.UTF_8)));
        return Boolean.TRUE.equals(template.hasKey(NOTIFIED_ALBUM_KEY + ":" + hashedAlbumName));
    }
}
