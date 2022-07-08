package com.kushtrimh.tomorr.album.repository;

import com.kushtrimh.tomorr.dal.tables.records.AlbumRecord;
import com.kushtrimh.tomorr.dal.tables.records.ArtistAlbumRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.kushtrimh.tomorr.dal.Tables.*;

/**
 * @author Kushtrim Hajrizi
 */
@Repository
public class AlbumJooqRepository implements AlbumRepository<AlbumRecord> {

    private final DSLContext create;

    public AlbumJooqRepository(DSLContext create) {
        this.create = create;
    }

    @Override
    public int count() {
        return create.selectCount().from(ALBUM).fetchOne(0, int.class);
    }

    @Override
    public AlbumRecord findById(String id) {
        return create.fetchOne(ALBUM, ALBUM.ID.eq(id));
    }

    @Override
    public Integer findCountByArtistId(String artistId) {
        return create.selectCount().from(ARTIST_ALBUM)
                .where(ARTIST_ALBUM.ARTIST_ID.eq(artistId))
                .fetchOne(0, Integer.class);
    }

    @Override
    public List<AlbumRecord> findByArtist(String artistId) {
        return create.select().from(ALBUM)
                .innerJoin(ARTIST_ALBUM)
                .on(ARTIST_ALBUM.ALBUM_ID.eq(ALBUM.ID))
                .innerJoin(ARTIST)
                .on(ARTIST.ID.eq(ARTIST_ALBUM.ARTIST_ID))
                .where(ARTIST.ID.eq(artistId))
                .fetchInto(ALBUM);
    }

    @Override
    public AlbumRecord save(String artistId, AlbumRecord album) {
        Objects.requireNonNull(album);
        var albumRecord = create.newRecord(ALBUM, album);
        create.insertInto(ALBUM)
                .set(albumRecord)
                .onDuplicateKeyIgnore()
                .execute();

        create.insertInto(ARTIST_ALBUM)
                .set(newArtistAlbumRecord(artistId, albumRecord))
                .onDuplicateKeyIgnore()
                .execute();
        return albumRecord;
    }

    @Override
    public void saveAll(String artistId, List<AlbumRecord> albums) {
        if (albums.isEmpty()) {
            return;
        }
        var batchAlbumsOperation = create.batchInsert(albums);
        batchAlbumsOperation.execute();

        List<ArtistAlbumRecord> artistAlbums = albums.stream()
                .map(album -> newArtistAlbumRecord(artistId, album))
                .toList();

        var batchArtistAlbumsOperation = create.batchInsert(artistAlbums);
        batchArtistAlbumsOperation.execute();
    }

    private ArtistAlbumRecord newArtistAlbumRecord(String artistId, AlbumRecord album) {
        var record = new ArtistAlbumRecord();
        record.setArtistId(artistId);
        record.setAlbumId(album.getId());
        return record;
    }

    @Override
    public void deleteById(String id) {
        create.delete(ALBUM)
                .where(ALBUM.ID.eq(id))
                .execute();
    }
}
