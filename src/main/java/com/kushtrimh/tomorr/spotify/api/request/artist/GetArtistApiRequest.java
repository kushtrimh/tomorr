package com.kushtrimh.tomorr.spotify.api.request.artist;

import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistApiResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class GetArtistApiRequest implements SpotifyApiRequest<GetArtistApiResponse> {
    private String id;

    public GetArtistApiRequest() {
    }

    public GetArtistApiRequest(Builder builder) {
        this.id = builder.id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/artists/" + id;
    }

    @Override
    public MultiValueMap<String, String> getQueryParams() {
        return new LinkedMultiValueMap<>();
    }

    @Override
    public Class<GetArtistApiResponse> getResponseClass() {
        return GetArtistApiResponse.class;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetArtistApiRequest that = (GetArtistApiRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GetArtistApiRequest{" +
                "id='" + id + '\'' +
                '}';
    }

    public static class Builder {
        private final String id;

        public Builder(String id) {
            this.id = id;
        }

        public GetArtistApiRequest build() {
            return new GetArtistApiRequest(this);
        }
    }
}
