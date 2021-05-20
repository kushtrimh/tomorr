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
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Kushtrim Hajrizi
 */
public class SpotifyApiClient {

    public final static String ACCESS_TOKEN = "accesstoken";

    private final SpotifyHttpClient httpClient;
    private final SpotifyProperties spotifyProperties;
    private final StringRedisTemplate stringRedisTemplate;

    public SpotifyApiClient(SpotifyHttpClient httpClient,
                            SpotifyProperties spotifyProperties,
                            StringRedisTemplate stringRedisTemplate) {
        this.httpClient = httpClient;
        this.spotifyProperties = spotifyProperties;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public GetArtistsApiResponse getMultipleArtists(GetArtistsApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        return httpClient.get(getAccessToken(), request, GetArtistsApiResponse.class);
    }

    public GetArtistAlbumsApiResponse getArtistAlbums(GetArtistAlbumsApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        return httpClient.get(getAccessToken(), request, GetArtistAlbumsApiResponse.class);
    }

    public GetArtistAlbumsApiResponse getArtistAlbums(String url)
            throws TooManyRequestsException, SpotifyApiException {
        return httpClient.get(getAccessToken(), url, GetArtistAlbumsApiResponse.class);
    }

    public TokenResponse refreshAccessToken() throws SpotifyApiException {
        TokenResponse response = httpClient.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret());
        stringRedisTemplate.opsForValue().set(ACCESS_TOKEN, response.getAccessToken());
        return response;
    }

    private String getAccessToken() {
        return stringRedisTemplate.opsForValue().get(ACCESS_TOKEN);
    }
}
