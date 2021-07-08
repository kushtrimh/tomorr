package com.kushtrimh.tomorr.artist.cache;

import java.util.List;
import java.util.Set;

/**
 * @author Kushtrim Hajrizi
 */
public interface ArtistCache {

    Set<String> getSearchedArtistIds();

    void putSearchedArtistIds(List<String> artistIds);

    boolean containsSearchedArtistId(String artistId);
}
