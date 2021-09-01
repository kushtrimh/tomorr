package com.kushtrimh.tomorr.spotify.api.response;

import com.kushtrimh.tomorr.spotify.api.response.artist.ArtistResponseData;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class SearchApiResponse {
    private SearchResponseData<ArtistResponseData> artists;
    private SearchResponseData<SearchTrackResponseData> tracks;

    public SearchResponseData<ArtistResponseData> getArtists() {
        return artists;
    }

    public void setArtists(SearchResponseData<ArtistResponseData> artists) {
        this.artists = artists;
    }

    public SearchResponseData<SearchTrackResponseData> getTracks() {
        return tracks;
    }

    public void setTracks(SearchResponseData<SearchTrackResponseData> tracks) {
        this.tracks = tracks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchApiResponse that = (SearchApiResponse) o;
        return Objects.equals(artists, that.artists) && Objects.equals(tracks, that.tracks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artists, tracks);
    }

    @Override
    public String toString() {
        return "SearchApiResponse{" +
                "artists=" + artists +
                ", tracks=" + tracks +
                '}';
    }
}
