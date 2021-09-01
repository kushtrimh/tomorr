package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.properties.SpotifyProperties;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.SpotifyCacheKeys;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistAlbumsApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistsApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.SearchApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.SearchApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistAlbumsApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistsApiResponse;
import com.kushtrimh.tomorr.spotify.http.SpotifyHttpClient;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Kushtrim Hajrizi
 */
public class DefaultSpotifyApiClient implements SpotifyApiClient {

    private final SpotifyHttpClient httpClient;
    private final SpotifyProperties spotifyProperties;
    private final StringRedisTemplate stringRedisTemplate;

    public DefaultSpotifyApiClient(SpotifyHttpClient httpClient,
                                   SpotifyProperties spotifyProperties,
                                   StringRedisTemplate stringRedisTemplate) {
        this.httpClient = httpClient;
        this.spotifyProperties = spotifyProperties;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public GetArtistsApiResponse getMultipleArtists(GetArtistsApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        return httpClient.get(getAccessToken(), request, GetArtistsApiResponse.class);
    }

    @Override
    public GetArtistAlbumsApiResponse getArtistAlbums(GetArtistAlbumsApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        return httpClient.get(getAccessToken(), request, GetArtistAlbumsApiResponse.class);
    }

    @Override
    public GetArtistAlbumsApiResponse getArtistAlbums(String url)
            throws TooManyRequestsException, SpotifyApiException {
        return httpClient.get(getAccessToken(), url, GetArtistAlbumsApiResponse.class);
    }

    @Override
    public GetArtistApiResponse getArtist(GetArtistApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        return httpClient.get(getAccessToken(), request, GetArtistApiResponse.class);
    }

    @Override
    public SearchApiResponse search(SearchApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        return httpClient.get(getAccessToken(), request, SearchApiResponse.class);
    }

    @Override
    public TokenResponse refreshAccessToken() throws SpotifyApiException {
        TokenResponse response = httpClient.getToken(spotifyProperties.getClientId(), spotifyProperties.getClientSecret());
        stringRedisTemplate.opsForValue().set(SpotifyCacheKeys.ACCESS_TOKEN_KEY, response.getAccessToken());
        return response;
    }

    private String getAccessToken() {
        return stringRedisTemplate.opsForValue().get(SpotifyCacheKeys.ACCESS_TOKEN_KEY);
    }
}
