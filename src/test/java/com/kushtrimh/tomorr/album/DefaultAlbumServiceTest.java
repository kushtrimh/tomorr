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
    public void findCountByArtistId_WhenArtistDoesNotHaveAlbums_ReturnEmptyOptional() {
        var artistId = "invalid-id";
        when(albumRepository.findCountByArtistId(artistId)).thenReturn(null);
        Optional<Integer> count = albumService.findCountByArtistId(artistId);
        assertTrue(count.isEmpty());
    }

    @Test
    public void findCountByArtistId_WhenArtistHasAlbums_ReturnArtistAlbumsCount() {
        var artistId = "artist1";
        var count = 10;
        when(albumRepository.findCountByArtistId(artistId)).thenReturn(count);
        Optional<Integer> albumCount = albumService.findCountByArtistId(artistId);
        assertAll(
                () -> assertFalse(albumCount.isEmpty()),
                () -> assertEquals(count, albumCount.get())
        );
    }

    @Test
    public void save_WhenAlbumIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> {
            albumService.save("artist1", null);
        });
    }

    @Test
    public void save_WhenArtistIdIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> {
            albumService.save(null, newAlbum(newAlbumRecord("id1")));
        });
    }

    @Test
    public void save_WhenAlbumIsValid_SaveSuccessfully() {
        var record = newAlbumRecord("id100");
        var artistId = "artist1";
        var album = newAlbum(record);
        albumService.save(artistId, album);
        verify(albumRepository, times(1)).save(artistId, record);
    }

    @Test
    public void saveAll_WhenAlbumsListIsNull_DoNothing() {
        albumService.saveAll("artist1", null);
        verify(albumRepository, times(0)).saveAll(any(), any());
    }

    @Test
    public void saveAll_WhenAlbumsListIsEmpty_DoNothing() {
        albumService.saveAll("artist1", List.of());
        verify(albumRepository, times(0)).saveAll(any(), any());
    }

    @Test
    public void saveAll_WhenArtistIdIsNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> {
            albumService.saveAll(null, List.of(newAlbum(newAlbumRecord("id1"))));
        });
    }

    @Test
    public void saveAll_WhenAlbumsListIsFilled_SaveAlbumsSuccessfully() {
        var artistId = "artist1";
        var records = List.of(newAlbumRecord("id1"), newAlbumRecord("id2"));
        var albums = records.stream().map(this::newAlbum).toList();
        albumService.saveAll(artistId, albums);
        verify(albumRepository, times(1)).saveAll(artistId, records);
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

    private Album newAlbum(AlbumRecord record) {
        return new Album(
                record.getId(),
                record.getName(),
                AlbumType.valueOf(record.getType()),
                record.getTotalTracks(),
                record.getReleaseDate(),
                record.getImageHref()
        );
    }
}
