package com.kushtrimh.tomorr.album.service;


import com.kushtrimh.tomorr.album.Album;

import java.util.List;
import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
public interface AlbumService {

    Optional<Album> findById(String id);

    Optional<Integer> findCountByArtistId(String artistId);

    List<Album> findByArtist(String artistId);

    void save(String artistId, Album album);

    void saveAll(String artistId, List<Album> albums);

    void deleteById(String id);
}
