package com.kushtrimh.tomorr.artist;

import com.kushtrimh.tomorr.artist.repository.ArtistRepository;
import com.kushtrimh.tomorr.configuration.TestDataSourceConfiguration;
import com.kushtrimh.tomorr.dal.extension.TestDatabaseExtension;
import com.kushtrimh.tomorr.dal.tables.records.ArtistRecord;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tag("database")
@ContextConfiguration(classes = TestDataSourceConfiguration.class)
@ExtendWith({TestDatabaseExtension.class})
@JooqTest
public class ArtistJooqRepositoryTest {

    private final ArtistRepository<ArtistRecord> artistRepository;

    @Autowired
    public ArtistJooqRepositoryTest(ArtistRepository<ArtistRecord> artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Test
    public void count_ReturnAll() {
        var expectedArtists = 3;
        var artistCounts = artistRepository.count();
        assertTrue(artistCounts >= expectedArtists);
    }

    @Test
    public void findById_WhenIdIsNull_ReturnNull() {
        assertNull(artistRepository.findById(null));
    }

    @Test
    public void findById_WhenIdIsEmpty_ReturnNull() {
        assertNull(artistRepository.findById(""));
    }

    @Test
    public void findById_WhenIdDoesNotExist_ReturnNull() {
        assertNull(artistRepository.findById("non-existing-artist"));
    }

    @Test
    public void findById_WhenArtistWithIdExists_ReturnArtist() {
        var id = "artist1";
        var expectedArtist = new ArtistRecord();
        expectedArtist.setId(id);
        expectedArtist.setName("Artist One");
        expectedArtist.setImageHref("artist-one-image");
        expectedArtist.setPopularity(50);
        var returnedRecord = artistRepository.findById(id);
        expectedArtist.setCreatedAt(returnedRecord.getCreatedAt());
        assertEquals(expectedArtist, returnedRecord);
    }

    @Test
    public void findByName_WhenArtistNameIsNullOrEmptyOrWrong_ReturnNull() {
        assertNull(artistRepository.findByName(null));
        assertNull(artistRepository.findByName(""));
        assertNull(artistRepository.findByName("wrong-artist-name"));
    }

    @Test
    public void findByName_WhenArtistWithNameExists_ReturnArtist() {
        var name = "Artist One";
        var expectedArtist = new ArtistRecord();
        expectedArtist.setId("artist1");
        expectedArtist.setName("Artist One");
        expectedArtist.setImageHref("artist-one-image");
        expectedArtist.setPopularity(50);
        var returnRecord = artistRepository.findByName(name);
        expectedArtist.setCreatedAt(returnRecord.getCreatedAt());
        assertEquals(expectedArtist, returnRecord);
    }

    @Test
    public void searchByName_WhenSearchStringIsNullOrWrong_ReturnEmptyList() {
        assertEquals(0, artistRepository.searchByName(null).size());
        assertEquals(0, artistRepository.searchByName("wrong-name").size());
    }

    @Test
    public void searchByName_WhenSearchQueryIsValid_ReturnArtists() {
        assertEquals(2, artistRepository.searchByName("Artist T").size());
        assertEquals(1, artistRepository.searchByName("Artist One").size());
        assertEquals(3, artistRepository.searchByName("Artist").size());
        assertEquals(3, artistRepository.searchByName("A").size());
    }

    @Test
    public void save_WhenArtistIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> {
            artistRepository.save(null);
        });
    }

    @Test
    public void save_WhenArtistIsValid_SaveSuccessfully() {
        var initialCount = artistRepository.count();
        var id = "new-artist";
        var artist = newArtistRecord(id);
        var savedRecord = artistRepository.save(artist);
        assertEquals(artist, savedRecord);

        var countAfterSave = artistRepository.count();
        assertNotEquals(initialCount, countAfterSave);

        var returnedArtist = artistRepository.findById(id);
        artist.setCreatedAt(returnedArtist.getCreatedAt());
        assertEquals(artist, returnedArtist);
    }

    @Test
    public void deleteById_WhenArtistIdIsNull_DoesNotDelete() {
        var initialCount = artistRepository.count();
        artistRepository.deleteById(null);
        var countAfterDelete = artistRepository.count();
        assertEquals(initialCount, countAfterDelete);
    }

    @Test
    public void deleteById_WhenArtistIdDoesNotExist_DoesNotDelete() {
        var initialCount = artistRepository.count();
        artistRepository.deleteById("non-existing-id");
        var countAfterDelete = artistRepository.count();
        assertEquals(initialCount, countAfterDelete);
    }

    @Test
    public void deleteById_WhenArtistDoesExist_DeleteSuccessfully() {
        var id = "artist-to-delete";
        var artist = newArtistRecord(id);
        artistRepository.save(artist);
        var initialCount = artistRepository.count();

        artistRepository.deleteById(id);
        var countAfterDelete = artistRepository.count();
        assertNotEquals(initialCount, countAfterDelete);

        assertNull(artistRepository.findById(id));
    }

    private ArtistRecord newArtistRecord(String id) {
        var artist = new ArtistRecord();
        artist.setId(id);
        artist.setName("album-name-" + id);
        artist.setPopularity(100);
        artist.setImageHref("album-image-href-" + id);
        return artist;
    }
}
