package com.kushtrimh.tomorr.spotify.api.request;

import org.springframework.util.MultiValueMap;

/**
 * @author Kushtrim Hajrizi
 */
public interface SpotifyApiRequest {

    String getPath();

    MultiValueMap<String, String> getQueryParams();
}
