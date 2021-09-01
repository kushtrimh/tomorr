package com.kushtrimh.tomorr.spotify.http;

import com.kushtrimh.tomorr.configuration.TestSpotifyConfiguration;
import com.kushtrimh.tomorr.properties.SpotifyProperties;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistAlbumsApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistsApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.SearchApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.SearchApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.SearchResponseData;
import com.kushtrimh.tomorr.spotify.api.response.SearchTrackResponseData;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import com.kushtrimh.tomorr.spotify.api.response.album.AlbumResponseData;
import com.kushtrimh.tomorr.spotify.api.response.artist.ArtistResponseData;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistAlbumsApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistsApiResponse;
import com.kushtrimh.tomorr.spotify.util.SpotifyApiUriBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

/**
 * @author Kushtrim Hajrizi
 */
@ContextConfiguration(classes = TestSpotifyConfiguration.class)
@ExtendWith(SpringExtension.class)
public class DefaultSpotifyHttpClientTest {

    private final RestTemplate restTemplate;
    private final SpotifyProperties spotifyProperties;
    private final SpotifyApiUriBuilder spotifyApiUriBuilder;

    private final MockRestServiceServer server;
    private SpotifyHttpClient client;

    @Autowired
    public DefaultSpotifyHttpClientTest(RestTemplate restTemplate,
                                        SpotifyProperties spotifyProperties,
                                        SpotifyApiUriBuilder spotifyApiUriBuilder) {
        this.restTemplate = restTemplate;
        this.spotifyProperties = spotifyProperties;
        this.spotifyApiUriBuilder = spotifyApiUriBuilder;

        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @BeforeEach
    public void init() {
        client = new DefaultSpotifyHttpClient.Builder(spotifyApiUriBuilder, spotifyProperties.getAuthUrl())
                .restTemplate(restTemplate)
                .userAgent(spotifyProperties.getUserAgent())
                .build();
    }

    // General tests for the client

    @Test
    public void get_WhenAccessTokenIsNull_ThrowException() {
        assertThrows(NullPointerException.class,
                () -> client.get(null, new GetArtistsApiRequest.Builder().build(), GetArtistsApiResponse.class));
    }

    @Test
    public void get_WhenRequestIsNull_ThrowException() {
        assertThrows(NullPointerException.class,
                () -> client.get("token", (SpotifyApiRequest) null, GetArtistsApiResponse.class));
    }

    @Test
    public void get_WhenUrlIsNull_ThrowException() {
        assertThrows(NullPointerException.class,
                () -> client.get("token", (String) null, GetArtistsApiResponse.class));
    }

    @Test
    public void get_WhenResponseClassIsNull_ThrowException() {
        assertThrows(NullPointerException.class,
                () -> client.get("token", "http://localhost/api/artists", null));
    }

    @Test
    public void post_WhenAccessTokenIsNull_ThrowException() {
        assertThrows(NullPointerException.class,
                () -> client.post(null, new GetArtistsApiRequest.Builder().build(), GetArtistsApiResponse.class));
    }

    @Test
    public void post_WhenRequestIsNull_ThrowException() {
        assertThrows(NullPointerException.class,
                () -> client.post("token", null, GetArtistsApiResponse.class));
    }


    @Test
    public void post_WhenResponseClassIsNull_ThrowException() {
        assertThrows(NullPointerException.class,
                () -> client.post("token", new GetArtistsApiRequest.Builder().build(), null));
    }

    @Test
    public void get_WhenRequestSentSuccessfully_RequestHeadersSentCorrectly()
            throws TooManyRequestsException, SpotifyApiException {
        var token = "token";
        var request = new GetArtistsApiRequest.Builder().artists(List.of("artist1", "artist2")).build();
        server.expect(requestTo(getApiUrl("/artists?ids=artist1,artist2")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.USER_AGENT, spotifyProperties.getUserAgent()))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(header(DefaultSpotifyHttpClient.TOMORR_TRACE_ID_HEADER, any(String.class)))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andRespond(withSuccess());
        client.get(token, request, GetArtistsApiResponse.class);
        server.verify();
    }

    @Test
    public void get_WhenRestClientExceptionThrown_ThrowSpotifyApiException() {
        String url = getApiUrl("/artists");
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());
        assertThrows(SpotifyApiException.class, () ->
                client.get("token", url, GetArtistsApiResponse.class));
    }

    @Test
    public void get_WhenTooManyRequestsResponseCodeReturned_ThrowTooManyRequestsExceptionWithDefaultTime() {
        TooManyRequestsException ex = sendRequestForTooManyRequestsException(new HttpHeaders());
        assertEquals(ex.getRetryAfter(), 60);
    }

    @Test
    public void get_WhenTooManyRequestsResponseCodeReturned_ThrowTooManyRequestsExceptionWithTimeFromHeaders() {
        var responseHeaders = new HttpHeaders();
        responseHeaders.add("Retry-After", "30");
        TooManyRequestsException ex = sendRequestForTooManyRequestsException(responseHeaders);
        assertEquals(ex.getRetryAfter(), 30);
    }

    // Request specific tests

    // GetArtistsApiRequest tests

    @Test
    public void get_WhenGetArtistsApiRequestSentSuccessfully_ReturnResponse()
            throws IOException, TooManyRequestsException, SpotifyApiException {
        var request = new GetArtistsApiRequest.Builder().artists(List.of("artist1", "artist2")).build();
        assertSuccessfulGetArtistsApiRequest(request, null);
    }

    @Test
    public void get_WhenGetArtistsApiRequestSentSuccessfullyWithUrl_ReturnResponse()
            throws IOException, TooManyRequestsException, SpotifyApiException {
        var url = getApiUrl("/artists?ids=artist1,artist2");
        assertSuccessfulGetArtistsApiRequest(null, url);
    }

    private void assertSuccessfulGetArtistsApiRequest(GetArtistsApiRequest request, String url)
            throws TooManyRequestsException, SpotifyApiException, IOException {
        var token = "token";
        setupServerExpectation("spotifyapi/get-artists-response.json", "/artists?ids=artist1,artist2");

        GetArtistsApiResponse response = request == null
                ? client.get(token, url, GetArtistsApiResponse.class)
                : client.get(token, request, GetArtistsApiResponse.class);

        server.verify();
        List<ArtistResponseData> artists = response.getArtists();
        assertFalse(artists.isEmpty());
        assertEquals(2, artists.size());
        ArtistResponseData artist1 = artists.stream().filter(artist -> "artist1".equals(artist.getId()))
                .findFirst().orElse(null);
        assertNotNull(artist1);
        assertEquals("artist1", artist1.getId());
        assertEquals("David Bowie", artist1.getName());
        assertEquals(77, artist1.getPopularity());
        assertEquals(4, artist1.getImages().size());
    }

    // GetArtistAlbumsApiRequest tests

    @Test
    public void get_GetArtistAlbumsApiRequestSentSuccessfully_ReturnResponse()
            throws TooManyRequestsException, SpotifyApiException, IOException {
        var request = new GetArtistAlbumsApiRequest.Builder("artist1")
                .build();
        assertSuccessfulGetArtistAlbumsApiRequest(request, null);
    }

    @Test
    public void get_GetArtistAlbumsApiRequestSentSuccessfullyWithUrl_ReturnResponse()
            throws TooManyRequestsException, SpotifyApiException, IOException {
        var url = getApiUrl("/artists/artist1/albums?include_groups=album,single&limit=50&offset=0");
        assertSuccessfulGetArtistAlbumsApiRequest(null, url);
    }

    private void assertSuccessfulGetArtistAlbumsApiRequest(GetArtistAlbumsApiRequest request, String url)
            throws IOException, TooManyRequestsException, SpotifyApiException {
        setupServerExpectation("spotifyapi/get-artists-albums-response.json", "/artists/artist1/albums?include_groups=album,single&limit=50&offset=0");

        GetArtistAlbumsApiResponse response = request == null
                ? client.get("token", url, GetArtistAlbumsApiResponse.class)
                : client.get("token", request, GetArtistAlbumsApiResponse.class);

        server.verify();
        List<AlbumResponseData> albums = response.getItems();
        assertFalse(albums.isEmpty());
        assertEquals(2, albums.size());
        assertEquals(2, response.getLimit());
        assertEquals(
                "https://api.spotify.com/v1/artists/1vCWHaC5f2uS3yhpwWbIA6/albums?offset=2&limit=2&include_groups=appears_on&market=ES",
                response.getNext());
        assertEquals(0, response.getOffset());
        assertEquals(308, response.getTotal());
        AlbumResponseData albumResponseData = albums.stream()
                .filter(album -> "album1".equals(album.getId())).findFirst().orElse(null);
        assertNotNull(albumResponseData);
        assertEquals("album1", albumResponseData.getId());
        assertEquals("Classic Club Monsters (25 Floor Killers)", albumResponseData.getName());
        assertEquals("2018-02-02", albumResponseData.getReleaseDate());
        assertEquals("compilation", albumResponseData.getAlbumType());
        assertEquals("appears_on", albumResponseData.getAlbumGroup());
        assertEquals("album", albumResponseData.getType());
        assertEquals(3, albumResponseData.getImages().size());
    }

    // GetArtistApiRequest tests

    @Test
    public void get_GetArtistApiRequestSentSuccessfully_ReturnResponse()
            throws TooManyRequestsException, SpotifyApiException, IOException {
        String artistId = "artist1";
        var request = new GetArtistApiRequest.Builder(artistId)
                .build();
        assertSuccessfulGetArtistApiRequest(artistId, request, null);
    }

    @Test
    public void get_GetArtistApiRequestSentSuccessfullyWithUrl_ReturnResponse()
            throws TooManyRequestsException, SpotifyApiException, IOException {
        var artistId = "artist1";
        var url = getApiUrl("/artists/" + artistId);
        assertSuccessfulGetArtistApiRequest(artistId, null, url);
    }

    private void assertSuccessfulGetArtistApiRequest(String artistId, GetArtistApiRequest request, String url)
            throws IOException, TooManyRequestsException, SpotifyApiException {
        setupServerExpectation("spotifyapi/get-artist-response.json", "/artists/" + artistId);

        GetArtistApiResponse response = request == null ?
                client.get("token", url, GetArtistApiResponse.class) :
                client.get("token", request, GetArtistApiResponse.class);

        server.verify();
        assertNotNull(response.getArtistResponseData());
        ArtistResponseData artistResponseData = response.getArtistResponseData();
        assertEquals("artist1", artistResponseData.getId());
        assertEquals("Artist Name", artistResponseData.getName());
        assertEquals(36, artistResponseData.getPopularity());
        assertFalse(artistResponseData.getImages().isEmpty());
    }

    // SearchApiRequest tests
    @Test
    public void get_SearchApiRequestSentSuccessfully_ReturnResponse()
            throws TooManyRequestsException, SpotifyApiException, IOException {
        var request = new SearchApiRequest.Builder("artist")
                .limit(2)
                .types(List.of("artist", "track"))
                .build();
        assertSuccessfulSearchApiRequest(request, null);
    }

    @Test
    public void get_SearchApiRequestSentSuccessfullyWithUrl_ReturnResponse()
            throws TooManyRequestsException, SpotifyApiException, IOException {
        var url = getApiUrl("/search?q=artist&limit=2&offset=0&type=artist,track");
        assertSuccessfulSearchApiRequest(null, url);
    }

    private void assertSuccessfulSearchApiRequest(SearchApiRequest request, String url)
            throws IOException, TooManyRequestsException, SpotifyApiException {
        setupServerExpectation("spotifyapi/search-response.json", "/search?q=artist&limit=2&offset=0&type=artist,track");

        SearchApiResponse response = request == null ?
                client.get("token", url, SearchApiResponse.class) :
                client.get("token", request, SearchApiResponse.class);

        server.verify();

        SearchResponseData<ArtistResponseData> artists = response.getArtists();
        SearchResponseData<SearchTrackResponseData> tracks = response.getTracks();
        assertNotNull(artists);
        assertNotNull(tracks);

        assertEquals(2, artists.getLimit());
        assertEquals(0, artists.getOffset());
        assertEquals(1687, artists.getTotal());

        assertEquals(2, tracks.getLimit());
        assertEquals(0, tracks.getOffset());
        assertEquals(28514, tracks.getTotal());

        assertEquals(2, artists.getItems().size());
        assertEquals(2, tracks.getItems().size());

        assertEquals("Artist One", artists.getItems().get(0).getName());
        assertEquals("Track One", tracks.getItems().get(0).getAlbum().getName());
    }

    // Auth tests

    @Test
    public void getToken_WhenClientIdIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () ->
                client.getToken(null, spotifyProperties.getClientSecret()));
    }

    @Test
    public void getToken_WhenClientSecretIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () ->
                client.getToken(spotifyProperties.getClientId(), null));
    }

    @Test
    public void getToken_WhenRequestIsSentSuccessfully_ReturnTokenResponse()
            throws IOException, SpotifyApiException {
        String authData = spotifyProperties.getClientId() + ":" + spotifyProperties.getClientSecret();
        String encodedAuthData = Base64.getEncoder().encodeToString(authData.getBytes(StandardCharsets.UTF_8));
        String responseBody = Files.readString(new ClassPathResource("spotifyapi/get-token-response.json")
                .getFile().toPath(), StandardCharsets.UTF_8);

        var responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        server.expect(requestTo(spotifyProperties.getAuthUrl()))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuthData))
                .andExpect(header(DefaultSpotifyHttpClient.TOMORR_TRACE_ID_HEADER, any(String.class)))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE
                        + ";charset=UTF-8"))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andRespond(withSuccess().headers(responseHeaders).body(responseBody));

        TokenResponse response = client.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret());
        server.verify();

        assertNotNull(response);
        assertEquals("NgCXRKcMzYjw", response.getAccessToken());
        assertEquals("bearer", response.getTokenType());
        assertEquals(3600, response.getExpiresIn());
    }

    @Test
    public void getToken_WhenErrorOccurs_ThrowSpotifyApiException() {
        server.expect(requestTo(spotifyProperties.getAuthUrl()))
                .andRespond(withServerError());
        assertThrows(SpotifyApiException.class, () ->
                client.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret()));
    }

    // Helper methods

    private TooManyRequestsException sendRequestForTooManyRequestsException(HttpHeaders responseHeaders) {
        String url = getApiUrl("/artists");
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS).headers(responseHeaders));
        return assertThrows(TooManyRequestsException.class, () ->
                client.get("token", url, GetArtistsApiResponse.class));
    }

    private String getApiUrl(String pathAndParams) {
        return spotifyProperties.getApiUrl() + pathAndParams;
    }


    private void setupServerExpectation(String responseResource, String query) throws IOException {
        String responseBody = Files.readString(new ClassPathResource(responseResource)
                .getFile().toPath(), StandardCharsets.UTF_8);
        var responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        server.expect(requestTo(getApiUrl(query)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess().headers(responseHeaders).body(responseBody));
    }
}