package com.kushtrimh.tomorr.artist.service;

import com.kushtrimh.tomorr.artist.cache.ArtistCache;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.SpotifyApiClient;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistApiResponse;
import org.springframework.stereotype.Service;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultArtistSearchService implements ArtistSearchService {

    private final ArtistCache artistCache;
    private final SpotifyApiClient spotifyApiClient;

    public DefaultArtistSearchService(ArtistCache artistCache, SpotifyApiClient spotifyApiClient) {
        this.artistCache = artistCache;
        this.spotifyApiClient = spotifyApiClient;
    }

    @Override
    public boolean exists(String artistId) {
        GetArtistApiRequest request = new GetArtistApiRequest.Builder(artistId).build();
        try {
            GetArtistApiResponse response = spotifyApiClient.getArtist(request);
            return response != null;
        } catch (TooManyRequestsException | SpotifyApiException e) {
            return false;
        }
    }

    // TODO: When searching for an artist by name, it gets data from the cache + db,
    // using cache abstraction and returns the data, when using + external flag,
    // it should search for data on Spotify as well, and merge the two results together.
    // Have TTL be like 5 min
}
