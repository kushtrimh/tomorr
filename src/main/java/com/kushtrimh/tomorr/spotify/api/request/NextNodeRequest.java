package com.kushtrimh.tomorr.spotify.api.request;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Kushtrim Hajrizi
 */
public class NextNodeRequest<T> implements SpotifyApiRequest<T> {
    private final String uri;
    private final Class<T> responseClass;

    public NextNodeRequest(String uri, Class<T> responseClass) {
        this.uri = uri;
        this.responseClass = responseClass;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public Class<T> getResponseClass() {
        return responseClass;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public MultiValueMap<String, String> getQueryParams() {
        return new LinkedMultiValueMap<>();
    }
}
