package com.kushtrimh.tomorr.spotify.http;

import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.SpotifyApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;

/**
 * @author Kushtrim Hajrizi
 */
public interface SpotifyHttpClient {

    <Res extends SpotifyApiResponse> Res get(String token, String path, Class<Res> cls)
            throws SpotifyApiException, TooManyRequestsException;

    <Req extends SpotifyApiRequest, Res extends SpotifyApiResponse> Res post(
            String token, String path, Req body, Class<Res> cls) throws SpotifyApiException, TooManyRequestsException;

    TokenResponse getToken(String clientId, String clientSecret) throws SpotifyApiException;
}
