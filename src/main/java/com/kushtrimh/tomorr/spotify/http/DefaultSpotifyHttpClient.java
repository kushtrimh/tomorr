package com.kushtrimh.tomorr.spotify.http;

import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.SpotifyApiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
public class DefaultSpotifyHttpClient implements SpotifyHttpClient {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    private DefaultSpotifyHttpClient(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.restTemplate = builder.restTemplate;
    }

    @Override
    public <T extends SpotifyApiResponse> T get(String path, Class<T> cls)
            throws SpotifyApiException, TooManyRequestsException {
        return execute(getUrl(path), HttpMethod.GET, null, cls);
    }

    @Override
    public <Req extends SpotifyApiRequest, Res extends SpotifyApiResponse> Res post(String path, Req body, Class<Res> cls)
            throws SpotifyApiException, TooManyRequestsException {
        return execute(getUrl(path), HttpMethod.POST, new HttpEntity<>(body), cls);
    }

    private <Req extends SpotifyApiRequest, Res extends SpotifyApiResponse> Res execute(
            String url, HttpMethod method, HttpEntity<Req> entity, Class<Res> cls)
            throws SpotifyApiException, TooManyRequestsException{
        try {
            ResponseEntity<Res> responseEntity = restTemplate.exchange(url, method, entity, cls);
            if (responseEntity.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                int retryAfter = getRetryAfter(responseEntity);
                throw new TooManyRequestsException(retryAfter);
            }
            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new SpotifyApiException(e);
        }
    }

    private int getRetryAfter(ResponseEntity<?> responseEntity) {
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        return Optional.ofNullable(responseHeaders.getFirst("Retry-After"))
                .map(Integer::parseInt).orElse(60);
    }

    private String getUrl(String path) {
        return baseUrl + path;
    }

    public static class Builder {
        private final String baseUrl;
        private RestTemplate restTemplate = new RestTemplate();

        public Builder(String baseUrl) {
            Objects.requireNonNull(baseUrl);
            this.baseUrl = baseUrl;
        }

        public Builder restTemplate(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
            return this;
        }

        public DefaultSpotifyHttpClient build() {
            return new DefaultSpotifyHttpClient(this);
        }
    }
}
