package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.limit.RequestLimitService;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistAlbumsApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistsApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.SearchApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.SearchApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.ArtistResponseData;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistAlbumsApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistsApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({MockitoExtension.class})
public class LimitAwareSpotifyApiClientTest {

    @Mock
    private SpotifyApiClient baseClient;
    @Mock
    private RequestLimitService requestLimitService;

    private LimitAwareSpotifyApiClient limitAwareSpotifyApiClient;

    @BeforeEach
    public void init() {
        limitAwareSpotifyApiClient = new LimitAwareSpotifyApiClient(baseClient, requestLimitService);
    }

    @Test
    public void getMultipleArtists_WhenLimitIsNotExceeded_CallBaseClient()
            throws TooManyRequestsException, SpotifyApiException {
        var request = new GetArtistsApiRequest.Builder()
                .artists(List.of("artist1", "artist2")).build();
        var response = new GetArtistsApiResponse();
        ArtistResponseData artistResponseData = new ArtistResponseData();
        artistResponseData.setId("artist1");
        response.setArtists(List.of(artistResponseData));

        when(requestLimitService.tryFor(LimitType.SPOTIFY_EXTERNAL)).thenReturn(true);
        ;
        when(baseClient.getMultipleArtists(request)).thenReturn(response);

        GetArtistsApiResponse returnedResponse = limitAwareSpotifyApiClient.getMultipleArtists(request);
        assertEquals(response, returnedResponse);
        verify(baseClient, times(1)).getMultipleArtists(request);
    }

    @Test
    public void getMultipleArtists_WhenLimitIsExceeded_ThrowTooManyRequestsException() {
        when(requestLimitService.tryFor(LimitType.SPOTIFY_EXTERNAL)).thenReturn(false);
        ;
        assertThrows(TooManyRequestsException.class,
                () -> limitAwareSpotifyApiClient.getMultipleArtists(new GetArtistsApiRequest()));
    }

    @Test
    public void getArtistAlbums_WhenLimitIsNotExceeded_CallBaseClient()
            throws TooManyRequestsException, SpotifyApiException {
        var request = new GetArtistAlbumsApiRequest.Builder("artist1")
                .limit(50)
                .build();
        var response = new GetArtistAlbumsApiResponse();
        response.setTotal(100);

        when(requestLimitService.tryFor(LimitType.SPOTIFY_EXTERNAL)).thenReturn(true);
        ;
        when(baseClient.getArtistAlbums(request)).thenReturn(response);

        GetArtistAlbumsApiResponse returnedResponse = limitAwareSpotifyApiClient.getArtistAlbums(request);
        assertEquals(response, returnedResponse);
        verify(baseClient, times(1)).getArtistAlbums(request);
    }

    @Test
    public void getArtistsAlbums_WhenLimitIsExceeded_ThrowTooManyRequestsException() {
        when(requestLimitService.tryFor(LimitType.SPOTIFY_EXTERNAL)).thenReturn(false);
        ;
        assertThrows(TooManyRequestsException.class,
                () -> limitAwareSpotifyApiClient.getArtistAlbums(new GetArtistAlbumsApiRequest()));
    }

    @Test
    public void getArtistAlbums_WhenLimitIsNotExceeded_CallBaseClientWithUrl()
            throws TooManyRequestsException, SpotifyApiException {
        String url = "http://localhost/api/artists/artist1/albums";
        var response = new GetArtistAlbumsApiResponse();
        response.setTotal(100);

        when(requestLimitService.tryFor(LimitType.SPOTIFY_EXTERNAL)).thenReturn(true);
        ;
        when(baseClient.getArtistAlbums(url)).thenReturn(response);

        GetArtistAlbumsApiResponse returnedResponse = limitAwareSpotifyApiClient.getArtistAlbums(url);
        assertEquals(response, returnedResponse);
        verify(baseClient, times(1)).getArtistAlbums(url);
    }

    @Test
    public void getArtist_WhenLimitIsExceeded_CallBaseClientWithRequest() {
        when(requestLimitService.tryFor(LimitType.SPOTIFY_EXTERNAL)).thenReturn(false);
        ;
        assertThrows(TooManyRequestsException.class,
                () -> limitAwareSpotifyApiClient.getArtist(new GetArtistApiRequest()));
    }

    @Test
    public void getArtist_WhenLimitIsNotExceeded_CallBaseClientWithRequest()
            throws TooManyRequestsException, SpotifyApiException {
        var request = new GetArtistApiRequest.Builder("artist1")
                .build();
        var response = new GetArtistApiResponse();
        var artistResponseData = new ArtistResponseData();
        artistResponseData.setId("artist1");
        artistResponseData.setName("Artist One");
        artistResponseData.setPopularity(15);
        response.setArtistResponseData(artistResponseData);

        when(requestLimitService.tryFor(LimitType.SPOTIFY_EXTERNAL)).thenReturn(true);
        ;
        when(baseClient.getArtist(request)).thenReturn(response);

        GetArtistApiResponse returnedResponse = limitAwareSpotifyApiClient.getArtist(request);
        assertEquals(response, returnedResponse);
        verify(baseClient, times(1)).getArtist(request);
    }

    @Test
    public void search_WhenLimitIsExceeded_CallBaseClientWithRequest() {
        when(requestLimitService.tryFor(LimitType.SPOTIFY_EXTERNAL)).thenReturn(false);
        ;
        assertThrows(TooManyRequestsException.class,
                () -> limitAwareSpotifyApiClient.search(new SearchApiRequest()));
    }

    @Test
    public void search_WhenLimitIsNotExceeded_CallBaseClientWithRequest()
            throws TooManyRequestsException, SpotifyApiException {
        var request = new SearchApiRequest.Builder("art")
                .build();
        var response = new SearchApiResponse();

        when(requestLimitService.tryFor(LimitType.SPOTIFY_EXTERNAL)).thenReturn(true);
        ;
        when(baseClient.search(request)).thenReturn(response);

        SearchApiResponse returnedResponse = limitAwareSpotifyApiClient.search(request);
        assertEquals(response, returnedResponse);
        verify(baseClient, times(1)).search(request);
    }

    @Test
    public void getArtistAlbums_WhenLimitIsExceeded_ThrowTooManyRequestsException() {
        when(requestLimitService.tryFor(LimitType.SPOTIFY_EXTERNAL)).thenReturn(false);
        ;
        var url = "http://localhost/api/artists/artist1/albums";
        assertThrows(TooManyRequestsException.class,
                () -> limitAwareSpotifyApiClient.getArtistAlbums(url));
    }

    @Test
    public void refreshAccessToken_WhenCalled_CallBaseClient() throws SpotifyApiException {
        limitAwareSpotifyApiClient.refreshAccessToken();
        verify(baseClient, times(1)).refreshAccessToken();
    }
}