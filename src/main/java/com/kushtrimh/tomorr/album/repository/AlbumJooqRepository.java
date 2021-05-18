package com.kushtrimh.tomorr.album.repository;

import com.kushtrimh.tomorr.dal.tables.records.AlbumRecord;
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
    public void save(AlbumRecord album) {
        Objects.requireNonNull(album);
        var record = create.newRecord(ALBUM, album);
        record.store();
    }

    @Override
    public void deleteById(String id) {
        create.delete(ALBUM)
                .where(ALBUM.ID.eq(id))
                .execute();
    }
}
