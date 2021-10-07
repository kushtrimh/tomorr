-- Setup

CREATE TABLE artist
(
    id         varchar(32) PRIMARY KEY,
    name       varchar(256) NOT NULL,
    image_href varchar(256),
    popularity integer      NOT NULL,
    sync_key   varchar(36),
    created_at timestamp    NOT NULL DEFAULT NOW()
);

CREATE TABLE app_user
(
    id         varchar(32) PRIMARY KEY,
    address    varchar(1024) NOT NULL,
    type       varchar(64)   NOT NULL,
    created_at timestamp     NOT NULL DEFAULT NOW()
);

CREATE TABLE artist_app_user
(
    artist_id   varchar(32) NOT NULL,
    app_user_id varchar(32) NOT NULL,
    PRIMARY KEY (artist_id, app_user_id)
);

CREATE TABLE album
(
    id           varchar(32) PRIMARY KEY,
    name         varchar(512) NOT NULL,
    total_tracks integer      NOT NULL,
    image_href   varchar(256) NOT NULL,
    type         varchar(32)  NOT NULL,
    release_date varchar(64)  NOT NULL,
    created_at   timestamp    NOT NULL DEFAULT NOW()
);

CREATE TABLE artist_album
(
    artist_id  varchar(32) NOT NULL,
    album_id   varchar(32) NOT NULL,
    created_at timestamp   NOT NULL DEFAULT NOW()
);

-- Seed

INSERT INTO artist (id, name, image_href, popularity)
VALUES ('artist1', 'Artist One', 'artist-one-image', 50);
INSERT INTO artist (id, name, image_href, popularity)
VALUES ('artist2', 'Artist Two', 'artist-two-image', 95);
INSERT INTO artist (id, name, image_href, popularity)
VALUES ('artist3', 'Artist Three', 'artist-three-image', 100);

INSERT INTO app_user (id, address, type)
VALUES ('user1', 'user1@tomorr.com', 'EMAIL');
INSERT INTO app_user (id, address, type)
VALUES ('user2', 'user2@tomorr.com', 'EMAIL');

INSERT INTO artist_app_user (artist_id, app_user_id)
VALUES ('artist1', 'user1');
INSERT INTO artist_app_user (artist_id, app_user_id)
VALUES ('artist3', 'user1');
INSERT INTO artist_app_user (artist_id, app_user_id)
VALUES ('artist1', 'user2');

INSERT INTO album (id, name, total_tracks, image_href, type, release_date)
VALUES ('album1', 'Album 1 Name', 8, 'album-one-image', 'ALBUM', '2021-01-15');
INSERT INTO album (id, name, total_tracks, image_href, type, release_date)
VALUES ('album2', 'Album 2 Name', 5, 'album-two-image', 'ALBUM', '2020-05-21');
INSERT INTO album (id, name, total_tracks, image_href, type, release_date)
VALUES ('album3', 'Album 3 Name', 12, 'album-three-image', 'ALBUM', '2015-03-05');

INSERT INTO artist_album (artist_id, album_id)
VALUES ('artist1', 'album1');
INSERT INTO artist_album (artist_id, album_id)
VALUES ('artist2', 'album1');
INSERT INTO artist_album (artist_id, album_id)
VALUES ('artist3', 'album1');
INSERT INTO artist_album (artist_id, album_id)
VALUES ('artist2', 'album2');
INSERT INTO artist_album (artist_id, album_id)
VALUES ('artist3', 'album3');