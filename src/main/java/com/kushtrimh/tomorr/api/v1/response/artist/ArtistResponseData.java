package com.kushtrimh.tomorr.api.v1.response.artist;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class ArtistResponseData {
    private String id;
    private String name;

    public ArtistResponseData() {
    }

    public ArtistResponseData(String id, String name) {
        this.id = id;
        this.name = name;
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
                '}';
    }
}
