package com.kushtrimh.tomorr.spotify.limit;

import com.kushtrimh.tomorr.properties.SpotifyProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultRequestLimitService implements RequestLimitService {

    private final RedisTemplate<String, Integer> integerRedisTemplate;
    private final ValueOperations<String, Integer> integerValueOperations;
    private final int globalRequestLimit;


    public DefaultRequestLimitService(SpotifyProperties spotifyProperties, RedisTemplate<String, Integer> integerRedisTemplate) {
        this.integerRedisTemplate = integerRedisTemplate;
        this.integerValueOperations = integerRedisTemplate.opsForValue();
        this.globalRequestLimit = spotifyProperties.getRequestLimit();
    }

    @PostConstruct
    public void init() {
        for (LimitType limitTypes: LimitType.values()) {
            integerValueOperations.setIfAbsent(limitTypes.getCacheKey(), 0);
        }
    }

    @Override
    public boolean canSendRequest(LimitType limitType) {
        int sendRequestsCounter = integerValueOperations.get(limitType.getCacheKey());
        return (globalRequestLimit - sendRequestsCounter)  > 0;
    }

    @Override
    public boolean cantSendRequest(LimitType limitType) {
        return !canSendRequest(limitType);
    }

    @Override
    public int getRemainingRequestLimit(LimitType limitType) {
        return globalRequestLimit - integerValueOperations.get(limitType.getCacheKey());
    }

    @Override
    public int getSentRequestsCounter(LimitType limitType) {
        return integerValueOperations.get(limitType.getCacheKey());
    }

    @Override
    public long increment(LimitType limitType) {
        return integerValueOperations.increment(limitType.getCacheKey());
    }

    @Override
    public void reset(LimitType limitType) {
        integerValueOperations.set(limitType.getCacheKey(), 0);
    }
}
