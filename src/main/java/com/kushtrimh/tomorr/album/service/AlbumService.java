package com.kushtrimh.tomorr.album.service;


import com.kushtrimh.tomorr.album.Album;

import java.util.List;
import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
public interface AlbumService {

    Optional<Album> findById(String id);

    List<Album> findByArtist(String artistId);

    void save(Album album);

    void deleteById(String id);
}
