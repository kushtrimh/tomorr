package com.kushtrimh.tomorr.spotify.api.request.artist;

import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class SearchApiRequest implements SpotifyApiRequest {
    private String query;
    private List<String> types = new ArrayList<>();
    private String market;
    private int limit;
    private int offset;

    public SearchApiRequest() {
    }

    public SearchApiRequest(Builder builder) {
        this.query = builder.query;
        this.types = builder.types;
        this.market = builder.market;
        this.limit = builder.limit;
        this.offset = builder.offset;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
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

    @Override
    public String getPath() {
        return "/search";
    }

    @Override
    public MultiValueMap<String, String> getQueryParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("q", query);
        params.add("limit", Integer.toString(limit));
        params.add("offset", Integer.toString(offset));
        if (!types.isEmpty()) {
            params.add("type", String.join(",", types));
        }
        if (market != null && !market.isBlank()) {
            params.add("market", market);
        }
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchApiRequest that = (SearchApiRequest) o;
        return limit == that.limit && offset == that.offset && Objects.equals(query, that.query)
                && Objects.equals(types, that.types) && Objects.equals(market, that.market);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, types, market, limit, offset);
    }

    @Override
    public String toString() {
        return "SearchApiRequest{" +
                "query='" + query + '\'' +
                ", type='" + types + '\'' +
                ", market='" + market + '\'' +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }

    public static class Builder {
        private final String query;
        private List<String> types = new ArrayList<>();
        private String market;
        private int limit = 50;
        private int offset;

        public Builder(String query) {
            this.query = query;
        }

        public Builder types(List<String> types) {
            this.types = types;
            return this;
        }

        public Builder market(String market) {
            this.market = market;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public SearchApiRequest build() {
            return new SearchApiRequest(this);
        }
    }
}
