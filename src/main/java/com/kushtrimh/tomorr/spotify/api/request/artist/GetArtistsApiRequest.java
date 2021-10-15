package com.kushtrimh.tomorr.spotify.api.request.artist;

import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistsApiResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
public class GetArtistsApiRequest implements SpotifyApiRequest<GetArtistsApiResponse> {
    private List<String> artists = new ArrayList<>();

    public GetArtistsApiRequest() {
    }

    private GetArtistsApiRequest(Builder builder) {
        this.artists = builder.artists;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    @Override
    public String getPath() {
        return "/artists";
    }

    @Override
    public MultiValueMap<String, String> getQueryParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ids", String.join(",", artists));
        return params;
    }

    @Override
    public Class<GetArtistsApiResponse> getResponseClass() {
        return GetArtistsApiResponse.class;
    }

    @Override
    public String toString() {
        return "GetArtistsApiRequest{" +
                "artists=" + artists +
                '}';
    }

    public static class Builder {
        private List<String> artists = new ArrayList<>();

        public Builder artists(List<String> artists) {
            this.artists = artists;
            return this;
        }

        public GetArtistsApiRequest build() {
            return new GetArtistsApiRequest(this);
        }
    }
}
