package com.kushtrimh.tomorr.artist.cache;

import java.util.List;
import java.util.Set;

/**
 * @author Kushtrim Hajrizi
 */
public interface ArtistCache {

    Set<String> getArtistIds();

    void putArtistIds(List<String> artistIds);

    boolean containsArtistId(String artistId);
}
