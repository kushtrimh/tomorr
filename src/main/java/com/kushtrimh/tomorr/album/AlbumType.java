package com.kushtrimh.tomorr.album;

import java.util.Arrays;
import java.util.Optional;

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

    public static Optional<AlbumType> fromApiType(String apiType) {
        return Arrays.stream(values()).filter(type -> type.apiType.equals(apiType))
                .findFirst();
    }
}
