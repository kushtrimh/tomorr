package com.kushtrimh.tomorr.album;

import com.kushtrimh.tomorr.album.repository.AlbumRepository;
import com.kushtrimh.tomorr.configuration.TestDataSourceConfiguration;
import com.kushtrimh.tomorr.dal.extension.TestDatabaseExtension;
import com.kushtrimh.tomorr.dal.tables.records.AlbumRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@ContextConfiguration(classes = TestDataSourceConfiguration.class)
@ExtendWith({SpringExtension.class, TestDatabaseExtension.class})
public class AlbumJooqRepositoryTest {

    private final AlbumRepository<AlbumRecord> albumRepository;

    @Autowired
    public AlbumJooqRepositoryTest(AlbumRepository<AlbumRecord> albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Test
    public void count_ReturnAll() {
        int expectedAlbums = 3;
        int albumsCount = albumRepository.count();
        assertEquals(expectedAlbums, albumsCount);
    }
}
