package com.kushtrimh.tomorr.album;

import com.kushtrimh.tomorr.album.repository.AlbumRepository;
import com.kushtrimh.tomorr.album.service.AlbumService;
import com.kushtrimh.tomorr.album.service.DefaultAlbumService;
import com.kushtrimh.tomorr.dal.tables.records.AlbumRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith(MockitoExtension.class)
public class DefaultAlbumServiceTest {

    @Mock
    private AlbumRepository<AlbumRecord> albumRepository;

    private AlbumService albumService;

    @BeforeEach
    public void init() {
        albumService = new DefaultAlbumService(albumRepository);
    }

    @Test
    public void findById_WhenRecordDoesNotExist_ReturnEmptyOptional() {
        var id = "invalid-id";
        when(albumRepository.findById(id)).thenReturn(null);
        Optional<Album> album = albumService.findById("invalid-id");
        assertTrue(album.isEmpty());
    }

    @Test
    public void findById_WhenRecordDoesExist_ReturnRecordInsideOptional() {
        var id = "id1";
        var record = newAlbumRecord(id);
        when(albumRepository.findById(id)).thenReturn(record);
        Optional<Album> albumOpt = albumService.findById(id);
        assertFalse(albumOpt.isEmpty());
        compareAlbumRecordAndAlbum(record, albumOpt.get());
    }

    @Test
    public void findByArtist_WhenArtistDoesNotHaveAlbums_ReturnEmptyList() {
        var artistId = "invalid-id";
        when(albumRepository.findByArtist(artistId)).thenReturn(List.of());
        List<Album> albums = albumService.findByArtist(artistId);
        assertEquals(0, albums.size());
    }

    @Test
    public void findByArtist_WhenArtistHasAlbums_ReturnArtistAlbums() {
        var artistId = "artist1";
        List<AlbumRecord> records = List.of(newAlbumRecord("id1"),
                newAlbumRecord("id2"),
                newAlbumRecord("id3"),
                newAlbumRecord("id10"));
        when(albumRepository.findByArtist(artistId)).thenReturn(records);
        List<Album> albums = albumService.findByArtist(artistId);
        assertEquals(4, albums.size());
        for (int i = 0; i < albums.size(); i++) {
            compareAlbumRecordAndAlbum(records.get(i), albums.get(i));
        }
    }

    @Test
    public void save_WhenAlbumIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> {
            albumService.save(null);
        });
    }

    @Test
    public void save_WhenAlbumIsValid_SaveSuccessfully() {
        var record = newAlbumRecord("id100");
        var album = new Album(
                record.getId(),
                record.getName(),
                AlbumType.valueOf(record.getType()),
                record.getTotalTracks(),
                record.getReleaseDate(),
                record.getImageHref()
        );
        albumService.save(album);
        verify(albumRepository, times(1)).save(record);
    }

    @Test
    public void deleteById_WhenIdIsValid_DeleteSuccessfully() {
        var id = "id142";
        albumService.deleteById(id);
        verify(albumRepository, times(1)).deleteById(id);
    }

    private void compareAlbumRecordAndAlbum(AlbumRecord record, Album album) {
        assertEquals(record.getId(), album.id());
        assertEquals(record.getName(), album.name());
        assertEquals(record.getType(), album.albumType().name());
        assertEquals(record.getTotalTracks(), album.totalTracks());
        assertEquals(record.getImageHref(), album.imageHref());
        assertEquals(record.getReleaseDate(), album.releasedDate());
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
