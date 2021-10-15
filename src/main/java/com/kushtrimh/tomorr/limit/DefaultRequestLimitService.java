package com.kushtrimh.tomorr.limit;

import com.kushtrimh.tomorr.properties.LimitProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultRequestLimitService implements RequestLimitService {

    private final RedisTemplate<String, Integer> integerRedisTemplate;
    private final ValueOperations<String, Integer> integerValueOperations;
    private final LimitProperties limitProperties;


    public DefaultRequestLimitService(LimitProperties limitProperties, RedisTemplate<String, Integer> integerRedisTemplate) {
        this.integerRedisTemplate = integerRedisTemplate;
        this.integerValueOperations = integerRedisTemplate.opsForValue();
        this.limitProperties = limitProperties;
    }

    @PostConstruct
    public void init() {
        for (LimitType limitTypes : LimitType.values()) {
            String cacheKey = limitTypes.getCacheKey();
            if (cacheKey != null) {
                integerValueOperations.setIfAbsent(cacheKey, 0);
            }
        }
    }

    @Override
    public boolean canSendRequest(LimitType limitType) {
        return getRemainingRequestLimit(limitType, integerValueOperations) > 0;
    }

    @Override
    public boolean cantSendRequest(LimitType limitType) {
        return !canSendRequest(limitType);
    }

    @Override
    public int getRemainingRequestLimit(LimitType limitType) {
        return getRemainingRequestLimit(limitType, integerValueOperations);
    }

    @Override
    public int getSentRequestsCounter(LimitType limitType) {
        return getCount(limitType, integerValueOperations);
    }

    @Override
    public void increment(LimitType limitType) {
        Objects.requireNonNull(limitType);
        increment(limitType, integerValueOperations);
    }

    @Override
    public boolean tryFor(LimitType limitType) {
        boolean canSendRequest = getRemainingRequestLimit(limitType, integerValueOperations) > 0;
        if (canSendRequest) {
            increment(limitType, integerValueOperations);
            return true;
        }
        return false;
    }

    @Override
    public void reset(LimitType limitType) {
        Objects.requireNonNull(limitType);
        integerValueOperations.set(limitType.getCacheKey(), 0);
    }

    @Override
    public void resetAll() {
        integerRedisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                ValueOperations<String, Integer> valueOperations = (ValueOperations<String, Integer>) redisOperations.opsForValue();
                for (LimitType type : LimitType.getCacheableTypes()) {
                    valueOperations.set(type.getCacheKey(), 0);
                }
                return null;
            }
        });
    }

    private int getCount(LimitType limitType, ValueOperations<String, Integer> operations) {
        return operations.get(limitType.getCacheKey());
    }

    private void increment(LimitType limitType, ValueOperations<String, Integer> operations) {
        operations.increment(limitType.getCacheKey());
    }

    private int getLimit(LimitType limitType) {
        return switch (limitType) {
            case SPOTIFY_SYNC -> limitProperties.getSpotifySync();
            case SPOTIFY_SEARCH -> limitProperties.getSpotifySearch();
            default -> limitProperties.getGlobal();
        };
    }

    private int getRemainingRequestLimit(LimitType limitType, ValueOperations<String, Integer> operations) {
        return getLimit(limitType) - getCount(limitType, operations);
    }
}
