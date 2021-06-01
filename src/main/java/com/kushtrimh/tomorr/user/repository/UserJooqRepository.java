package com.kushtrimh.tomorr.user.repository;

import com.kushtrimh.tomorr.dal.tables.records.AppUserRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.kushtrimh.tomorr.dal.Tables.APP_USER;

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
    public void follow(String userId, String artistId) {

    }

    @Override
    public boolean followExists(String userId, String artistId) {
        return false;
    }
}
