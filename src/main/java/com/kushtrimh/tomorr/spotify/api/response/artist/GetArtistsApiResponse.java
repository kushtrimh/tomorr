package com.kushtrimh.tomorr.spotify.api.response.artist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class GetArtistsApiResponse {
    private List<ArtistResponseData> artists = new ArrayList<>();

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
        GetArtistsApiResponse response = (GetArtistsApiResponse) o;
        return Objects.equals(artists, response.artists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artists);
    }

    @Override
    public String toString() {
        return "GetArtistsApiResponse{" +
                "artists=" + artists +
                '}';
    }
}
