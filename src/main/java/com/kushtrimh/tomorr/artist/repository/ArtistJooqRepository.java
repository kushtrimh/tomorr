package com.kushtrimh.tomorr.artist.repository;

import static com.kushtrimh.tomorr.dal.Tables.*;

import com.kushtrimh.tomorr.dal.tables.records.ArtistRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

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
    public List<ArtistRecord> searchByName(String name) {
        return create.fetch(ARTIST, ARTIST.NAME.startsWithIgnoreCase(name));
    }

    @Override
    public void save(ArtistRecord artist) {
        Objects.requireNonNull(artist);
        var record = create.newRecord(ARTIST, artist);
        record.store();
    }

    @Override
    public void deleteById(String id) {
        create.delete(ARTIST)
                .where(ARTIST.ID.eq(id))
                .execute();
    }
}
