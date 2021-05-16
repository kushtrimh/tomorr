package com.kushtrimh.tomorr.spotify.api.request.artist;

import com.kushtrimh.tomorr.spotify.api.request.ApiRequestType;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
public class GetArtistsApiRequest implements SpotifyApiRequest {

    private List<String> artists = new ArrayList<>();

    public GetArtistsApiRequest() {}

    public GetArtistsApiRequest(List<String> artists) {
        this.artists = artists;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    @Override
    public ApiRequestType getApiRequestType() {
        return ApiRequestType.ARTISTS;
    }

    @Override
    public MultiValueMap<String, String> getQueryParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ids", String.join(",", artists));
        return params;
    }

    @Override
    public String toString() {
        return "GetArtistsApiRequest{" +
                "artists=" + artists +
                '}';
    }
}
