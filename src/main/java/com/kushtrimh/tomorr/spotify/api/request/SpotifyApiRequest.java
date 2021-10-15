package com.kushtrimh.tomorr.spotify.api.request;

import org.springframework.util.MultiValueMap;

/**
 * @author Kushtrim Hajrizi
 */
public interface SpotifyApiRequest<T> {

    Class<T> getResponseClass();

    String getPath();

    MultiValueMap<String, String> getQueryParams();
}
