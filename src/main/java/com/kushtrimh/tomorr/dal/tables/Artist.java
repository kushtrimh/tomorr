/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables;


import com.kushtrimh.tomorr.dal.Keys;
import com.kushtrimh.tomorr.dal.Public;
import com.kushtrimh.tomorr.dal.tables.records.ArtistRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Artist extends TableImpl<ArtistRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.artist</code>
     */
    public static final Artist ARTIST = new Artist();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ArtistRecord> getRecordType() {
        return ArtistRecord.class;
    }

    /**
     * The column <code>public.artist.id</code>.
     */
    public final TableField<ArtistRecord, String> ID = createField(DSL.name("id"), SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>public.artist.name</code>.
     */
    public final TableField<ArtistRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(256).nullable(false), this, "");

    /**
     * The column <code>public.artist.image_href</code>.
     */
    public final TableField<ArtistRecord, String> IMAGE_HREF = createField(DSL.name("image_href"), SQLDataType.VARCHAR(256), this, "");

    /**
     * The column <code>public.artist.popularity</code>.
     */
    public final TableField<ArtistRecord, Integer> POPULARITY = createField(DSL.name("popularity"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.artist.sync_key</code>.
     */
    public final TableField<ArtistRecord, String> SYNC_KEY = createField(DSL.name("sync_key"), SQLDataType.VARCHAR(36), this, "");

    /**
     * The column <code>public.artist.status</code>.
     */
    public final TableField<ArtistRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.VARCHAR(16).nullable(false), this, "");

    /**
     * The column <code>public.artist.created_at</code>.
     */
    public final TableField<ArtistRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field("now()", SQLDataType.LOCALDATETIME)), this, "");

    private Artist(Name alias, Table<ArtistRecord> aliased) {
        this(alias, aliased, null);
    }

    private Artist(Name alias, Table<ArtistRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.artist</code> table reference
     */
    public Artist(String alias) {
        this(DSL.name(alias), ARTIST);
    }

    /**
     * Create an aliased <code>public.artist</code> table reference
     */
    public Artist(Name alias) {
        this(alias, ARTIST);
    }

    /**
     * Create a <code>public.artist</code> table reference
     */
    public Artist() {
        this(DSL.name("artist"), null);
    }

    public <O extends Record> Artist(Table<O> child, ForeignKey<O, ArtistRecord> key) {
        super(child, key, ARTIST);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<ArtistRecord> getPrimaryKey() {
        return Keys.ARTIST_PKEY;
    }

    @Override
    public List<UniqueKey<ArtistRecord>> getKeys() {
        return Arrays.<UniqueKey<ArtistRecord>>asList(Keys.ARTIST_PKEY);
    }

    @Override
    public Artist as(String alias) {
        return new Artist(DSL.name(alias), this);
    }

    @Override
    public Artist as(Name alias) {
        return new Artist(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Artist rename(String name) {
        return new Artist(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Artist rename(Name name) {
        return new Artist(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<String, String, String, Integer, String, String, LocalDateTime> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
