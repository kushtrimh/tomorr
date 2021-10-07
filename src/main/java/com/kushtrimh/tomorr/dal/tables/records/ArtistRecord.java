/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables.records;


import com.kushtrimh.tomorr.dal.tables.Artist;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
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
        name = "artist",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "artist_pkey", columnNames = {"id"})
        }
)
public class ArtistRecord extends UpdatableRecordImpl<ArtistRecord> implements Record6<String, String, String, Integer, String, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.artist.id</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.artist.id</code>.
     */
    @Id
    @Column(name = "id", nullable = false, length = 32)
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.artist.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.artist.name</code>.
     */
    @Column(name = "name", nullable = false, length = 256)
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.artist.image_href</code>.
     */
    public void setImageHref(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.artist.image_href</code>.
     */
    @Column(name = "image_href", length = 256)
    public String getImageHref() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.artist.popularity</code>.
     */
    public void setPopularity(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.artist.popularity</code>.
     */
    @Column(name = "popularity", nullable = false, precision = 32)
    public Integer getPopularity() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>public.artist.sync_key</code>.
     */
    public void setSyncKey(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.artist.sync_key</code>.
     */
    @Column(name = "sync_key", length = 36)
    public String getSyncKey() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.artist.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.artist.created_at</code>.
     */
    @Column(name = "created_at", nullable = false, precision = 6)
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<String, String, String, Integer, String, LocalDateTime> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<String, String, String, Integer, String, LocalDateTime> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Artist.ARTIST.ID;
    }

    @Override
    public Field<String> field2() {
        return Artist.ARTIST.NAME;
    }

    @Override
    public Field<String> field3() {
        return Artist.ARTIST.IMAGE_HREF;
    }

    @Override
    public Field<Integer> field4() {
        return Artist.ARTIST.POPULARITY;
    }

    @Override
    public Field<String> field5() {
        return Artist.ARTIST.SYNC_KEY;
    }

    @Override
    public Field<LocalDateTime> field6() {
        return Artist.ARTIST.CREATED_AT;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public String component3() {
        return getImageHref();
    }

    @Override
    public Integer component4() {
        return getPopularity();
    }

    @Override
    public String component5() {
        return getSyncKey();
    }

    @Override
    public LocalDateTime component6() {
        return getCreatedAt();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public String value3() {
        return getImageHref();
    }

    @Override
    public Integer value4() {
        return getPopularity();
    }

    @Override
    public String value5() {
        return getSyncKey();
    }

    @Override
    public LocalDateTime value6() {
        return getCreatedAt();
    }

    @Override
    public ArtistRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public ArtistRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public ArtistRecord value3(String value) {
        setImageHref(value);
        return this;
    }

    @Override
    public ArtistRecord value4(Integer value) {
        setPopularity(value);
        return this;
    }

    @Override
    public ArtistRecord value5(String value) {
        setSyncKey(value);
        return this;
    }

    @Override
    public ArtistRecord value6(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public ArtistRecord values(String value1, String value2, String value3, Integer value4, String value5, LocalDateTime value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ArtistRecord
     */
    public ArtistRecord() {
        super(Artist.ARTIST);
    }

    /**
     * Create a detached, initialised ArtistRecord
     */
    public ArtistRecord(String id, String name, String imageHref, Integer popularity, String syncKey, LocalDateTime createdAt) {
        super(Artist.ARTIST);

        setId(id);
        setName(name);
        setImageHref(imageHref);
        setPopularity(popularity);
        setSyncKey(syncKey);
        setCreatedAt(createdAt);
    }
}
