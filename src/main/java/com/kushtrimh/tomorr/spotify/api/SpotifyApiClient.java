package com.kushtrimh.tomorr.spotify.api;

import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistAlbumsApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistsApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistAlbumsApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistsApiResponse;

/**
 * @author Kushtrim Hajrizi
 */
public interface SpotifyApiClient {
    GetArtistsApiResponse getMultipleArtists(GetArtistsApiRequest request)
        throws TooManyRequestsException, SpotifyApiException;

    GetArtistAlbumsApiResponse getArtistAlbums(GetArtistAlbumsApiRequest request)
        throws TooManyRequestsException, SpotifyApiException;

    GetArtistAlbumsApiResponse getArtistAlbums(String url)
        throws TooManyRequestsException, SpotifyApiException;

    GetArtistApiResponse getArtist(GetArtistApiRequest request)
        throws TooManyRequestsException, SpotifyApiException;

    TokenResponse refreshAccessToken() throws SpotifyApiException;
}
