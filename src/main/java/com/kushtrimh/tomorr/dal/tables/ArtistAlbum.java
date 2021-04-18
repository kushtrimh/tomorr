/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables;


import com.kushtrimh.tomorr.dal.Public;
import com.kushtrimh.tomorr.dal.tables.records.ArtistAlbumRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ArtistAlbum extends TableImpl<ArtistAlbumRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.artist_album</code>
     */
    public static final ArtistAlbum ARTIST_ALBUM = new ArtistAlbum();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ArtistAlbumRecord> getRecordType() {
        return ArtistAlbumRecord.class;
    }

    /**
     * The column <code>public.artist_album.artist_id</code>.
     */
    public final TableField<ArtistAlbumRecord, String> ARTIST_ID = createField(DSL.name("artist_id"), SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>public.artist_album.album_id</code>.
     */
    public final TableField<ArtistAlbumRecord, String> ALBUM_ID = createField(DSL.name("album_id"), SQLDataType.VARCHAR(32).nullable(false), this, "");

    private ArtistAlbum(Name alias, Table<ArtistAlbumRecord> aliased) {
        this(alias, aliased, null);
    }

    private ArtistAlbum(Name alias, Table<ArtistAlbumRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.artist_album</code> table reference
     */
    public ArtistAlbum(String alias) {
        this(DSL.name(alias), ARTIST_ALBUM);
    }

    /**
     * Create an aliased <code>public.artist_album</code> table reference
     */
    public ArtistAlbum(Name alias) {
        this(alias, ARTIST_ALBUM);
    }

    /**
     * Create a <code>public.artist_album</code> table reference
     */
    public ArtistAlbum() {
        this(DSL.name("artist_album"), null);
    }

    public <O extends Record> ArtistAlbum(Table<O> child, ForeignKey<O, ArtistAlbumRecord> key) {
        super(child, key, ARTIST_ALBUM);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public ArtistAlbum as(String alias) {
        return new ArtistAlbum(DSL.name(alias), this);
    }

    @Override
    public ArtistAlbum as(Name alias) {
        return new ArtistAlbum(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ArtistAlbum rename(String name) {
        return new ArtistAlbum(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ArtistAlbum rename(Name name) {
        return new ArtistAlbum(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
