/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables.records;


import com.kushtrimh.tomorr.dal.tables.AppUser;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(
        name = "app_user",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "app_user_pkey", columnNames = {"id"})
        }
)
public class AppUserRecord extends UpdatableRecordImpl<AppUserRecord> implements Record4<String, String, String, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.app_user.id</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.app_user.id</code>.
     */
    @Id
    @Column(name = "id", nullable = false, length = 32)
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.app_user.address</code>.
     */
    public void setAddress(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.app_user.address</code>.
     */
    @Column(name = "address", nullable = false, length = 1024)
    public String getAddress() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.app_user.type</code>.
     */
    public void setType(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.app_user.type</code>.
     */
    @Column(name = "type", nullable = false, length = 64)
    public String getType() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.app_user.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.app_user.created_at</code>.
     */
    @Column(name = "created_at", nullable = false, precision = 6)
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, String, LocalDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, String, LocalDateTime> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return AppUser.APP_USER.ID;
    }

    @Override
    public Field<String> field2() {
        return AppUser.APP_USER.ADDRESS;
    }

    @Override
    public Field<String> field3() {
        return AppUser.APP_USER.TYPE;
    }

    @Override
    public Field<LocalDateTime> field4() {
        return AppUser.APP_USER.CREATED_AT;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getAddress();
    }

    @Override
    public String component3() {
        return getType();
    }

    @Override
    public LocalDateTime component4() {
        return getCreatedAt();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getAddress();
    }

    @Override
    public String value3() {
        return getType();
    }

    @Override
    public LocalDateTime value4() {
        return getCreatedAt();
    }

    @Override
    public AppUserRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public AppUserRecord value2(String value) {
        setAddress(value);
        return this;
    }

    @Override
    public AppUserRecord value3(String value) {
        setType(value);
        return this;
    }

    @Override
    public AppUserRecord value4(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public AppUserRecord values(String value1, String value2, String value3, LocalDateTime value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AppUserRecord
     */
    public AppUserRecord() {
        super(AppUser.APP_USER);
    }

    /**
     * Create a detached, initialised AppUserRecord
     */
    public AppUserRecord(String id, String address, String type, LocalDateTime createdAt) {
        super(AppUser.APP_USER);

        setId(id);
        setAddress(address);
        setType(type);
        setCreatedAt(createdAt);
    }
}
