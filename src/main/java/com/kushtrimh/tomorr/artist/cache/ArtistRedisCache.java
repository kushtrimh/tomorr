package com.kushtrimh.tomorr.artist.cache;

import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
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
    public Set<String> getSearchedArtistIds() {
        return stringRedisTemplate.opsForZSet().range(SEARCHED_ARTIST_IDS_KEY, 0, -1);
    }

    @Override
    public void putSearchedArtistIds(List<String> artistIds) {
        stringRedisTemplate.execute(redisConnection -> {
            StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
            for (String artistId: artistIds) {
                stringRedisConnection.zAdd(SEARCHED_ARTIST_IDS_KEY, Instant.now().getEpochSecond(), artistId);
            }
            stringRedisConnection.zRemRange(SEARCHED_ARTIST_IDS_KEY, 0,  -1000);
            return null;
        }, false, true);
    }

    @Override
    public boolean containsSearchedArtistId(String artistId) {
        return stringRedisTemplate.opsForZSet().score(SEARCHED_ARTIST_IDS_KEY, artistId) != null;
    }
}
