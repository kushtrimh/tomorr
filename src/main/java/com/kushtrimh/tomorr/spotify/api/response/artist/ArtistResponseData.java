package com.kushtrimh.tomorr.spotify.api.response.artist;

import com.kushtrimh.tomorr.spotify.api.response.ImageResponseData;

import java.util.List;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class ArtistResponseData {
    private String id;
    private String name;
    private List<ImageResponseData> images;
    private int popularity;

    public ArtistResponseData() {
    }

    public ArtistResponseData(String id, String name, List<ImageResponseData> images, int popularity) {
        this.id = id;
        this.name = name;
        this.images = images;
        this.popularity = popularity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ImageResponseData> getImages() {
        return images;
    }

    public void setImages(List<ImageResponseData> images) {
        this.images = images;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistResponseData that = (ArtistResponseData) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "ArtistResponseData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", images=" + images +
                ", popularity=" + popularity +
                '}';
    }
}
