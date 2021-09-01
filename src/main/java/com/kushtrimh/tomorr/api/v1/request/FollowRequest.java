package com.kushtrimh.tomorr.api.v1.request;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class FollowRequest {
    @NotBlank
    private String user;
    @NotBlank
    private String artistId;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowRequest that = (FollowRequest) o;
        return Objects.equals(user, that.user) && Objects.equals(artistId, that.artistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, artistId);
    }

    @Override
    public String toString() {
        return "FollowRequest{" +
                "user='" + user + '\'' +
                ", artistId='" + artistId + '\'' +
                '}';
    }
}
