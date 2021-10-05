package com.kushtrimh.tomorr.limit;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
public enum LimitType {
    SPOTIFY_EXTERNAL("spotify:requestCounter"),
    ARTIST_SEARCH("artistSearch:requestCounter");

    private final String cacheKey;

    LimitType(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public static List<LimitType> getCacheableTypes() {
        return List.of(LimitType.SPOTIFY_EXTERNAL, LimitType.ARTIST_SEARCH);
    }

    public String getCacheKey() {
        return cacheKey;
    }
}
