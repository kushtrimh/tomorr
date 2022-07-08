package com.kushtrimh.tomorr.album;

import com.kushtrimh.tomorr.album.repository.AlbumRepository;
import com.kushtrimh.tomorr.configuration.TestDataSourceConfiguration;
import com.kushtrimh.tomorr.dal.tables.records.AlbumRecord;
import com.kushtrimh.tomorr.extension.TestDatabaseExtension;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static com.kushtrimh.tomorr.dal.Tables.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = {@Tag("database"), @Tag("integration")})
@ContextConfiguration(classes = TestDataSourceConfiguration.class)
@ExtendWith(TestDatabaseExtension.class)
@JooqTest
public class AlbumJooqRepositoryIntegrationTest {

    private final AlbumRepository<AlbumRecord> albumRepository;
    private final DSLContext create;

    @Autowired
    public AlbumJooqRepositoryIntegrationTest(
            AlbumRepository<AlbumRecord> albumRepository, DSLContext create) {
        this.albumRepository = albumRepository;
        this.create = create;
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
        expectedRecord.setType("ALBUM");
        expectedRecord.setReleaseDate("2021-01-15");
        var returnedRecord = albumRepository.findById(id);
        expectedRecord.setCreatedAt(returnedRecord.getCreatedAt());
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
        assertAll(
                () -> assertEquals(2, albumRepository.findByArtist("artist2").size()),
                () -> assertEquals(1, albumRepository.findByArtist("artist1").size()),
                () -> assertEquals(0, albumRepository.findByArtist("artist424").size())
        );

    }

    @Test
    public void findCountByArtistId_WhenArtistIdIsNull_ReturnZero() {
        assertEquals(0, albumRepository.findCountByArtistId(null));
    }

    @Test
    public void findCountByArtistId_WhenArtistIdIsInvalid_ReturnZero() {
        assertEquals(0, albumRepository.findCountByArtistId("invalid-artist-id"));
    }

    @Test
    public void findCountByArtistId_WhenArtistIdIsValid_ReturnArtistAlbumsCount() {
        assertAll(
                () -> assertEquals(2, albumRepository.findCountByArtistId("artist2")),
                () -> assertEquals(1, albumRepository.findCountByArtistId("artist1")),
                () -> assertEquals(0, albumRepository.findCountByArtistId("artist424"))
        );
    }

    @Test
    public void save_WhenAlbumIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> {
            albumRepository.save("artist1", null);
        });
    }

    @Test
    public void save_WhenAlbumTotalTracksIsNull_SaveAlbumSuccessfully() {
        var artistId = "artist1";
        var album = newAlbumRecord("album-15254");
        album.setTotalTracks(null);
        assertDoesNotThrow(() -> albumRepository.save(artistId, album));
    }

    @Test
    public void save_WhenAlbumIsValid_SaveSuccessfully() {
        var initialCount = albumRepository.count();
        var id = "new-al";
        var artistId = "artist1";
        var album = newAlbumRecord(id);

        var savedAlbum = albumRepository.save(artistId, album);
        var countAfterSave = albumRepository.count();
        var returnedAlbum = albumRepository.findById(id);

        var artistAlbumRecord = create.selectFrom(ARTIST_ALBUM)
                .where(ARTIST_ALBUM.ARTIST_ID.eq(artistId))
                .and(ARTIST_ALBUM.ALBUM_ID.eq(id))
                .fetchOne();

        // Because date is generated by the database itself on insert
        // we assign it to the expected album
        album.setCreatedAt(returnedAlbum.getCreatedAt());
        savedAlbum.setCreatedAt(returnedAlbum.getCreatedAt());

        assertAll(
                () -> assertEquals(album, savedAlbum),
                () -> assertNotEquals(initialCount, countAfterSave),
                () -> assertEquals(album, returnedAlbum),
                () -> assertNotNull(artistAlbumRecord)
        );
    }

    @Test
    public void save_WhenTryingToSaveExistingAlbum_SaveSuccessfully() {
        var id = "new-al634263";
        var artistId = "artist1";
        var album = newAlbumRecord(id);

        // Save twice
        albumRepository.save(artistId, album);
        assertDoesNotThrow(() -> albumRepository.save(artistId, album));
    }

    @Test
    public void saveAll_WhenAlbumsListIsEmpty_DoNothing() {
        var artistId = "artist1";
        var initialCount = albumRepository.count();
        albumRepository.saveAll(artistId, Collections.emptyList());
        assertEquals(initialCount, albumRepository.count());
    }

    @Test
    public void saveAll_WhenAlbumListIsFilled_SaveAlbumsSuccessfully() {
        var initialCount = albumRepository.count();
        var artistId = "artist545456";

        List<AlbumRecord> newAlbums = IntStream.range(0, 10)
                .mapToObj(i -> newAlbumRecord("batch-album-" + i))
                .toList();
        albumRepository.saveAll(artistId, newAlbums);

        var savedArtistAlbumsCount = create.selectCount().from(ARTIST_ALBUM)
                .where(ARTIST_ALBUM.ARTIST_ID.eq(artistId))
                .fetchOne(0, int.class);

        assertAll(
                () -> assertEquals(initialCount + newAlbums.size(), albumRepository.count()),
                () -> assertEquals(newAlbums.size(), savedArtistAlbumsCount)
        );
    }

    @Test
    public void deleteById_WhenAlbumIdIsNull_DoesNotDelete() {
        var initialCount = albumRepository.count();
        albumRepository.deleteById(null);
        var countAfterDelete = albumRepository.count();
        assertEquals(initialCount, countAfterDelete);
    }

    @Test
    public void deleteById_WhenAlbumDoesNotExist_DoesNotDelete() {
        var initialCount = albumRepository.count();
        albumRepository.deleteById("invalid-id");
        var countAfterDelete = albumRepository.count();
        assertEquals(initialCount, countAfterDelete);
    }

    @Test
    public void deleteById_WhenAlbumDoesExist_DeleteAlbum() {
        var id = "album-to-delete";
        var album = newAlbumRecord(id);
        albumRepository.save("artist1", album);
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
        album.setType("ALBUM");
        album.setTotalTracks(10);
        return album;
    }
}
