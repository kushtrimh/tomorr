package com.kushtrimh.tomorr.sync.execute;

import com.kushtrimh.tomorr.album.service.AlbumService;
import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.mail.spotify.SpotifyMailService;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.SpotifyApiClient;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistAlbumsApiRequest;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import com.kushtrimh.tomorr.task.manager.ArtistSyncTaskManager;
import com.kushtrimh.tomorr.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class DefaultArtistSyncTaskExecutorTest {

    @Mock
    private UserService userService;
    @Mock
    private ArtistSyncTaskManager artistSyncTaskManager;
    @Mock
    private AlbumService albumService;
    @Mock
    private ArtistService artistService;
    @Mock
    private SpotifyApiClient spotifyApiClient;
    @Mock
    private SpotifyMailService spotifyMailService;

    private DefaultArtistSyncTaskExecutor taskExecutor;

    @BeforeEach
    public void init() {
        taskExecutor = new DefaultArtistSyncTaskExecutor(
                userService,
                artistSyncTaskManager,
                albumService,
                artistService,
                spotifyApiClient,
                spotifyMailService);
    }

    @Test
    public void execute_WhenTaskIsNull_ThrowException() {
        assertThrows(NullPointerException.class,
                () -> taskExecutor.execute(null));
    }

    @Test
    public void execute_WhenArtistDataIsNull_ThrowException() {
        var task = new Task<ArtistTaskData>(null);
        assertThrows(NullPointerException.class,
                () -> taskExecutor.execute(task));
    }

    @Test
    public void execute_WhenThereIsASpotifyException_ReturnAndAddTaskToQueue() throws TooManyRequestsException, SpotifyApiException {
        assertTaskFailOfSpotifyException(new SpotifyApiException());
    }

    @Test
    public void execute_WhenThereIsATooManyRequestException_ReturnAndAddTaskToQueue() throws TooManyRequestsException, SpotifyApiException {
        assertTaskFailOfSpotifyException(new TooManyRequestsException("Too many requests"));
    }

    private void assertTaskFailOfSpotifyException(Throwable throwable) throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var artistData = ArtistTaskData.fromArtistId(artistId, TaskType.SYNC);
        var task = new Task<>(artistData);

        when(spotifyApiClient.get(eq(LimitType.SPOTIFY_SYNC), any(GetArtistAlbumsApiRequest.class)))
                .thenThrow(throwable);

        taskExecutor.execute(task);

        verify(artistSyncTaskManager, times(1)).add(artistData);
        verify(albumService, never()).findCountByArtistId(artistId);
        verify(albumService, never()).findByArtist(artistId);
        verify(albumService, never()).saveAll(eq(artistId), any());
    }

    @Test
    public void execute_WhenAlbumCountIsEqualToResponseAlbumCountOnSync_DoNothing() {
        var artistId = "artist1";
        var task = newTask(artistId);

    }

    private Task<ArtistTaskData> newTask(String artistId) {
        var artistData = ArtistTaskData.fromArtistId(artistId, TaskType.SYNC);
        return new Task<>(artistData);
    }
}