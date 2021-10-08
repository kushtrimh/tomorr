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
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistAlbumsApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistsApiResponse;

/**
 * @author Kushtrim Hajrizi
 */
public class LimitAwareSpotifyApiClient implements SpotifyApiClient {
    // TODO: Switch to using AOP instead of checking each method one by one
    private final SpotifyApiClient baseClient;
    private final RequestLimitService requestLimitService;

    public LimitAwareSpotifyApiClient(SpotifyApiClient baseClient, RequestLimitService requestLimitService) {
        this.baseClient = baseClient;
        this.requestLimitService = requestLimitService;
    }

    @Override
    public GetArtistsApiResponse getMultipleArtists(GetArtistsApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        checkLimit();
        return baseClient.getMultipleArtists(request);
    }

    @Override
    public GetArtistAlbumsApiResponse getArtistAlbums(GetArtistAlbumsApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        checkLimit();
        return baseClient.getArtistAlbums(request);
    }

    @Override
    public GetArtistAlbumsApiResponse getArtistAlbums(String url)
            throws TooManyRequestsException, SpotifyApiException {
        checkLimit();
        return baseClient.getArtistAlbums(url);
    }

    @Override
    public GetArtistApiResponse getArtist(GetArtistApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        checkLimit();
        return baseClient.getArtist(request);
    }

    @Override
    public SearchApiResponse search(SearchApiRequest request)
            throws TooManyRequestsException, SpotifyApiException {
        checkLimit();
        return baseClient.search(request);
    }

    @Override
    public TokenResponse refreshAccessToken() throws SpotifyApiException {
        return baseClient.refreshAccessToken();
    }

    private void checkLimit() throws TooManyRequestsException {
        if (!requestLimitService.tryFor(LimitType.SPOTIFY_EXTERNAL)) {
            throw new TooManyRequestsException("Reach limit for requests");
        }
    }
}
