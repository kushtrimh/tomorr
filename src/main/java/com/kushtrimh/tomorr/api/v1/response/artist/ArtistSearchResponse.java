package com.kushtrimh.tomorr.api.v1.response.artist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class ArtistSearchResponse {
    private List<ArtistResponseData> artists = new ArrayList<>();

    public ArtistSearchResponse() {
    }

    public ArtistSearchResponse(List<ArtistResponseData> artists) {
        this.artists = artists;
    }

    public List<ArtistResponseData> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistResponseData> artists) {
        this.artists = artists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistSearchResponse that = (ArtistSearchResponse) o;
        return Objects.equals(artists, that.artists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artists);
    }

    @Override
    public String toString() {
        return "ArtistSearchResponse{" +
                "artists=" + artists +
                '}';
    }
}
