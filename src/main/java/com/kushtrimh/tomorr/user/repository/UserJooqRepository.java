package com.kushtrimh.tomorr.user.repository;

import com.kushtrimh.tomorr.dal.tables.records.AppUserRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.kushtrimh.tomorr.dal.Tables.*;

/**
 * @author Kushtrim Hajrizi
 */
@Repository
public class UserJooqRepository implements UserRepository<AppUserRecord> {

    private final DSLContext create;

    public UserJooqRepository(DSLContext create) {
        this.create = create;
    }

    @Override
    public int count() {
        return create.selectCount().from(APP_USER).fetchOne(0, int.class);
    }

    @Override
    public AppUserRecord findById(String id) {
        return create.fetchOne(APP_USER, APP_USER.ID.eq(id));
    }

    @Override
    public AppUserRecord findByAddress(String address) {
        return create.fetchOne(APP_USER, APP_USER.ADDRESS.eq(address));
    }

    @Override
    public List<AppUserRecord> findByFollowedArtist(String artistId) {
        return create.select().from(APP_USER)
                .innerJoin(ARTIST_APP_USER).on(ARTIST_APP_USER.APP_USER_ID.eq(APP_USER.ID))
                .where(ARTIST_APP_USER.ARTIST_ID.eq(artistId))
                .fetchInto(APP_USER);
    }

    @Override
    public AppUserRecord save(AppUserRecord user) {
        Objects.requireNonNull(user);
        var record = create.newRecord(APP_USER, user);
        record.store();
        return record;
    }

    @Override
    public void deleteById(String id) {
        create.delete(APP_USER).where(APP_USER.ID.eq(id)).execute();
    }

    @Override
    public void associate(String userId, String artistId) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(artistId);
        var record = create.newRecord(ARTIST_APP_USER);
        record.setAppUserId(userId);
        record.setArtistId(artistId);
        record.store();
    }

    @Override
    public boolean associationExists(String userId, String artistId) {
        return create.fetchExists(create.selectOne()
                .from(ARTIST_APP_USER)
                .where(ARTIST_APP_USER.APP_USER_ID.eq(userId).and(ARTIST_APP_USER.ARTIST_ID.eq(artistId))));
    }
}
