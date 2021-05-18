package com.kushtrimh.tomorr.spotify.api.response.album;

import com.kushtrimh.tomorr.spotify.api.response.ImageResponseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class AlbumResponseData {
    private String id;
    private String albumGroup;
    private String albumType;
    private String name;
    private String type;
    private String releaseDate;
    private List<ImageResponseData> images = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumGroup() {
        return albumGroup;
    }

    public void setAlbumGroup(String albumGroup) {
        this.albumGroup = albumGroup;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<ImageResponseData> getImages() {
        return images;
    }

    public void setImages(List<ImageResponseData> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumResponseData that = (AlbumResponseData) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(releaseDate, that.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, releaseDate);
    }

    @Override
    public String toString() {
        return "AlbumResponseData{" +
                "id='" + id + '\'' +
                ", albumGroup='" + albumGroup + '\'' +
                ", albumType='" + albumType + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", images=" + images +
                '}';
    }
}
