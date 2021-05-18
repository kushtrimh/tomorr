package com.kushtrimh.tomorr.spotify.util;

import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class SpotifyApiUriBuilder {

    private final String apiUri;

    public SpotifyApiUriBuilder(String apiUri) {
        this.apiUri = apiUri;
    }

    public <Req extends SpotifyApiRequest> String getApiUri(Req request) {
        Objects.requireNonNull(request);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .uri(URI.create(apiUri))
                .path(request.getPath())
                .queryParams(request.getQueryParams());
        return uriBuilder.build().toUriString();
    }
}
