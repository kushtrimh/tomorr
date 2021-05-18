package com.kushtrimh.tomorr.spotify.job;

import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.api.SpotifyApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class SpotifyAuthenticationJobTest {

    @Mock
    private SpotifyApiClient spotifyApiClient;

    private SpotifyAuthenticationJob spotifyAuthenticationJob;

    @BeforeEach
    public void init() {
        spotifyAuthenticationJob = new SpotifyAuthenticationJob(spotifyApiClient);
    }

    @Test
    public void executeInternal_WhenJobIsCalled_RefreshAccessToken()
            throws SpotifyApiException, JobExecutionException {
        spotifyAuthenticationJob.executeInternal(null);
        verify(spotifyApiClient, times(1)).refreshAccessToken();
    }
}
