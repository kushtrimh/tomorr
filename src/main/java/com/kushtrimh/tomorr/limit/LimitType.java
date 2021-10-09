package com.kushtrimh.tomorr.limit;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
public enum LimitType {
    SPOTIFY_SYNC("limit:spotify:sync"),
    SPOTIFY_SEARCH("limit:spotify:search");

    private final String cacheKey;

    LimitType(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public static List<LimitType> getCacheableTypes() {
        return List.of(LimitType.SPOTIFY_SYNC, LimitType.SPOTIFY_SEARCH);
    }

    public String getCacheKey() {
        return cacheKey;
    }
}
