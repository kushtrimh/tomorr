package com.kushtrimh.tomorr.artist;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record Artist(
        String id,
        String name,
        String imageHref,
        int popularity) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(id, artist.id) && Objects.equals(name, artist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
