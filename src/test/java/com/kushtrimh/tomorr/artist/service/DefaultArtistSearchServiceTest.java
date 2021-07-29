package com.kushtrimh.tomorr.artist.service;

import com.kushtrimh.tomorr.artist.cache.ArtistCache;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.SpotifyApiClient;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.artist.ArtistResponseData;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistApiResponse;
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
class DefaultArtistSearchServiceTest {

    @Mock
    private ArtistCache artistCache;
    @Mock
    private SpotifyApiClient spotifyApiClient;

    private DefaultArtistSearchService defaultArtistSearchService;

    @BeforeEach
    public void init() {
        defaultArtistSearchService = new DefaultArtistSearchService(artistCache, spotifyApiClient);
    }

    @Test
    public void exists_WhenApiCallThrowsTooManyRequestsException_ReturnFalse()
            throws TooManyRequestsException, SpotifyApiException {
        assertThatThrowsException(TooManyRequestsException.class);
    }

    @Test
    public void exists_WhenApiCallThrowsSpotifyApiException_ReturnFalse()
            throws TooManyRequestsException, SpotifyApiException {
        assertThatThrowsException(SpotifyApiException.class);
    }

    @Test
    public void exists_WhenApiCallReturnArtist_ReturnTrueAndCacheArtistId()
            throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var request = new GetArtistApiRequest.Builder(artistId).build();
        var response = new GetArtistApiResponse();
        var artistResponseData = new ArtistResponseData();
        artistResponseData.setId("artist1");
        response.setArtistResponseData(artistResponseData);

        when(spotifyApiClient.getArtist(request)).thenReturn(response);
        assertTrue(defaultArtistSearchService.exists(artistId));
        verify(artistCache, times(1)).putArtistIds(List.of("artist1"));
    }

    private <T extends Exception> void assertThatThrowsException(Class<T> exceptionCls)
            throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var request = new GetArtistApiRequest.Builder(artistId).build();
        when(spotifyApiClient.getArtist(request)).thenThrow(exceptionCls);
        assertFalse(defaultArtistSearchService.exists(artistId));
        verify(artistCache, never()).putArtistIds(any());
    }
}