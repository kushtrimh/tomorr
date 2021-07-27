package com.kushtrimh.tomorr.spotify.util;

import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Kushtrim Hajrizi
 */
public class SpotifyApiUriBuilder {

    private final String apiUri;

    public SpotifyApiUriBuilder(String apiUri) {
        this.apiUri = apiUri;
    }

    public <Req extends SpotifyApiRequest> String getApiUri(Req request) {
        Objects.requireNonNull(request);

        MultiValueMap<String, String> params = request.getQueryParams();
        // params.replaceAll((key, value) -> List.of(URLEncoder.encode(String.join(",", value), StandardCharsets.UTF_8)));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .uri(URI.create(apiUri))
                .path(request.getPath())
                .queryParams(params);
        return uriBuilder.build().toUriString();
    }
}
