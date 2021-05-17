package com.kushtrimh.tomorr.spotify.http;

import com.kushtrimh.tomorr.configuration.TestSpotifyConfiguration;
import com.kushtrimh.tomorr.properties.SpotifyProperties;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistsApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.artist.ArtistResponseData;
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
        String token = "token";
        GetArtistsApiRequest request = new GetArtistsApiRequest.Builder().artists(List.of("artist1", "artist2")).build();
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
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Retry-After", "30");
        TooManyRequestsException ex = sendRequestForTooManyRequestsException(responseHeaders);
        assertEquals(ex.getRetryAfter(), 30);
    }

    // Request specific tests

    // GetArtistsApiRequest tests

    @Test
    public void get_WhenGetArtistsApiRequestSentSuccessfully_ReturnResponse()
            throws IOException, TooManyRequestsException, SpotifyApiException {
        GetArtistsApiRequest request = new GetArtistsApiRequest.Builder().artists(List.of("artist1", "artist2")).build();
        assertSuccessfulGetArtistsApiRequest(request, null);
    }

    @Test
    public void get_WhenGetArtistsApiRequestSentSuccessfullyWithUrl_ReturnResponse()
            throws IOException, TooManyRequestsException, SpotifyApiException {
        String url = getApiUrl("/artists?ids=artist1,artist2");
        assertSuccessfulGetArtistsApiRequest(null, url);
    }

    private void assertSuccessfulGetArtistsApiRequest(GetArtistsApiRequest request, String url)
            throws TooManyRequestsException, SpotifyApiException, IOException {
        String token = "token";
        String responseBody = Files.readString(new ClassPathResource("spotifyapi/get-artists-response.json")
                .getFile().toPath(), StandardCharsets.UTF_8);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        server.expect(requestTo(getApiUrl("/artists?ids=artist1,artist2")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess().headers(responseHeaders).body(responseBody));

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

    // GetArtistAlbumsTests tests

    @Test
    public void get_GetArtistAlbumsApiRequestSentSuccessfully_ReturnResponse() {
        // TODO:
    }

    @Test
    public void get_GetArtistAlbumsApiRequestSentSuccessfullyWithUrl_ReturnResponse() {
        // TODO:
    }

    // Auth tests

    @Test
    public void getToken_WhenClientIdIsNull_ThrowException() {
        // TODO:
    }

    @Test
    public void getToken_WhenClientSecretIsNull_ThrowException() {
        // TODO:
    }

    @Test
    public void getToken_WhenRequestIsSentSuccessfully_ReturnTokenResponse() {
        // TODO:
    }

    @Test
    public void getToken_WhenErrorOccurs_ThrowSpotifyApiException() {
        // TODO:
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
}