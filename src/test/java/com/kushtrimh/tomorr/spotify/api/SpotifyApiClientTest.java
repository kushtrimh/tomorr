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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestSpotifyConfiguration.class)
public class SpotifyApiClientTest {

    @Mock
    private SpotifyHttpClient spotifyHttpClient;

    private final SpotifyProperties spotifyProperties;

    private SpotifyApiClient client;

    @Autowired
    public SpotifyApiClientTest(SpotifyProperties spotifyProperties) {
        this.spotifyProperties = spotifyProperties;
    }

    @BeforeEach
    public void init() {
        client = new SpotifyApiClient(spotifyHttpClient, spotifyProperties);
    }

    @Test
    public void getMultipleArtists_WhenCalledWithRequest_ForwardRequestToHttpClient()
            throws TooManyRequestsException, SpotifyApiException {
        GetArtistsApiRequest request = new GetArtistsApiRequest.Builder()
                .artists(List.of("artist1", "artist2")).build();
        client.getMultipleArtists(request);
        verify(spotifyHttpClient, times(1))
                .get(any(String.class), eq(request), eq(GetArtistsApiResponse.class));
    }

    @Test
    public void getArtistAlbums_WhenCalledWithRequest_ForwardRequestToHttpClient()
            throws TooManyRequestsException, SpotifyApiException {
        GetArtistAlbumsApiRequest request = new GetArtistAlbumsApiRequest.Builder("artist1")
                .build();
        client.getArtistAlbums(request);
        verify(spotifyHttpClient, times(1))
                .get(any(String.class), eq(request), eq(GetArtistAlbumsApiResponse.class));
    }

    @Test
    public void getArtistAlbums_WhenCalledWithUrl_ForwardRequestToHttpClient()
            throws TooManyRequestsException, SpotifyApiException {
        String url = "http://localhost/api/artists/artist1/albums";
        client.getArtistAlbums(url);
        verify(spotifyHttpClient, times(1))
                .get(any(String.class), eq(url), eq(GetArtistAlbumsApiResponse.class));
    }

    @Test
    public void refreshAccessToken_WhenCalled_GenerateNewAccessToken()
            throws SpotifyApiException {
        when(spotifyHttpClient.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret()))
                .thenReturn(new TokenResponse());
        client.refreshAccessToken();
        verify(spotifyHttpClient, times(1))
                .getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret());
    }
}
