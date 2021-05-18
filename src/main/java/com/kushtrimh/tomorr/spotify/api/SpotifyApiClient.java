package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.properties.SpotifyProperties;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistAlbumsApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistsApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistAlbumsApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistsApiResponse;
import com.kushtrimh.tomorr.spotify.http.SpotifyHttpClient;

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

        this.accessToken = "";
    }

    public GetArtistsApiResponse getMultipleArtists(GetArtistsApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        return httpClient.get(accessToken, request, GetArtistsApiResponse.class);
    }

    public GetArtistAlbumsApiResponse getArtistAlbums(GetArtistAlbumsApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        return httpClient.get(accessToken, request, GetArtistAlbumsApiResponse.class);
    }

    public GetArtistAlbumsApiResponse getArtistAlbums(String url)
            throws TooManyRequestsException, SpotifyApiException {
        return httpClient.get(accessToken, url, GetArtistAlbumsApiResponse.class);
    }

    public TokenResponse refreshAccessToken() throws SpotifyApiException {
        TokenResponse response = httpClient.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret());
        accessToken = response.getAccessToken();
        return response;
    }
}
