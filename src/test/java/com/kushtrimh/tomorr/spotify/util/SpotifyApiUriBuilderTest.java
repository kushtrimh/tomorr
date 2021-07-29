package com.kushtrimh.tomorr.spotify.util;

import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
class SpotifyApiUriBuilderTest {

    private SpotifyApiUriBuilder builder;

    private String testApiUrl;

    @BeforeEach
    public void init() {
        testApiUrl = "http://localhost:8080/api/v1";
        builder = new SpotifyApiUriBuilder(testApiUrl);
    }

    @Test
    public void getApiUri_WhenRequestIsNull_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> builder.getApiUri(null));
    }

    @Test
    public void getApiUri_WhenRequestIsValid_ReturnApiUri() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("one", "one-value");
        params.add("two", "two-value");
        params.add("three", "three-value");

        var returnedUri = builder.getApiUri(buildRequest("/path", params));
        var expectedUri = testApiUrl + "/path?one=one-value&two=two-value&three=three-value";
        assertEquals(expectedUri, returnedUri);
    }

    @Test
    public void getApiUri_WhenRequestIsValidButPathNullOrEmpty_ReturnApiUriWithoutPath() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("one", "one-value");
        params.add("two", "two-value");

        var expectedUri = testApiUrl + "?one=one-value&two=two-value";

        var returnedUriWithNull = builder.getApiUri(buildRequest(null, params));
        assertEquals(expectedUri, returnedUriWithNull);

        var returnedUriWithEmpty = builder.getApiUri(buildRequest("", params));
        assertEquals(expectedUri, returnedUriWithEmpty);
    }

    @Test
    public void getApiUri_WhenRequestIsValidButParamsAreNullOrEmpty_ReturnApiUriWithoutParams() {
        var path = "/path";
        var expectedUri = testApiUrl + path;

        var returnedUriWithNull = builder.getApiUri(buildRequest(path, null));
        assertEquals(expectedUri, returnedUriWithNull);

        var returnedUriWithEmpty = builder.getApiUri(buildRequest(path, new LinkedMultiValueMap<>()));
        assertEquals(expectedUri, returnedUriWithEmpty);
    }

    private SpotifyApiRequest buildRequest(String path, MultiValueMap<String, String> params) {
        return new SpotifyApiRequest() {
            @Override
            public String getPath() {
                return path;
            }

            @Override
            public MultiValueMap<String, String> getQueryParams() {
                return params;
            }
        };
    }
}