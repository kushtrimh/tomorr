package com.kushtrimh.tomorr.artist.repository;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
public interface ArtistRepository<T> {

    int count();

    T findById(String id);

    T findByName(String name);

    List<T> searchByName(String name);

    void save(T artist);

    void deleteById(String id);
}
