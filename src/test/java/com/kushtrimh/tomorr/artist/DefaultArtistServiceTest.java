package com.kushtrimh.tomorr.artist;

import com.kushtrimh.tomorr.artist.repository.ArtistRepository;
import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.artist.service.DefaultArtistService;
import com.kushtrimh.tomorr.dal.tables.records.ArtistRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith(MockitoExtension.class)
public class DefaultArtistServiceTest {

    @Mock
    private ArtistRepository<ArtistRecord> artistRepository;

    private ArtistService artistService;

    @BeforeEach
    public void init() {
        artistService = new DefaultArtistService(artistRepository);
    }

    @Test
    public void findById_WhenRecordWithIdDoesNotExist_ReturnEmptyOptional() {
        var id = "invalid-id";
        when(artistRepository.findById(id)).thenReturn(null);
        Optional<Artist> artistOpt = artistService.findById(id);
        assertTrue(artistOpt.isEmpty());
    }

    @Test
    public void findById_WhenRecordDoesExist_ReturnArtistInsideOptional() {
        var id = "artist-id-01";
        var record = newArtistRecord(id);
        when(artistRepository.findById(id)).thenReturn(record);
        Optional<Artist> artistOpt = artistService.findById(id);
        compareArtistRecordAndArtist(record, artistOpt.get());
    }

    @Test
    public void findByName_WhenRecordWithNameDoesNotExist_ReturnEmptyOptional() {
        var id = "invalid-id";
        var name = "name" + id;
        when(artistRepository.findByName(name)).thenReturn(null);
        Optional<Artist> artistOpt = artistService.findByName(name);
        assertTrue(artistOpt.isEmpty());
    }

    @Test
    public void findByName_WhenRecordDoesExist_ReturnArtistInsideOptional() {
        var id = "artist-id-01";
        var name = "name" + id;
        var record = newArtistRecord(id);
        when(artistRepository.findByName(name)).thenReturn(record);
        Optional<Artist> artistOpt = artistService.findByName(name);
        compareArtistRecordAndArtist(record, artistOpt.get());
    }

    @Test
    public void findToSync_WhenSyncKeyIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> artistService.findToSync(null, 10));
    }

    @Test
    public void findToSync_WhenSyncIsProvided_ReturnArtists() {
        String syncKey = UUID.randomUUID().toString();
        ArtistRecord record1 = newArtistRecord("id1");
        ArtistRecord record2 = newArtistRecord("id2");
        when(artistRepository.findToSync(syncKey, 2)).thenReturn(List.of(record1, record2));
        List<Artist> artists = artistService.findToSync(syncKey, 2);
        compareArtistRecordAndArtist(record1, artists.get(0));
        compareArtistRecordAndArtist(record2, artists.get(1));
    }

    @Test
    public void searchByName_WhenRecordsAreNotFound_ReturnEmptyList() {
        String name = "invalid-name";
        when(artistRepository.searchByName(name)).thenReturn(Collections.emptyList());
        assertTrue(artistService.searchByName(name).isEmpty());
    }

    @Test
    public void searchByName_WhenRecordsAreFound_ReturnArtistsList() {
        ArtistRecord record1 = newArtistRecord("id2");
        ArtistRecord record2 = newArtistRecord("id3");
        String name = "name-";
        when(artistRepository.searchByName(name)).thenReturn(List.of(record1, record2));
        List<Artist> artists = artistService.searchByName(name);
        assertFalse(artists.isEmpty());
        compareArtistRecordAndArtist(record1, artists.get(0));
        compareArtistRecordAndArtist(record2, artists.get(1));
    }

    @Test
    public void save_WhenArtistIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> {
            artistService.save(null);
        });
    }

    @Test
    public void save_WhenArtistIsValid_SaveArtistSuccessfully() {
        var record = newArtistRecord("newartist");
        var artist = new Artist(
                record.getId(),
                record.getName(),
                record.getImageHref(),
                record.getPopularity());
        artistService.save(artist);
        verify(artistRepository, times(1)).save(record);
    }

    @Test
    public void deleteById_WhenIdIsValid_DeleteSuccessfully() {
        var id = "id252";
        artistRepository.deleteById(id);
        verify(artistRepository, times(1)).deleteById(id);
    }

    @Test
    public void exists_WhenArtistDoesNotExist_ReturnFalse() {
        var id = "qwet3";
        when(artistRepository.findById(id)).thenReturn(null);
        assertFalse(artistService.exists(id));
    }

    @Test
    public void exists_WhenArtistDoesExist_ReturnTrue() {
        var id = "ga5gw";
        when(artistRepository.findById(id)).thenReturn(newArtistRecord(id));
        assertTrue(artistService.exists(id));
    }

    private void compareArtistRecordAndArtist(ArtistRecord artistRecord, Artist artist) {
        assertEquals(artistRecord.getId(), artist.id());
        assertEquals(artistRecord.getName(), artist.name());
        assertEquals(artistRecord.getImageHref(), artist.imageHref());
        assertEquals(artistRecord.getPopularity(), artist.popularity());
    }

    private ArtistRecord newArtistRecord(String id) {
        var artistRecord = new ArtistRecord();
        artistRecord.setId(id);
        artistRecord.setName("name-" + id);
        artistRecord.setImageHref("image-" + id);
        artistRecord.setPopularity(50);
        return artistRecord;
    }
}
