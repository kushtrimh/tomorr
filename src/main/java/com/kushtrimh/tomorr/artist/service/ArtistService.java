package com.kushtrimh.tomorr.artist.service;

import com.kushtrimh.tomorr.artist.Artist;

import java.util.List;
import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
public interface ArtistService {

    Optional<Artist> findById(String id);

    Optional<Artist> findByName(String name);

    List<Artist> searchByName(String name);

    List<Artist> findToSync(String syncKey, int count);

    void save(Artist artist);

    void deleteById(String id);

    boolean exists(String id);
}
