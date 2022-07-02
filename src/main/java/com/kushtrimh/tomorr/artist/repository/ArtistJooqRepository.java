package com.kushtrimh.tomorr.artist.repository;

import com.kushtrimh.tomorr.artist.ArtistStatus;
import com.kushtrimh.tomorr.dal.tables.records.ArtistRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.kushtrimh.tomorr.dal.Tables.*;

/**
 * @author Kushtrim Hajrizi
 */
@Repository
public class ArtistJooqRepository implements ArtistRepository<ArtistRecord> {

    private final DSLContext create;

    public ArtistJooqRepository(DSLContext create) {
        this.create = create;
    }

    @Override
    public int count() {
        return create.selectCount().from(ARTIST).fetchOne(0, int.class);
    }

    @Override
    public ArtistRecord findById(String id) {
        return create.fetchOne(ARTIST, ARTIST.ID.eq(id));
    }

    @Override
    public ArtistRecord findByName(String name) {
        return create.fetchOne(ARTIST, ARTIST.NAME.eq(name));
    }

    @Override
    public List<ArtistRecord> findToSync(String syncKey, int count) {
        List<ArtistRecord> artists = create
                .select()
                .from(ARTIST)
                .where(ARTIST.SYNC_KEY.ne(syncKey).or(ARTIST.SYNC_KEY.isNull()))
                .and(ARTIST.STATUS.eq(ArtistStatus.ACTIVE.name()))
                .andExists(create.selectOne().from(ARTIST_ALBUM).where(ARTIST_ALBUM.ARTIST_ID.eq(ARTIST.ID)))
                .limit(count)
                .forUpdate().fetchInto(ARTIST);
        artists.forEach(record -> record.setSyncKey(syncKey));
        if (!artists.isEmpty()) {
            create.batchUpdate(artists).execute();
        }
        return artists;
    }

    @Override
    public List<ArtistRecord> searchByName(String name) {
        return create.fetch(ARTIST, ARTIST.NAME.startsWithIgnoreCase(name));
    }

    @Override
    public ArtistRecord save(ArtistRecord artist) {
        Objects.requireNonNull(artist);
        var record = create.newRecord(ARTIST, artist);
        record.store();
        return record;
    }

    @Override
    public void deleteById(String id) {
        create.delete(ARTIST)
                .where(ARTIST.ID.eq(id))
                .execute();
    }
}
