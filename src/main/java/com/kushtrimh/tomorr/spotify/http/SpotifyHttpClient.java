package com.kushtrimh.tomorr.spotify.http;

import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;

/**
 * @author Kushtrim Hajrizi
 */
public interface SpotifyHttpClient {

    <Req extends SpotifyApiRequest, Res> Res get(String token, Req request, Class<Res> cls)
            throws SpotifyApiException, TooManyRequestsException;

    <Req extends SpotifyApiRequest, Res> Res post(String token, Req request, Class<Res> cls)
            throws SpotifyApiException, TooManyRequestsException;

    TokenResponse getToken(String clientId, String clientSecret) throws SpotifyApiException;
}
