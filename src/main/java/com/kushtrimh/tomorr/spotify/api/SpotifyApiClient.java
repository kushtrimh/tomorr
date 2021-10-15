package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;

/**
 * @author Kushtrim Hajrizi
 */
public interface SpotifyApiClient {

    <Req extends SpotifyApiRequest<Res>, Res> Res get(Req request)
            throws TooManyRequestsException, SpotifyApiException;

    <Req extends SpotifyApiRequest<Res>, Res> Res get(LimitType limitType, Req request)
            throws TooManyRequestsException, SpotifyApiException;

    TokenResponse refreshAccessToken() throws SpotifyApiException;
}
