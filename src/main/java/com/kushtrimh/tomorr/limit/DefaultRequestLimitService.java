package com.kushtrimh.tomorr.limit;

import com.kushtrimh.tomorr.properties.LimitProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

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
        increment(limitType, integerValueOperations);
    }

    @Override
    public boolean tryFor(LimitType limitType) {
        List<Object> results = integerRedisTemplate.executePipelined(new SessionCallback<List<Object>>() {
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                ValueOperations<String, Integer> operations = (ValueOperations<String, Integer>) redisOperations.opsForValue();
                boolean canSendRequest = getRemainingRequestLimit(limitType, operations) > 0;
                if (canSendRequest) {
                    increment(limitType, operations);
                }
                return null;
            }
        });
        if (results.isEmpty()) {
            return false;
        }
        return (Boolean) results.get(0);
    }

    @Override
    public void reset(LimitType limitType) {
        if (limitType == LimitType.ALL) {
            for (LimitType type : LimitType.getCacheableTypes()) {
                integerValueOperations.set(type.getCacheKey(), 0);
            }
        } else {
            integerValueOperations.set(limitType.getCacheKey(), 0);
        }
    }

    private int getCount(LimitType limitType, ValueOperations<String, Integer> operations) {
        if (limitType == LimitType.ALL) {
            int count = 0;
            for (LimitType type : LimitType.getCacheableTypes()) {
                count += operations.get(type.getCacheKey());
            }
            return count;
        }
        return operations.get(limitType.getCacheKey());
    }

    private void increment(LimitType limitType, ValueOperations<String, Integer> operations) {
        if (limitType == LimitType.ALL) {
            for (LimitType type : LimitType.getCacheableTypes()) {
                integerValueOperations.increment(type.getCacheKey());
            }
        } else {
            integerValueOperations.increment(limitType.getCacheKey());
        }
    }

    private int getLimit(LimitType limitType) {
        return switch (limitType) {
            case SPOTIFY_EXTERNAL -> limitProperties.getSpotify();
            case ARTIST_SEARCH -> limitProperties.getArtistSearch();
            default -> limitProperties.getGlobal();
        };
    }

    private int getRemainingRequestLimit(LimitType limitType, ValueOperations<String, Integer> operations) {
        return getLimit(limitType) - getCount(limitType, operations);
    }
}
