package com.kushtrimh.tomorr.api.v1.request;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class ArtistSearchRequest {
    @NotBlank
    private String name;
    private boolean external;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistSearchRequest that = (ArtistSearchRequest) o;
        return external == that.external && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, external);
    }

    @Override
    public String toString() {
        return "ArtistSearchRequest{" +
                "name='" + name + '\'' +
                ", external=" + external +
                '}';
    }
}
