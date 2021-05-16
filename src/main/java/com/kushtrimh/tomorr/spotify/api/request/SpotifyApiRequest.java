package com.kushtrimh.tomorr.spotify.api.request;

import org.springframework.util.MultiValueMap;

/**
 * @author Kushtrim Hajrizi
 */
public interface SpotifyApiRequest {

    ApiRequestType getApiRequestType();

    MultiValueMap<String, String> getQueryParams();
}
