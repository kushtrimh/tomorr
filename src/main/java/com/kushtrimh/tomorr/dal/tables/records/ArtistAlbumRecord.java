/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables.records;


import com.kushtrimh.tomorr.dal.tables.ArtistAlbum;
import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.TableRecordImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(
        name = "artist_album",
        schema = "public"
)
public class ArtistAlbumRecord extends TableRecordImpl<ArtistAlbumRecord> implements Record3<String, String, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.artist_album.artist_id</code>.
     */
    public void setArtistId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.artist_album.artist_id</code>.
     */
    @Column(name = "artist_id", nullable = false, length = 32)
    public String getArtistId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.artist_album.album_id</code>.
     */
    public void setAlbumId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.artist_album.album_id</code>.
     */
    @Column(name = "album_id", nullable = false, length = 32)
    public String getAlbumId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.artist_album.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.artist_album.created_at</code>.
     */
    @Column(name = "created_at", nullable = false, precision = 6)
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(2);
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, LocalDateTime> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<String, String, LocalDateTime> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return ArtistAlbum.ARTIST_ALBUM.ARTIST_ID;
    }

    @Override
    public Field<String> field2() {
        return ArtistAlbum.ARTIST_ALBUM.ALBUM_ID;
    }

    @Override
    public Field<LocalDateTime> field3() {
        return ArtistAlbum.ARTIST_ALBUM.CREATED_AT;
    }

    @Override
    public String component1() {
        return getArtistId();
    }

    @Override
    public String component2() {
        return getAlbumId();
    }

    @Override
    public LocalDateTime component3() {
        return getCreatedAt();
    }

    @Override
    public String value1() {
        return getArtistId();
    }

    @Override
    public String value2() {
        return getAlbumId();
    }

    @Override
    public LocalDateTime value3() {
        return getCreatedAt();
    }

    @Override
    public ArtistAlbumRecord value1(String value) {
        setArtistId(value);
        return this;
    }

    @Override
    public ArtistAlbumRecord value2(String value) {
        setAlbumId(value);
        return this;
    }

    @Override
    public ArtistAlbumRecord value3(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public ArtistAlbumRecord values(String value1, String value2, LocalDateTime value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ArtistAlbumRecord
     */
    public ArtistAlbumRecord() {
        super(ArtistAlbum.ARTIST_ALBUM);
    }

    /**
     * Create a detached, initialised ArtistAlbumRecord
     */
    public ArtistAlbumRecord(String artistId, String albumId, LocalDateTime createdAt) {
        super(ArtistAlbum.ARTIST_ALBUM);

        setArtistId(artistId);
        setAlbumId(albumId);
        setCreatedAt(createdAt);
    }
}
