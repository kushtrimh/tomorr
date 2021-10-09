package com.kushtrimh.tomorr.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
@Component
@ConfigurationProperties(prefix = "request-limit")
public class LimitProperties {
    private int spotifySync;
    private int spotifySearch;

    public int getSpotifySync() {
        return spotifySync;
    }

    public void setSpotifySync(int spotifySync) {
        this.spotifySync = spotifySync;
    }

    public int getSpotifySearch() {
        return spotifySearch;
    }

    public void setSpotifySearch(int spotifySearch) {
        this.spotifySearch = spotifySearch;
    }

    public int getGlobal() {
        return spotifySearch + spotifySync;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LimitProperties that = (LimitProperties) o;
        return spotifySync == that.spotifySync && spotifySearch == that.spotifySearch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(spotifySync, spotifySearch);
    }

    @Override
    public String toString() {
        return "LimitProperties{" +
                "spotifySync=" + spotifySync +
                ", spotifySearch=" + spotifySearch +
                '}';
    }
}
