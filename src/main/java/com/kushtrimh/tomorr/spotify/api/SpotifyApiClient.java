package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.spotify.http.SpotifyHttpClient;

/**
 * @author Kushtrim Hajrizi
 */
public class SpotifyApiClient {

    private final SpotifyHttpClient httpClient;

    public SpotifyApiClient(SpotifyHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
