package com.kushtrimh.tomorr.artist.service;

import org.springframework.stereotype.Service;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultArtistSearchService implements ArtistSearchService {

    @Override
    public boolean exists(String artistId) {
        return false;
    }

    // TODO: When searching for an artist by name, it gets data from the cache + db,
    // using cache abstraction and returns the data, when using + external flag,
    // it should search for data on Spotify as well, and merge the two results together.
    // Have TTL be like 5 min
}
