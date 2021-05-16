package com.kushtrimh.tomorr.spotify.http;

import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Kushtrim Hajrizi
 */
public class DefaultSpotifyHttpClient implements SpotifyHttpClient {

    public static final String TOMORR_TRACE_ID_HEADER = "tomorr-trace-id";
    public static final int RETRY_AFTER_DEFAULT_VALUE = 60;

    private final Logger logger = LoggerFactory.getLogger(DefaultSpotifyHttpClient.class);

    private final String apiUrl;
    private final String authUrl;
    private final String userAgent;
    private final RestTemplate restTemplate;

    private DefaultSpotifyHttpClient(Builder builder) {
        this.apiUrl = builder.apiUrl;
        this.authUrl = builder.authUrl;
        this.restTemplate = builder.restTemplate;
        this.userAgent = builder.userAgent;
    }

    @Override
    public <Req extends SpotifyApiRequest, Res> Res get(String token, Req request, Class<Res> cls)
            throws SpotifyApiException, TooManyRequestsException {
        return execute(token, buildApiUrl(request), HttpMethod.GET, null, cls);
    }

    @Override
    public <Req extends SpotifyApiRequest, Res> Res post(String token, Req request, Class<Res> cls)
            throws SpotifyApiException, TooManyRequestsException {
        return execute(token, buildApiUrl(request), HttpMethod.POST, request, cls);
    }

    @Override
    public TokenResponse getToken(String clientId, String clientSecret) throws SpotifyApiException {
        String traceId = UUID.randomUUID().toString();
        HttpHeaders httpHeaders = new HttpHeaders();
        String authData = clientId + ":" + clientSecret;
        String encodedAuthData = Base64.getEncoder().encodeToString(authData.getBytes(StandardCharsets.UTF_8));
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuthData);
        httpHeaders.set(TOMORR_TRACE_ID_HEADER, traceId);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("grant_type", "client_credentials");
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(bodyParams, httpHeaders);
        logger.info("{} Sending auth request at {}", traceId, authUrl);
        try {
            TokenResponse response = restTemplate.postForObject(authUrl, body, TokenResponse.class);
            logger.info("{} Received successful response for auth request", traceId);
            return response;
        } catch (RestClientException e) {
            logger.error("{} Error while sending auth request", traceId, e);
            throw new SpotifyApiException(e);
        }
    }

    private <Req extends SpotifyApiRequest, Res> Res execute(
            String token, String url, HttpMethod method, Req body, Class<Res> cls)
            throws SpotifyApiException, TooManyRequestsException {
        String traceId = UUID.randomUUID().toString();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.USER_AGENT, userAgent);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        httpHeaders.add(TOMORR_TRACE_ID_HEADER, traceId);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        HttpEntity<Req> entity = new HttpEntity<>(body, httpHeaders);
        try {
            logger.info("{} Sending {} request at {}, with content {}",
                    traceId, method, url, entity.getBody());
            ResponseEntity<Res> responseEntity = restTemplate.exchange(url, method, entity, cls);
            Res response = responseEntity.getBody();
            logger.info("{} Received response {}", traceId, response);
            return response;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                int retryAfter = getRetryAfter(e.getResponseHeaders());
                logger.warn("{} 429 received for request at {}", traceId, url);
                throw new TooManyRequestsException(retryAfter);
            }
            logger.error("{} Client error for request {}", traceId, e);
            throw new SpotifyApiException(e);
        } catch (RestClientException e) {
            logger.error("{} Error while sending request", traceId, e);
            throw new SpotifyApiException(e);
        }
    }

    private <Req extends SpotifyApiRequest> String buildApiUrl(Req request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .host(apiUrl)
                .path(request.getApiRequestType().getPath())
                .queryParams(request.getQueryParams());
        return uriBuilder.build().toUriString();
    }

    private int getRetryAfter(HttpHeaders responseHeaders) {
        if (responseHeaders == null) {
            return RETRY_AFTER_DEFAULT_VALUE;
        }
        return Optional.ofNullable(responseHeaders.getFirst("Retry-After"))
                .map(Integer::parseInt).orElse(RETRY_AFTER_DEFAULT_VALUE);
    }

    public static class Builder {
        private final String apiUrl;
        private final String authUrl;
        private RestTemplate restTemplate = new RestTemplate();
        private String userAgent = "tomorr-agent";

        public Builder(String apiUrl, String authUrl) {
            Objects.requireNonNull(apiUrl);
            Objects.requireNonNull(authUrl);
            this.apiUrl = apiUrl;
            this.authUrl = authUrl;
        }

        public Builder restTemplate(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public DefaultSpotifyHttpClient build() {
            return new DefaultSpotifyHttpClient(this);
        }
    }
}
