package com.kushtrimh.tomorr.spotify.api.response.artist;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class GetArtistApiResponse {
    @JsonUnwrapped
    private ArtistResponseData artistResponseData;

    public ArtistResponseData getArtistResponseData() {
        return artistResponseData;
    }

    public void setArtistResponseData(ArtistResponseData artistResponseData) {
        this.artistResponseData = artistResponseData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetArtistApiResponse that = (GetArtistApiResponse) o;
        return Objects.equals(artistResponseData, that.artistResponseData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistResponseData);
    }

    @Override
    public String toString() {
        return "GetArtistApiResponse{" +
                "artistResponseData=" + artistResponseData +
                '}';
    }
}
