package com.kushtrimh.tomorr.spotify.api.response;

import com.kushtrimh.tomorr.spotify.api.response.album.AlbumResponseData;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class SearchTrackResponseData {
    private AlbumResponseData album;

    public AlbumResponseData getAlbum() {
        return album;
    }

    public void setAlbum(AlbumResponseData album) {
        this.album = album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchTrackResponseData that = (SearchTrackResponseData) o;
        return Objects.equals(album, that.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(album);
    }

    @Override
    public String toString() {
        return "SearchTrackResponseData{" +
                "album=" + album +
                '}';
    }
}
