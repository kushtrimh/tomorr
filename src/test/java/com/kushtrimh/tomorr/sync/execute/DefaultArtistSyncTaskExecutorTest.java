package com.kushtrimh.tomorr.sync.execute;

import com.kushtrimh.tomorr.album.Album;
import com.kushtrimh.tomorr.album.AlbumType;
import com.kushtrimh.tomorr.album.service.AlbumService;
import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.mail.spotify.SpotifyMailService;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.SpotifyApiClient;
import com.kushtrimh.tomorr.spotify.api.request.NextNodeRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistAlbumsApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.ImageResponseData;
import com.kushtrimh.tomorr.spotify.api.response.album.AlbumResponseData;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistAlbumsApiResponse;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import com.kushtrimh.tomorr.task.manager.ArtistSyncTaskManager;
import com.kushtrimh.tomorr.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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

    // TODO: Verify email not send

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
    public void execute_WhenAlbumCountIsEqualToResponseAlbumCountOnSync_DoNothing() throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var albumsCount = 15;
        var task = newSyncTask(artistId);

        when(spotifyApiClient.get(eq(LimitType.SPOTIFY_SYNC), any(GetArtistAlbumsApiRequest.class)))
                .thenReturn(newArtistAlbumsResponse(albumsCount));
        when(albumService.findCountByArtistId(artistId)).thenReturn(Optional.of(albumsCount));

        taskExecutor.execute(task);

        verify(artistSyncTaskManager, never()).add(any(ArtistTaskData.class));
        verify(albumService, never()).saveAll(any(), any());
    }

    @Test
    public void execute_WhenAlbumCountIsNullSetItAsZeroOnSync_DoNothing() throws TooManyRequestsException, SpotifyApiException {
        var albumsCount = 0;
        var artistId = "artist1";
        var task = newSyncTask(artistId);

        when(spotifyApiClient.get(eq(LimitType.SPOTIFY_SYNC), any(GetArtistAlbumsApiRequest.class)))
                .thenReturn(newArtistAlbumsResponse(albumsCount));
        when(albumService.findCountByArtistId(artistId)).thenReturn(Optional.empty());

        taskExecutor.execute(task);

        verify(artistSyncTaskManager, never()).add(any(ArtistTaskData.class));
        verify(albumService, never()).saveAll(any(), any());
    }

    // TODO: Sent emails here
    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void execute_WhenAlbumCountIsLessThanResponseAlbumCountOnSync_SaveAlbumsAndSendEmails() throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var existingAlbumsCount = 10;
        var responseAlbumsCount = 15;
        var task = newSyncTask(artistId);
        var albums = newAlbums(10);

        ArgumentCaptor<List<Album>> albumsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<GetArtistAlbumsApiRequest> requestCaptor = ArgumentCaptor.forClass(GetArtistAlbumsApiRequest.class);

        when(spotifyApiClient.get(eq(LimitType.SPOTIFY_SYNC), any(GetArtistAlbumsApiRequest.class)))
                .thenReturn(newArtistAlbumsResponse(responseAlbumsCount));
        when(albumService.findCountByArtistId(artistId)).thenReturn(Optional.of(existingAlbumsCount));
        when(albumService.findByArtist(artistId)).thenReturn(albums);

        taskExecutor.execute(task);

        verify(artistSyncTaskManager, never()).add(any(ArtistTaskData.class));
        verify(albumService, times(1)).saveAll(eq(artistId), albumsCaptor.capture());
        verify(spotifyApiClient, times(1)).get(eq(LimitType.SPOTIFY_SYNC), requestCaptor.capture());

        var addedAlbums = albumsCaptor.getValue();
        var request = requestCaptor.getValue();

        assertAll(
                () -> assertEquals(5, addedAlbums.size()),
                () -> assertEquals("album10", addedAlbums.get(0).id()),
                () -> assertEquals("album14", addedAlbums.get(4).id()),
                () -> assertEquals(artistId, request.getArtistId())
        );
    }

    @Test
    public void execute_WhenNewAlbumIsNotInCurrentBatchOnSync_AddNextNodeToQueue() throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var task = newSyncTask(artistId);
        var albums = newAlbums(20);

        // Increase total so we can test if next node is added to queue
        var nextUri = "nextUri";
        var response = newArtistAlbumsResponse(20);
        response.setTotal(22);
        response.setNext(nextUri);

        when(spotifyApiClient.get(eq(LimitType.SPOTIFY_SYNC), any(GetArtistAlbumsApiRequest.class)))
                .thenReturn(response);
        when(albumService.findCountByArtistId(artistId)).thenReturn(Optional.of(20));
        when(albumService.findByArtist(artistId)).thenReturn(albums);

        taskExecutor.execute(task);

        verify(artistSyncTaskManager, times(1)).add(ArtistTaskData.fromNextNode(artistId, nextUri, TaskType.SYNC));
        verify(albumService, never()).saveAll(eq(artistId), any());
    }

    @Test
    public void execute_WhenArtistTaskDataIsFromANextNodeOnSync_ContinueSyncing() throws TooManyRequestsException, SpotifyApiException {
        var nextUri = "nextUri";
        var artistId = "artist1";
        var task = new Task<>(ArtistTaskData.fromNextNode(artistId, nextUri, TaskType.SYNC));

        var requestCaptor = ArgumentCaptor.forClass(NextNodeRequest.class);

        when(spotifyApiClient.get(eq(LimitType.SPOTIFY_SYNC), any(NextNodeRequest.class)))
                .thenReturn(newArtistAlbumsResponse(5));

        taskExecutor.execute(task);

        verify(spotifyApiClient, times(1)).get(eq(LimitType.SPOTIFY_SYNC), requestCaptor.capture());

        var request = requestCaptor.getValue();
        assertEquals(nextUri, request.getUri());
    }

    @Test
    public void execute_WhenArtistTaskIsForInitialSyncAndHasANextNode_SaveAlbumsAndAddNextNodeToQueue() throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var albumsCount = 20;
        var nextUri = "nextUri";
        var task = newInitialSyncTask(artistId);
        task.getData().setNextNode(nextUri);

        var albumsCaptor = ArgumentCaptor.forClass(List.class);
        var nextArtistTaskCaptor = ArgumentCaptor.forClass(ArtistTaskData.class);

        var response = newArtistAlbumsResponse(albumsCount);
        response.setNext(nextUri);
        when(spotifyApiClient.get(eq(LimitType.SPOTIFY_SYNC), any()))
                .thenReturn(response);

        taskExecutor.execute(task);

        verify(artistSyncTaskManager, times(1)).add(nextArtistTaskCaptor.capture());
        verify(albumService, times(1)).saveAll(eq(artistId), albumsCaptor.capture());
        verify(artistService, never()).activateArtist(any());

        var albums = albumsCaptor.getValue();
        var nextArtistTask = nextArtistTaskCaptor.getValue();

        assertAll(
                () -> assertEquals(albumsCount, albums.size()),
                () -> assertEquals(artistId, nextArtistTask.getArtistId()),
                () -> assertEquals(nextUri, nextArtistTask.getNextNode())
        );
    }

    @Test
    public void execute_WhenArtistTaskIsForInitialSyncAndDoesNotHaveANextNode_SaveAlbumsAndActivateArtist() throws TooManyRequestsException, SpotifyApiException {
        var artistId = "artist1";
        var albumsCount = 20;
        var task = newInitialSyncTask(artistId);

        var albumsCaptor = ArgumentCaptor.forClass(List.class);

        when(spotifyApiClient.get(eq(LimitType.SPOTIFY_SYNC), any()))
                .thenReturn(newArtistAlbumsResponse(albumsCount));

        taskExecutor.execute(task);

        verify(artistSyncTaskManager, never()).add(any(ArtistTaskData.class));
        verify(albumService, times(1)).saveAll(eq(artistId), albumsCaptor.capture());
        verify(artistService, times(1)).activateArtist(artistId);

        var albums = albumsCaptor.getValue();

        assertEquals(albumsCount, albums.size());
    }

    private Task<ArtistTaskData> newSyncTask(String artistId) {
        var artistData = ArtistTaskData.fromArtistId(artistId, TaskType.SYNC);
        return new Task<>(artistData);
    }

    private Task<ArtistTaskData> newInitialSyncTask(String artistId) {
        var artistData = ArtistTaskData.fromArtistId(artistId, TaskType.INITIAL_SYNC);
        return new Task<>(artistData);
    }

    private GetArtistAlbumsApiResponse newArtistAlbumsResponse(int albumCount) {
        var response = new GetArtistAlbumsApiResponse();
        response.setTotal(albumCount);
        response.setItems(IntStream.range(0, albumCount)
                .mapToObj(i -> {
                    var item = new AlbumResponseData();
                    item.setId("album" + i);
                    item.setName("album-name" + i);
                    item.setAlbumType(AlbumType.ALBUM.name());
                    item.setReleaseDate("2020-01-01");
                    item.setImages(List.of(new ImageResponseData("image-url-" + i)));
                    return item;
                }).toList());
        return response;
    }

    private List<Album> newAlbums(int albumCount) {
        return IntStream.range(0, albumCount)
                .mapToObj(i -> new Album(
                        "album" + i,
                        "album-name" + i,
                        AlbumType.ALBUM,
                        null,
                        "2020-01-01",
                        "image-url-" + i))
                .toList();
    }
}