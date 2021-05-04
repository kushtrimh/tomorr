package com.kushtrimh.tomorr.user.repository;

import static com.kushtrimh.tomorr.dal.Tables.*;

import com.kushtrimh.tomorr.dal.tables.records.AppUserRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Objects;

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
    public void save(AppUserRecord user) {
        Objects.requireNonNull(user);
        var record = create.newRecord(APP_USER, user);
        record.store();
    }

    @Override
    public void deleteById(String id) {
        create.delete(APP_USER).where(APP_USER.ID.eq(id)).execute();
    }
}
