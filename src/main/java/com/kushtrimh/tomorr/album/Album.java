package com.kushtrimh.tomorr.album;

/**
 * @author Kushtrim Hajrizi
 */
public record Album(String id,
                    String name,
                    AlbumType albumType,
                    int totalTracks,
                    String releasedDate,
                    String imageHref) {
}
