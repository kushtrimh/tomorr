package com.kushtrimh.tomorr.sync.execute;

import com.kushtrimh.tomorr.album.service.AlbumService;
import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.SpotifyApiClient;
import com.kushtrimh.tomorr.spotify.api.request.NextNodeRequest;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistAlbumsApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistAlbumsApiResponse;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import com.kushtrimh.tomorr.task.manager.ArtistSyncTaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class DefaultArtistSyncTaskExecutor implements ArtistSyncTaskExecutor {

    private final Logger logger = LoggerFactory.getLogger(DefaultArtistSyncTaskExecutor.class);

    private final ArtistService artistService;
    private final ArtistSyncTaskManager artistSyncTaskManager;
    private final AlbumService albumService;
    private final SpotifyApiClient spotifyApiClient;

    public DefaultArtistSyncTaskExecutor(
            ArtistService artistService,
            ArtistSyncTaskManager artistSyncTaskManager,
            AlbumService albumService,
            SpotifyApiClient spotifyApiClient) {
        this.artistService = artistService;
        this.artistSyncTaskManager = artistSyncTaskManager;
        this.albumService = albumService;
        this.spotifyApiClient = spotifyApiClient;
    }

    @Override
    @RabbitListener(queues = "artistSync")
    public void execute(Task<ArtistTaskData> task) {
        Objects.requireNonNull(task);
        ArtistTaskData artistData = task.getData();
        Objects.requireNonNull(artistData);

        SpotifyApiRequest<GetArtistAlbumsApiResponse> request = artistData.getNextNode() != null ?
                new NextNodeRequest<>(artistData.getNextNode(), GetArtistAlbumsApiResponse.class) :
                new GetArtistAlbumsApiRequest.Builder(artistData.getArtistId()).build();

        GetArtistAlbumsApiResponse response;
        try {
            response = spotifyApiClient.get(LimitType.SPOTIFY_SYNC, request);
        } catch (TooManyRequestsException | SpotifyApiException e) {
            logger.error("Could not sync data for task {}, task will be reentered into the task queue", task, e);
            artistSyncTaskManager.add(artistData);
            return;
        }

        TaskType taskType = artistData.getTaskType();
        switch (taskType) {
            case SYNC -> sync(artistData, response);
            case CONTINUED_SYNC -> throw new UnsupportedOperationException();
            case INITIAL_SYNC -> throw new UnsupportedOperationException();
            default -> logger.warn("Task type {} not supported", taskType);
        }
    }

    private void sync(ArtistTaskData artistData, GetArtistAlbumsApiResponse response) {

    }
}
