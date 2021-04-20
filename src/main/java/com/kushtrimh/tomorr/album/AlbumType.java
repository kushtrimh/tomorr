package com.kushtrimh.tomorr.album;

/**
 * @author Kushtrim Hajrizi
 */
public enum AlbumType {
    ALBUM("album"),
    SINGLE("single"),
    COMPILATION("compilation");

    private final String apiType;

    AlbumType(String apiType) {
        this.apiType = apiType;
    }

    public String getApiType() {
        return apiType;
    }
}
