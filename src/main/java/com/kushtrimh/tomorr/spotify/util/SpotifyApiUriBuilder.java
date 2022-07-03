package com.kushtrimh.tomorr.spotify.util;

import com.kushtrimh.tomorr.spotify.api.request.NextNodeRequest;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
public class SpotifyApiUriBuilder {

    private final String apiUri;

    public SpotifyApiUriBuilder(String apiUri) {
        this.apiUri = apiUri;
    }

    public <Req extends SpotifyApiRequest<Res>, Res> String getApiUri(Req request) {
        Objects.requireNonNull(request);
        if (request instanceof NextNodeRequest<?> nextNode) {
            return nextNode.getUri();
        } else {
            MultiValueMap<String, String> params = request.getQueryParams();
            // params.replaceAll((key, value) -> List.of(URLEncoder.encode(String.join(",", value), StandardCharsets.UTF_8)));
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .uri(URI.create(apiUri))
                    .path(request.getPath())
                    .queryParams(params);
            return uriBuilder.build().toUriString();
        }
    }
}
