package com.kushtrimh.tomorr.album.service;

import com.kushtrimh.tomorr.album.repository.AlbumRepository;
import com.kushtrimh.tomorr.dal.tables.records.AlbumRecord;
import org.springframework.stereotype.Service;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultAlbumService implements AlbumService {

    private AlbumRepository<AlbumRecord> albumRepository;

    public DefaultAlbumService(AlbumRepository<AlbumRecord> albumRepository) {
        this.albumRepository = albumRepository;
    }
}
