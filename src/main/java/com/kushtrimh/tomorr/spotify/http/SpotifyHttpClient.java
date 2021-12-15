package com.kushtrimh.tomorr.spotify.http;

import com.kushtrimh.tomorr.spotify.AuthorizationException;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import org.springframework.http.HttpMethod;

/**
 * @author Kushtrim Hajrizi
 */
public interface SpotifyHttpClient {

    <Res> Res execute(HttpMethod httpMethod, String token, String url, Class<Res> cls)
            throws SpotifyApiException, TooManyRequestsException, AuthorizationException;

    <Req extends SpotifyApiRequest<Res>, Res> Res execute(HttpMethod httpMethod, String token, Req request, Class<Res> cls)
            throws SpotifyApiException, TooManyRequestsException, AuthorizationException;

    TokenResponse getToken(String clientId, String clientSecret) throws SpotifyApiException;
}
