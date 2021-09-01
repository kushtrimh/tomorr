package com.kushtrimh.tomorr.spotify.limit;

/**
 * @author Kushtrim Hajrizi
 */
public enum LimitType {
    SPOTIFY_EXTERNAL("spotify:requestCounter"),
    ARTIST_SEARCH("artistSearch:requestCounter");

    private String cacheKey;

    LimitType(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheKey() {
        return cacheKey;
    }
}
