package com.kushtrimh.tomorr.limit;

import com.kushtrimh.tomorr.properties.LimitProperties;
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
    private final LimitProperties limitProperties;


    public DefaultRequestLimitService(LimitProperties limitProperties, RedisTemplate<String, Integer> integerRedisTemplate) {
        this.integerRedisTemplate = integerRedisTemplate;
        this.integerValueOperations = integerRedisTemplate.opsForValue();
        this.limitProperties = limitProperties;
    }

    @PostConstruct
    public void init() {
        for (LimitType limitTypes: LimitType.values()) {
            String cacheKey = limitTypes.getCacheKey();
            if (cacheKey != null) {
                integerValueOperations.setIfAbsent(cacheKey, 0);
            }
        }
    }

    @Override
    public boolean canSendRequest(LimitType limitType) {
        return (getLimit(limitType) - getCount(limitType))  > 0;
    }

    @Override
    public boolean cantSendRequest(LimitType limitType) {
        return !canSendRequest(limitType);
    }

    @Override
    public int getRemainingRequestLimit(LimitType limitType) {
        return getLimit(limitType) - getCount(limitType);
    }

    @Override
    public int getSentRequestsCounter(LimitType limitType) {
        return getCount(limitType);
    }

    @Override
    public long increment(LimitType limitType) {
        if (limitType == LimitType.GLOBAL) {
            return -1L;
        }
        return integerValueOperations.increment(limitType.getCacheKey());
    }

    @Override
    public void reset(LimitType limitType) {
        if (limitType == LimitType.GLOBAL) {
            return;
        }
        integerValueOperations.set(limitType.getCacheKey(), 0);
    }

    private int getCount(LimitType limitType) {
        if (limitType == LimitType.GLOBAL) {
            int count = 0;
            for (LimitType type: LimitType.getCacheableTypes()) {
                count += integerValueOperations.get(type.getCacheKey());
            }
            return count;
        }
        return integerValueOperations.get(limitType.getCacheKey());
    }

    private int getLimit(LimitType limitType) {
        return switch (limitType) {
            case SPOTIFY_EXTERNAL -> limitProperties.getSpotify();
            case ARTIST_SEARCH -> limitProperties.getArtistSearch();
            default -> limitProperties.getGlobal();
        };
    }
}
