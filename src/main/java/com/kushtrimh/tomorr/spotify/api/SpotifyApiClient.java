package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.properties.SpotifyProperties;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistsApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistsApiResponse;
import com.kushtrimh.tomorr.spotify.http.SpotifyHttpClient;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
public class SpotifyApiClient {

    private String accessToken;

    private final SpotifyHttpClient httpClient;
    private final SpotifyProperties spotifyProperties;

    public SpotifyApiClient(SpotifyHttpClient httpClient,
                            SpotifyProperties spotifyProperties) {
        this.httpClient = httpClient;
        this.spotifyProperties = spotifyProperties;
    }

    public GetArtistsApiResponse getMultipleArtists(String... artistsIds) throws TooManyRequestsException, SpotifyApiException {
        GetArtistsApiRequest request = new GetArtistsApiRequest();
        request.setArtists(List.of(artistsIds));
        return httpClient.get(accessToken, request, GetArtistsApiResponse.class);
    }

    public TokenResponse refreshAccessToken() throws SpotifyApiException {
        TokenResponse response = httpClient.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret());
        accessToken = response.getAccessToken();
        return response;
    }
}
