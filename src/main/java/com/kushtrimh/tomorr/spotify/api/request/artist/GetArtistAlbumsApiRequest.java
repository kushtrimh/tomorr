package com.kushtrimh.tomorr.spotify.api.request.artist;

import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
public class GetArtistAlbumsApiRequest implements SpotifyApiRequest {
    private String artistId;
    private List<String> includeGroups;
    private String market;
    private int limit;
    private int offset;

    private GetArtistAlbumsApiRequest(Builder builder) {
        this.artistId = builder.artistId;
        this.includeGroups = builder.includeGroups;
        this.market = builder.market;
        this.limit = builder.limit;
        this.offset = builder.offset;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public List<String> getIncludeGroups() {
        return includeGroups;
    }

    public void setIncludeGroups(List<String> includeGroups) {
        this.includeGroups = includeGroups;
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
        return "/artists/" + artistId + "/albums";
    }

    @Override
    public MultiValueMap<String, String> getQueryParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (!includeGroups.isEmpty()) {
            params.add("include_groups", String.join(",", includeGroups));
        }
        if (!market.isBlank()) {
            params.add("market", market);
        }
        params.add("limit", Integer.toString(limit));
        params.add("offset", Integer.toString(offset));
        return params;
    }

    @Override
    public String toString() {
        return "GetArtistAlbumsApiRequest{" +
                "artistId='" + artistId + '\'' +
                ", includeGroups='" + includeGroups + '\'' +
                ", market='" + market + '\'' +
                ", limit='" + limit + '\'' +
                ", offset='" + offset + '\'' +
                '}';
    }

    public static class Builder {
        private final String artistId;
        private List<String> includeGroups = List.of("album", "single");
        private String market = "";
        private int limit = 50;
        private int offset = 0;

        public Builder(String artistId) {
            this.artistId = artistId;
        }

        public Builder includeGroups(List<String> includeGroups) {
            this.includeGroups = includeGroups;
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

        public GetArtistAlbumsApiRequest build() {
            return new GetArtistAlbumsApiRequest(this);
        }
    }
}
