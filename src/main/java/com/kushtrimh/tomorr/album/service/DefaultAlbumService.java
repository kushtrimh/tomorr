package com.kushtrimh.tomorr.album.service;

import com.kushtrimh.tomorr.album.Album;
import com.kushtrimh.tomorr.album.AlbumType;
import com.kushtrimh.tomorr.album.repository.AlbumRepository;
import com.kushtrimh.tomorr.dal.tables.records.AlbumRecord;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultAlbumService implements AlbumService {

    private final AlbumRepository<AlbumRecord> albumRepository;

    public DefaultAlbumService(AlbumRepository<AlbumRecord> albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Transactional
    @Override
    public Optional<Album> findById(String id) {
        AlbumRecord record = albumRepository.findById(id);
        if (record == null) {
            return Optional.empty();
        }
        return Optional.of(toAlbum(record));
    }

    @Override
    @Cacheable(value = "artistAlbumsCount", key = "'albumsCount:' + #artistId", unless = "#result != null")
    public Optional<Integer> findCountByArtistId(String artistId) {
        return Optional.ofNullable(albumRepository.findCountByArtistId(artistId));
    }

    @Transactional
    @Override
    public List<Album> findByArtist(String artistId) {
        List<AlbumRecord> records = albumRepository.findByArtist(artistId);
        return records.stream().map(this::toAlbum).toList();
    }

    @Transactional
    @Override
    @CacheEvict(value = "artistAlbumsCount", key = "'albumsCount:' + #artistId")
    public void save(String artistId, Album album) {
        Objects.requireNonNull(album);
        Objects.requireNonNull(artistId);
        albumRepository.save(artistId, toAlbumRecord(album));
    }

    @Transactional
    @Override
    @CacheEvict(value = "artistAlbumsCount", key = "'albumsCount:' + #artistId")
    public void saveAll(String artistId, List<Album> albums) {
        Objects.requireNonNull(artistId);
        if (albums == null || albums.isEmpty()) {
            return;
        }
        albumRepository.saveAll(artistId, albums.stream().map(this::toAlbumRecord).toList());
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        albumRepository.deleteById(id);
    }

    private Album toAlbum(AlbumRecord albumRecord) {
        return new Album(albumRecord.getId(),
                albumRecord.getName(),
                AlbumType.valueOf(albumRecord.getType()),
                albumRecord.getTotalTracks(),
                albumRecord.getReleaseDate(),
                albumRecord.getImageHref());
    }

    private AlbumRecord toAlbumRecord(Album album) {
        var albumRecord = new AlbumRecord();
        albumRecord.setId(album.id());
        albumRecord.setName(album.name());
        albumRecord.setType(album.albumType().name());
        albumRecord.setTotalTracks(album.totalTracks());
        albumRecord.setReleaseDate(album.releasedDate());
        albumRecord.setImageHref(album.imageHref());
        return albumRecord;
    }
}
