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

    public static final String SENT_REQUESTS_COUNTER_KEY= "spotify:sentRequestsCounter";

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
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 0);
    }

    @Override
    public boolean canSendRequest() {
        int sendRequestsCounter = integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY);
        return (globalRequestLimit - sendRequestsCounter)  > 0;
    }

    @Override
    public boolean cantSendRequest() {
        return !canSendRequest();
    }

    @Override
    public int getRemainingRequestLimit() {
        return globalRequestLimit - integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY);
    }

    @Override
    public int getSentRequestsCounter() {
        return integerValueOperations.get(SENT_REQUESTS_COUNTER_KEY);
    }

    @Override
    public long increment() {
        return integerValueOperations.increment(SENT_REQUESTS_COUNTER_KEY);
    }

    @Override
    public void reset() {
        integerValueOperations.set(SENT_REQUESTS_COUNTER_KEY, 0);
    }
}
