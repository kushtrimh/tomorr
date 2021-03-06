package com.kushtrimh.tomorr.artist.service;

import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.cache.ArtistCache;
import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.SpotifyApiClient;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.SearchApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.ImageResponseData;
import com.kushtrimh.tomorr.spotify.api.response.SearchApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.SearchResponseData;
import com.kushtrimh.tomorr.spotify.api.response.artist.ArtistResponseData;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistApiResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
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
    private ArtistService artistService;
    @Mock
    private SpotifyApiClient spotifyApiClient;

    private DefaultArtistSearchService defaultArtistSearchService;

    @BeforeEach
    public void init() {
        defaultArtistSearchService = new DefaultArtistSearchService(artistCache, artistService, spotifyApiClient);
    }

    @Test
    public void exists_WhenApiCallThrowsTooManyRequestsException_ReturnFalse()
            throws TooManyRequestsException, SpotifyApiException {
        assertThatThrowsExceptionOnExists(TooManyRequestsException.class);
    }

    @Test
    public void exists_WhenApiCallThrowsSpotifyApiException_ReturnFalse()
            throws TooManyRequestsException, SpotifyApiException {
        assertThatThrowsExceptionOnExists(SpotifyApiException.class);
    }

    @Test
    public void exists_WhenApiCallReturnArtist_ReturnTrueAndCacheArtistId()
            throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var request = new GetArtistApiRequest.Builder(artistId).build();
        GetArtistApiResponse response = createGetArtistApiResponse(artistId, "artist-name");

        when(spotifyApiClient.get(LimitType.SPOTIFY_SEARCH, request)).thenReturn(response);
        assertTrue(defaultArtistSearchService.exists(artistId));
        verify(artistCache, times(1)).putArtistIds(List.of("artist1"));
    }

    @Test
    public void get_WhenApiCallThrowsTooManyRequestsException_ReturnEmptyOptional()
            throws TooManyRequestsException, SpotifyApiException {
        assertThatThrowsExceptionOnGet(TooManyRequestsException.class);
    }

    @Test
    public void get_WhenApiCallThrowsSpotifyApiException_ReturnEmptyOptional()
            throws TooManyRequestsException, SpotifyApiException {
        assertThatThrowsExceptionOnGet(SpotifyApiException.class);
    }

    @Test
    public void get_WhenApiCallReturnArtist_ReturnArtistAndCacheArtistId()
            throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var expectedArtist = new Artist(artistId, "artist-name", null, 0);

        var request = new GetArtistApiRequest.Builder(artistId).build();
        GetArtistApiResponse response = createGetArtistApiResponse(artistId, "artist-name");

        when(spotifyApiClient.get(LimitType.SPOTIFY_SEARCH, request)).thenReturn(response);
        var returnedArtist = defaultArtistSearchService.get(artistId).get();
        assertEquals(expectedArtist, returnedArtist);
        verify(artistCache, times(1)).putArtistIds(List.of("artist1"));
    }

    @Test
    public void search_WhenSearchingWithNameAsNull_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> defaultArtistSearchService.search(null, false));
    }

    @Test
    public void search_WhenSearchingWithNameAndWithoutExternal_ReturnArtists() throws TooManyRequestsException, SpotifyApiException {
        String name = "artist1";
        List<Artist> artists = List.of(new Artist("artist1", "Artist One", "image-1", 50),
                new Artist("artist11", "Artist Eleven", "image-11", 83));
        when(artistService.searchByName(name)).thenReturn(artists);
        List<Artist> returnedArtists = defaultArtistSearchService.search(name, false);
        assertIterableEquals(artists, returnedArtists);
        verify(spotifyApiClient, never()).get(eq(LimitType.SPOTIFY_SEARCH), any(SearchApiRequest.class));
    }

    @Test
    public void search_WhenSearchingWithNameAndExternalAndExceptionIsThrown_ReturnArtistsFromServiceOnly()
            throws TooManyRequestsException, SpotifyApiException {
        String name = "artist1";
        List<Artist> artists = List.of(new Artist("artist1", "Artist One", "image-1", 50),
                new Artist("artist11", "Artist Eleven", "image-11", 83));
        when(artistService.searchByName(name)).thenReturn(artists);
        SearchApiRequest request = new SearchApiRequest.Builder(name)
                .limit(50)
                .offset(0)
                .types(List.of("artist"))
                .build();
        when(spotifyApiClient.get(LimitType.SPOTIFY_SEARCH, request))
                .thenThrow(SpotifyApiException.class);
        List<Artist> returnedArtists = defaultArtistSearchService.search(name, true);
        Assertions.assertThat(artists).hasSameElementsAs(returnedArtists);
    }

    @Test
    public void search_WhenSearchingWithNameAndExternal_ReturnArtistsFromServiceAndExternalQuery()
            throws TooManyRequestsException, SpotifyApiException {
        String name = "artist1";
        List<Artist> artists = List.of(new Artist("artist1", "Artist One", "image-1", 50),
                new Artist("artist11", "Artist Eleven", "image-11", 83));
        when(artistService.searchByName(name)).thenReturn(artists);
        SearchApiRequest request = new SearchApiRequest.Builder(name)
                .limit(50)
                .offset(0)
                .types(List.of("artist"))
                .build();
        SearchApiResponse response = new SearchApiResponse();
        SearchResponseData<ArtistResponseData> searchResponseData = new SearchResponseData<>();
        List<ArtistResponseData> artistsResponseData = List.of(
                new ArtistResponseData("artist1", "Artist One", List.of(new ImageResponseData("image-1")), 50),
                new ArtistResponseData("artist10", "Artist Ten", List.of(new ImageResponseData("image-10")), 59),
                new ArtistResponseData("artist12", "Artist Twelve", List.of(new ImageResponseData("image-12")), 75)
        );
        searchResponseData.setItems(artistsResponseData);
        response.setArtists(searchResponseData);
        when(spotifyApiClient.get(LimitType.SPOTIFY_SEARCH, request)).thenReturn(response);
        List<Artist> returnedArtists = defaultArtistSearchService.search(name, true);

        List<Artist> expectedArtists = new ArrayList<>(artists);
        expectedArtists.add(new Artist("artist10", "Artist Ten", "image-10", 59));
        expectedArtists.add(new Artist("artist12", "Artist Twelve", "image-12", 75));
        Assertions.assertThat(expectedArtists).hasSameElementsAs(returnedArtists);
    }

    private <T extends Exception> void assertThatThrowsExceptionOnExists(Class<T> exceptionCls)
            throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var request = new GetArtistApiRequest.Builder(artistId).build();
        when(spotifyApiClient.get(LimitType.SPOTIFY_SEARCH, request)).thenThrow(exceptionCls);
        assertFalse(defaultArtistSearchService.exists(artistId));
        verify(artistCache, never()).putArtistIds(any());
    }

    private <T extends Exception> void assertThatThrowsExceptionOnGet(Class<T> exceptionCls)
            throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var request = new GetArtistApiRequest.Builder(artistId).build();
        when(spotifyApiClient.get(LimitType.SPOTIFY_SEARCH, request)).thenThrow(exceptionCls);
        assertTrue(defaultArtistSearchService.get(artistId).isEmpty());
        verify(artistCache, never()).putArtistIds(any());
    }


    private GetArtistApiResponse createGetArtistApiResponse(String id, String name) {
        var response = new GetArtistApiResponse();
        var artistResponseData = new ArtistResponseData();
        artistResponseData.setId(id);
        artistResponseData.setName(name);
        artistResponseData.setImages(Collections.emptyList());
        response.setArtistResponseData(artistResponseData);
        return response;
    }
}