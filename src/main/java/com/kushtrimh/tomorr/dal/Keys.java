/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal;


import com.kushtrimh.tomorr.dal.tables.Album;
import com.kushtrimh.tomorr.dal.tables.AppUser;
import com.kushtrimh.tomorr.dal.tables.Artist;
import com.kushtrimh.tomorr.dal.tables.ArtistAppUser;
import com.kushtrimh.tomorr.dal.tables.records.AlbumRecord;
import com.kushtrimh.tomorr.dal.tables.records.AppUserRecord;
import com.kushtrimh.tomorr.dal.tables.records.ArtistAppUserRecord;
import com.kushtrimh.tomorr.dal.tables.records.ArtistRecord;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AlbumRecord> ALBUM_PKEY = Internal.createUniqueKey(Album.ALBUM, DSL.name("album_pkey"), new TableField[] { Album.ALBUM.ID }, true);
    public static final UniqueKey<AppUserRecord> APP_USER_PKEY = Internal.createUniqueKey(AppUser.APP_USER, DSL.name("app_user_pkey"), new TableField[] { AppUser.APP_USER.ID }, true);
    public static final UniqueKey<ArtistRecord> ARTIST_PKEY = Internal.createUniqueKey(Artist.ARTIST, DSL.name("artist_pkey"), new TableField[] { Artist.ARTIST.ID }, true);
    public static final UniqueKey<ArtistAppUserRecord> ARTIST_APP_USER_PKEY = Internal.createUniqueKey(ArtistAppUser.ARTIST_APP_USER, DSL.name("artist_app_user_pkey"), new TableField[] { ArtistAppUser.ARTIST_APP_USER.ARTIST_ID, ArtistAppUser.ARTIST_APP_USER.APP_USER_ID }, true);
}
