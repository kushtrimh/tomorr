package com.kushtrimh.tomorr.album.repository;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
public interface AlbumRepository<T> {

    int count();

    T findById(String id);

    Integer findCountByArtistId(String artistId);

    List<T> findByArtist(String artistId);

    T save(T album);

    void saveAll(List<T> albums);

    void deleteById(String id);
}
