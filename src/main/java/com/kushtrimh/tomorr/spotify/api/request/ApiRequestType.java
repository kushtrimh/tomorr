package com.kushtrimh.tomorr.spotify.api.request;

/**
 * @author Kushtrim Hajrizi
 */
public enum ApiRequestType {
    ARTISTS("/artists");

    private final String path;

    private ApiRequestType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
