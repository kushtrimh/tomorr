package com.kushtrimh.tomorr.spotify.api.response.artist;

import com.kushtrimh.tomorr.spotify.api.response.album.AlbumResponseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class GetArtistAlbumsApiResponse {
    private List<AlbumResponseData> items = new ArrayList<>();
    private int limit;
    private int offset;
    private String next;
    private int total;

    public List<AlbumResponseData> getItems() {
        return items;
    }

    public void setItems(List<AlbumResponseData> items) {
        this.items = items;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetArtistAlbumsApiResponse response = (GetArtistAlbumsApiResponse) o;
        return limit == response.limit && offset == response.offset && total == response.total && Objects.equals(next, response.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, offset, next, total);
    }

    @Override
    public String toString() {
        return "GetArtistAlbumsApiResponse{" +
                "items=" + items +
                ", limit=" + limit +
                ", offset=" + offset +
                ", next='" + next + '\'' +
                ", total=" + total +
                '}';
    }
}
