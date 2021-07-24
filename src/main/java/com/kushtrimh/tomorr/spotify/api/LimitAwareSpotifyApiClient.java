package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistAlbumsApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistsApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistAlbumsApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistsApiResponse;
import com.kushtrimh.tomorr.spotify.limit.RequestLimitService;

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
    public TokenResponse refreshAccessToken() throws SpotifyApiException {
        return baseClient.refreshAccessToken();
    }

    private void checkLimit() throws TooManyRequestsException {
        if (requestLimitService.cantSendRequest()) {
            throw new TooManyRequestsException("Reach limit for requests");
        }
    }
}
