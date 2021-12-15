package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.configuration.TestSpotifyConfiguration;
import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.limit.RequestLimitService;
import com.kushtrimh.tomorr.properties.SpotifyProperties;
import com.kushtrimh.tomorr.spotify.AuthorizationException;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.artist.SearchApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.SearchApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.SearchResponseData;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.ArtistResponseData;
import com.kushtrimh.tomorr.spotify.http.SpotifyHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;

import static com.kushtrimh.tomorr.spotify.SpotifyCacheKeys.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {TestSpotifyConfiguration.class})
public class DefaultSpotifyApiClientTest {

    @Mock
    private SpotifyHttpClient spotifyHttpClient;
    @Mock
    private RequestLimitService requestLimitService;
    @Mock
    private StringRedisTemplate stringRedisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;
    @Mock
    private SpotifyProperties spotifyProperties;

    private SpotifyApiClient client;

    private final String accessTokenValue = "access-token-value-tsg6523dstw5";

    @BeforeEach
    public void init() {
        client = new DefaultSpotifyApiClient(spotifyHttpClient, requestLimitService, spotifyProperties, stringRedisTemplate);
    }

    @Test
    public void get_WhenRequestIsNullInGetWithoutLimit_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> client.get(null));
    }

    @Test
    public void get_WhenRequestIsNullInGetWithLimit_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> client.get(LimitType.SPOTIFY_SYNC, null));
    }

    @Test
    public void get_WhenLimitTypeIsNullInGetWithLimit_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> client.get(null, new SearchApiRequest()));
    }

    @Test
    public void get_WhenCalledWithRequest_ForwardToHttpClient()
            throws TooManyRequestsException, SpotifyApiException, AuthorizationException {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(ACCESS_TOKEN_KEY)).thenReturn(accessTokenValue);
        var request = new SearchApiRequest.Builder("somequery")
                .limit(20)
                .build();
        client.get(request);
        verify(spotifyHttpClient, times(1)).execute(HttpMethod.GET, accessTokenValue, request, SearchApiResponse.class);
    }

    @Test
    public void get_WhenCalledWithRequestAndLimitIsNotExceeded_ForwardToHttpClient()
            throws TooManyRequestsException, SpotifyApiException, AuthorizationException {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(ACCESS_TOKEN_KEY)).thenReturn(accessTokenValue);
        when(requestLimitService.tryFor(LimitType.SPOTIFY_SEARCH)).thenReturn(true);
        var request = new SearchApiRequest.Builder("query")
                .limit(5)
                .build();
        client.get(LimitType.SPOTIFY_SEARCH, request);
        verify(spotifyHttpClient, times(1)).execute(HttpMethod.GET, accessTokenValue, request, SearchApiResponse.class);
    }

    @Test
    public void get_WhenCalledWithRequestAndLimitIsExceeded_ThrowTooManyRequestsException()
            throws TooManyRequestsException, SpotifyApiException, AuthorizationException {
        when(requestLimitService.tryFor(LimitType.SPOTIFY_SYNC)).thenReturn(false);
        var request = new SearchApiRequest.Builder("somequery")
                .limit(100)
                .build();
        assertThrows(TooManyRequestsException.class, () -> client.get(LimitType.SPOTIFY_SYNC, request));
        verify(spotifyHttpClient, never()).execute(HttpMethod.GET, accessTokenValue, request, SearchApiResponse.class);
    }

    @Test
    public void get_WhenTokenIsExpired_RefreshTokenAndRetry()
            throws TooManyRequestsException, AuthorizationException, SpotifyApiException {
        var invalidToken = "qwtq2315sdtg";
        var validToken = "qwetsdfhsdfhwery";
        var request = new SearchApiRequest.Builder("somequeryrequest")
                .limit(20)
                .build();
        var response = new SearchApiResponse();
        var artistResponseData = new SearchResponseData<ArtistResponseData>();
        artistResponseData.setLimit(20);
        artistResponseData.setTotal(200);
        response.setArtists(artistResponseData);
        var tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(validToken);

        when(requestLimitService.tryFor(LimitType.SPOTIFY_SYNC)).thenReturn(true);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(ACCESS_TOKEN_KEY)).thenReturn(invalidToken, validToken);
        when(spotifyHttpClient.execute(HttpMethod.GET, invalidToken, request, request.getResponseClass()))
                .thenThrow(AuthorizationException.class);
        when(spotifyHttpClient.execute(HttpMethod.GET, validToken, request, request.getResponseClass()))
                .thenReturn(response);
        when(spotifyHttpClient.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret()))
                .thenReturn(tokenResponse);
        var returnedResponse = client.get(LimitType.SPOTIFY_SYNC, request);
        assertEquals(response, returnedResponse);
        verify(valueOperations, times(1)).set(ACCESS_TOKEN_KEY, validToken);
    }

    @Test
    public void get_WhenTokenIsExpiredAndRetrievedOneFailsToo_ThrowSpotifyApiException()
            throws SpotifyApiException, TooManyRequestsException, AuthorizationException {
        var invalidToken1 = "qwetqwetqwgzsdga";
        var invalidToken2 = "qwetczsg21351235";
        var validToken = "qwetasdgwqetqwtg3";
        var tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(invalidToken2);
        var request = new SearchApiRequest.Builder("somequeryrequest")
                .limit(20)
                .build();

        when(requestLimitService.tryFor(LimitType.SPOTIFY_SYNC)).thenReturn(true);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(ACCESS_TOKEN_KEY)).thenReturn(invalidToken1, invalidToken2, validToken);
        when(spotifyHttpClient.execute(HttpMethod.GET, invalidToken1, request, request.getResponseClass()))
                .thenThrow(AuthorizationException.class);
        when(spotifyHttpClient.execute(HttpMethod.GET, invalidToken2, request, request.getResponseClass()))
                .thenThrow(AuthorizationException.class);
        when(spotifyHttpClient.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret()))
                .thenReturn(tokenResponse);
        assertThrows(SpotifyApiException.class, () -> client.get(LimitType.SPOTIFY_SYNC, request));
        verify(spotifyHttpClient, never()).execute(HttpMethod.GET, validToken, request, request.getResponseClass());
    }

    @Test
    public void refreshAccessToken_WhenCalled_GenerateNewAccessToken()
            throws SpotifyApiException {
        String newAccessToken = "newAccessToken-qwet2153wef4";
        TokenResponse response = new TokenResponse();
        response.setAccessToken(newAccessToken);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(spotifyHttpClient.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret()))
                .thenReturn(response);
        client.refreshAccessToken();
        verify(spotifyHttpClient, times(1))
                .getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret());
        verify(valueOperations, times(1))
                .set(ACCESS_TOKEN_KEY, response.getAccessToken());
    }
}
