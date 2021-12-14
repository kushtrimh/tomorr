package com.kushtrimh.tomorr.artist.service;

import com.kushtrimh.tomorr.artist.Artist;

import java.util.List;
import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
public interface ArtistSearchService {

    boolean exists(String artistId);

    Optional<Artist> get(String artistId);

    List<Artist> search(String name, boolean external);
}
