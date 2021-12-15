package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.limit.RequestLimitService;
import com.kushtrimh.tomorr.properties.SpotifyProperties;
import com.kushtrimh.tomorr.spotify.AuthorizationException;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.SpotifyCacheKeys;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import com.kushtrimh.tomorr.spotify.http.SpotifyHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class DefaultSpotifyApiClient implements SpotifyApiClient {

    private final Logger logger = LoggerFactory.getLogger(DefaultSpotifyApiClient.class);

    private final SpotifyHttpClient httpClient;
    private final RequestLimitService requestLimitService;
    private final SpotifyProperties spotifyProperties;
    private final StringRedisTemplate stringRedisTemplate;

    public DefaultSpotifyApiClient(
            SpotifyHttpClient httpClient,
            RequestLimitService requestLimitService,
            SpotifyProperties spotifyProperties,
            StringRedisTemplate stringRedisTemplate) {
        this.httpClient = httpClient;
        this.requestLimitService = requestLimitService;
        this.spotifyProperties = spotifyProperties;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public <Req extends SpotifyApiRequest<Res>, Res> Res get(Req request)
            throws TooManyRequestsException, SpotifyApiException {
        Objects.requireNonNull(request);
        return execute(HttpMethod.GET, request);
    }

    @Override
    public <Req extends SpotifyApiRequest<Res>, Res> Res get(LimitType limitType, Req request)
            throws TooManyRequestsException, SpotifyApiException {
        Objects.requireNonNull(limitType);
        Objects.requireNonNull(request);
        checkLimit(limitType);
        return execute(HttpMethod.GET, request);
    }

    private <Req extends SpotifyApiRequest<Res>, Res> Res execute(HttpMethod httpMethod, Req request)
            throws TooManyRequestsException, SpotifyApiException {
        var count = 0;
        while (true) {
            try {
                return httpClient.execute(httpMethod, getAccessToken(), request, request.getResponseClass());
            } catch (AuthorizationException e) {
                // Only 1 retry if the request fails the first time
                if (++count > 1) {
                    throw new SpotifyApiException(e);
                }
                refreshAccessToken();
                logger.info("Request failed because of authorization, token refreshed", e);
            }
        }
    }

    @Override
    public TokenResponse refreshAccessToken() throws SpotifyApiException {
        TokenResponse response = httpClient.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret());
        stringRedisTemplate.opsForValue().set(SpotifyCacheKeys.ACCESS_TOKEN_KEY, response.getAccessToken());
        return response;
    }

    private String getAccessToken() {
        return stringRedisTemplate.opsForValue().get(SpotifyCacheKeys.ACCESS_TOKEN_KEY);
    }

    private void checkLimit(LimitType limitType) throws TooManyRequestsException {
        if (!requestLimitService.tryFor(limitType)) {
            throw new TooManyRequestsException("Reached limit for requests");
        }
    }
}
