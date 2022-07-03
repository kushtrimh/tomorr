package com.kushtrimh.tomorr.artist.service;

import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.ArtistStatus;
import com.kushtrimh.tomorr.artist.repository.ArtistRepository;
import com.kushtrimh.tomorr.dal.tables.records.ArtistRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultArtistService implements ArtistService {

    private final ArtistRepository<ArtistRecord> artistRepository;

    public DefaultArtistService(ArtistRepository<ArtistRecord> artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Transactional
    @Override
    public Optional<Artist> findById(String id) {
        ArtistRecord record = artistRepository.findById(id);
        return getArtistOptional(record);
    }

    @Transactional
    @Override
    public Optional<Artist> findByName(String name) {
        ArtistRecord record = artistRepository.findByName(name);
        return getArtistOptional(record);
    }

    @Transactional
    @Override
    public List<Artist> findToSync(String syncKey, int count) {
        Objects.requireNonNull(syncKey);
        return artistRepository.findToSync(syncKey, count)
                .stream().map(this::toArtist).toList();
    }

    @Transactional
    @Override
    public List<Artist> searchByName(String name) {
        List<ArtistRecord> records = artistRepository.searchByName(name);
        if (records.isEmpty()) {
            return Collections.emptyList();
        }
        return records.stream().map(this::toArtist).toList();
    }

    @Transactional
    @Override
    public void save(Artist artist) {
        Objects.requireNonNull(artist);
        ArtistRecord record = toArtistRecord(artist);
        record.setStatus(ArtistStatus.INITIAL_SYNC.name());
        artistRepository.save(record);
    }

    @Transactional
    @Override
    public void activateArtist(String artistId) {
        Objects.requireNonNull(artistId);
        artistRepository.activateArtist(artistId);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        artistRepository.deleteById(id);
    }

    @Transactional
    @Override
    public boolean exists(String id) {
        return findById(id).isPresent();
    }

    private Optional<Artist> getArtistOptional(ArtistRecord artistRecord) {
        if (artistRecord == null) {
            return Optional.empty();
        }
        return Optional.of(toArtist(artistRecord));
    }

    private Artist toArtist(ArtistRecord record) {
        return new Artist(record.getId(),
                record.getName(),
                record.getImageHref(),
                record.getPopularity());
    }

    private ArtistRecord toArtistRecord(Artist artist) {
        var record = new ArtistRecord();
        record.setId(artist.id());
        record.setName(artist.name());
        record.setImageHref(artist.imageHref());
        record.setPopularity(artist.popularity());
        return record;
    }
}
