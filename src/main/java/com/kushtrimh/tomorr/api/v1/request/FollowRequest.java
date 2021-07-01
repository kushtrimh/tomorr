package com.kushtrimh.tomorr.api.v1.request;

import javax.validation.constraints.NotBlank;

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
}
