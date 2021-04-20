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
        var expectedAlbums = 3;
        var albumsCount = albumRepository.count();
        assertTrue(albumsCount >= expectedAlbums);
    }

    @Test
    public void findById_WhenIdIsNull_ReturnNull() {
        assertNull(albumRepository.findById(null));
    }

    @Test
    public void findById_WhenIdIsEmpty_ReturnNull() {
        assertNull(albumRepository.findById(""));
    }

    @Test
    public void findById_WhenIdDoesNotExist_ReturnNull() {
        assertNull(albumRepository.findById("non-existing-album"));
    }

    @Test
    public void findById_WhenRecordDoesExist_ReturnAlbumRecord() {
        var id = "album1";
        var expectedRecord = new AlbumRecord();
        expectedRecord.setId(id);
        expectedRecord.setName("Album 1 Name");
        expectedRecord.setTotalTracks(8);
        expectedRecord.setImageHref("album-one-image");
        expectedRecord.setType("album");
        expectedRecord.setReleaseDate("2021-01-15");
        var returnedRecord = albumRepository.findById(id);
        assertEquals(expectedRecord, returnedRecord);
    }

    @Test
    public void findByArtist_WhenArtistIdIsNull_ReturnEmptyList() {
        assertTrue(albumRepository.findByArtist(null).isEmpty());
    }

    @Test
    public void findByArtist_WhenArtistIdIsInvalid_ReturnEmptyList() {
        assertTrue(albumRepository.findByArtist("invalid-artist-id").isEmpty());
    }

    @Test
    public void findByArtist_WhenArtistIdIsValid_ReturnArtistAlbums() {
        assertEquals(2, albumRepository.findByArtist("artist2").size());
        assertEquals(1, albumRepository.findByArtist("artist1").size());
        assertEquals(0, albumRepository.findByArtist("artist424").size());
    }

    @Test
    public void save_WhenArtistIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> {
            albumRepository.save(null);
        });
    }

    @Test
    public void save_WhenArtistIsValid_SaveSuccessfully() {
        var initialCount = albumRepository.count();
        var id = "new-al";
        var album = newAlbumRecord(id);
        albumRepository.save(album);
        var countAfterSave = albumRepository.count();
        assertNotEquals(initialCount, countAfterSave);

        var returnedAlbum = albumRepository.findById(id);
        assertEquals(album, returnedAlbum);
    }

    @Test
    public void deleteById_WhenArtistIdIsNull_DoesNotDelete() {
        var initialCount = albumRepository.count();
        albumRepository.deleteById(null);
        var countAfterDelete = albumRepository.count();
        assertEquals(initialCount, countAfterDelete);
    }

    @Test
    public void deleteById_WhenArtistDoesNotExist_DoesNotDelete() {
        var initialCount = albumRepository.count();
        albumRepository.deleteById("invalid-id");
        var countAfterDelete = albumRepository.count();
        assertEquals(initialCount, countAfterDelete);
    }

    @Test
    public void deleteById_WhenArtistDoesExist_DeleteAlbum() {
        var id = "album-to-delete";
        var album = newAlbumRecord(id);
        albumRepository.save(album);
        var initialCount = albumRepository.count();

        albumRepository.deleteById(id);
        var countAfterDelete = albumRepository.count();
        assertNotEquals(initialCount, countAfterDelete);

        var albumToDelete = albumRepository.findById(id);
        assertNull(albumToDelete);
    }

    private AlbumRecord newAlbumRecord(String id) {
        var album = new AlbumRecord();
        album.setId(id);
        album.setName("album-name-" + id);
        album.setReleaseDate("2012-03-05");
        album.setImageHref("album-image-href-" + id);
        album.setType("album");
        album.setTotalTracks(10);
        return album;
    }
}
