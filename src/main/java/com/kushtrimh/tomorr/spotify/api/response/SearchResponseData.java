package com.kushtrimh.tomorr.spotify.api.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class SearchResponseData<T> {
    private List<T> items = new ArrayList<>();
    private int limit;
    private int offset;
    private String next;
    private String previous;
    private int total;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
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

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
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
        SearchResponseData<?> that = (SearchResponseData<?>) o;
        return limit == that.limit && offset == that.offset && total == that.total && Objects.equals(next, that.next) && Objects.equals(previous, that.previous);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, offset, next, previous, total);
    }

    @Override
    public String toString() {
        return "SearchResponseData{" +
                "items=" + items +
                ", limit=" + limit +
                ", offset=" + offset +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", total=" + total +
                '}';
    }
}
