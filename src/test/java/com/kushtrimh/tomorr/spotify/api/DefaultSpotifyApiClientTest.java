package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.configuration.TestSpotifyConfiguration;
import com.kushtrimh.tomorr.properties.SpotifyProperties;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistAlbumsApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistsApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistAlbumsApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistsApiResponse;
import com.kushtrimh.tomorr.spotify.http.SpotifyHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.kushtrimh.tomorr.spotify.SpotifyCacheKeys.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {TestSpotifyConfiguration.class})
public class DefaultSpotifyApiClientTest {

    @Mock
    private SpotifyHttpClient spotifyHttpClient;
    @Mock
    private StringRedisTemplate stringRedisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    private final SpotifyProperties spotifyProperties;

    private SpotifyApiClient client;

    private final String accessTokenValue = "access-token-value-tsg6523dstw5";

    @Autowired
    public DefaultSpotifyApiClientTest(SpotifyProperties spotifyProperties) {
        this.spotifyProperties = spotifyProperties;
    }

    @BeforeEach
    public void init() {
        client = new DefaultSpotifyApiClient(spotifyHttpClient, spotifyProperties, stringRedisTemplate);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void getMultipleArtists_WhenCalledWithRequest_ForwardRequestToHttpClient()
            throws TooManyRequestsException, SpotifyApiException {
        when(valueOperations.get(ACCESS_TOKEN_KEY)).thenReturn(accessTokenValue);
        GetArtistsApiRequest request = new GetArtistsApiRequest.Builder()
                .artists(List.of("artist1", "artist2")).build();
        client.getMultipleArtists(request);
        verify(spotifyHttpClient, times(1))
                .get(accessTokenValue, request, GetArtistsApiResponse.class);
    }

    @Test
    public void getArtistAlbums_WhenCalledWithRequest_ForwardRequestToHttpClient()
            throws TooManyRequestsException, SpotifyApiException {
        when(valueOperations.get(ACCESS_TOKEN_KEY)).thenReturn(accessTokenValue);
        GetArtistAlbumsApiRequest request = new GetArtistAlbumsApiRequest.Builder("artist1")
                .build();
        client.getArtistAlbums(request);
        verify(spotifyHttpClient, times(1))
                .get(accessTokenValue, request, GetArtistAlbumsApiResponse.class);
    }

    @Test
    public void getArtistAlbums_WhenCalledWithUrl_ForwardRequestToHttpClient()
            throws TooManyRequestsException, SpotifyApiException {
        when(valueOperations.get(ACCESS_TOKEN_KEY)).thenReturn(accessTokenValue);
        String url = "http://localhost/api/artists/artist1/albums";
        client.getArtistAlbums(url);
        verify(spotifyHttpClient, times(1))
                .get(accessTokenValue, url, GetArtistAlbumsApiResponse.class);
    }

    @Test
    public void refreshAccessToken_WhenCalled_GenerateNewAccessToken()
            throws SpotifyApiException {
        String newAccessToken = "newAccessToken-qwet2153wef4";
        TokenResponse response = new TokenResponse();
        response.setAccessToken(newAccessToken);
        when(spotifyHttpClient.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret()))
                .thenReturn(response);
        client.refreshAccessToken();
        verify(spotifyHttpClient, times(1))
                .getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret());
        verify(valueOperations, times(1))
                .set(ACCESS_TOKEN_KEY, response.getAccessToken());
    }
}
