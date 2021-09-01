package com.kushtrimh.tomorr.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Kushtrim Hajrizi
 */
@Component
@ConfigurationProperties(prefix = "limit")
public class LimitProperties {
    private int spotify;
    private int artistSearch;

    public int getSpotify() {
        return spotify;
    }

    public void setSpotify(int spotify) {
        this.spotify = spotify;
    }

    public int getArtistSearch() {
        return artistSearch;
    }

    public void setArtistSearch(int artistSearch) {
        this.artistSearch = artistSearch;
    }

    public int getGlobal() {
        return spotify + artistSearch;
    }

    @Override
    public String toString() {
        return "LimitProperties{" +
                "spotify=" + spotify +
                ", artistSearch=" + artistSearch +
                '}';
    }
}
