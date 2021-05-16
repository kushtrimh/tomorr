package com.kushtrimh.tomorr.spotify.api.response.artist;

import java.util.ArrayList;
import java.util.List;

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
    public String toString() {
        return "GetArtistsApiResponse{" +
                "artists=" + artists +
                '}';
    }
}
