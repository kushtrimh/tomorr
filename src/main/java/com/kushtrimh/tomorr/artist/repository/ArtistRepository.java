package com.kushtrimh.tomorr.artist.repository;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
public interface ArtistRepository<T> {

    int count();

    T findById(String id);

    T findByName(String name);

    List<T> findToSync(String syncKey, int count);

    List<T> searchByName(String name);

    T save(T artist);

    void deleteById(String id);
}
