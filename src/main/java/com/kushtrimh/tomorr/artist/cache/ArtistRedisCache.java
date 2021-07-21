package com.kushtrimh.tomorr.artist.cache;

import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class ArtistRedisCache implements ArtistCache {

    protected static final String SEARCHED_ARTIST_IDS_KEY = "searchedArtistIds";

    private final StringRedisTemplate stringRedisTemplate;

    public ArtistRedisCache(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Set<String> getArtistIds() {
        return stringRedisTemplate.opsForZSet().range(SEARCHED_ARTIST_IDS_KEY, 0, -1);
    }

    @Override
    public void putArtistIds(List<String> artistIds) {
        Objects.requireNonNull(artistIds);
        if (artistIds.isEmpty()) {
            return;
        }
        stringRedisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
            var stringRedisConnection = (StringRedisConnection) redisConnection;
            for (String artistId: artistIds) {
                stringRedisConnection.zAdd(SEARCHED_ARTIST_IDS_KEY, Instant.now().getEpochSecond(), artistId);
            }
            stringRedisConnection.zRemRange(SEARCHED_ARTIST_IDS_KEY, 0,  -1001);
            return null;
        });
    }

    @Override
    public boolean containsArtistId(String artistId) {
        if (artistId == null) {
            return false;
        }
        return stringRedisTemplate.opsForZSet().score(SEARCHED_ARTIST_IDS_KEY, artistId) != null;
    }
}
