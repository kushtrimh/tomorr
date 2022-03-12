package com.kushtrimh.tomorr.sync.execute;

import com.kushtrimh.tomorr.album.Album;
import com.kushtrimh.tomorr.album.service.AlbumService;
import com.kushtrimh.tomorr.configuration.RabbitMQConfiguration;
import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.mail.spotify.SpotifyMailService;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.SpotifyApiClient;
import com.kushtrimh.tomorr.spotify.api.request.NextNodeRequest;
import com.kushtrimh.tomorr.spotify.api.request.SpotifyApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistAlbumsApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.album.AlbumResponseData;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistAlbumsApiResponse;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import com.kushtrimh.tomorr.task.manager.ArtistSyncTaskManager;
import com.kushtrimh.tomorr.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class DefaultArtistSyncTaskExecutor implements ArtistSyncTaskExecutor {

    private final Logger logger = LoggerFactory.getLogger(DefaultArtistSyncTaskExecutor.class);

    private final UserService userService;
    private final ArtistSyncTaskManager artistSyncTaskManager;
    private final AlbumService albumService;
    private final SpotifyApiClient spotifyApiClient;
    private final SpotifyMailService spotifyMailService;

    public DefaultArtistSyncTaskExecutor(
            UserService userService,
            ArtistSyncTaskManager artistSyncTaskManager,
            AlbumService albumService,
            SpotifyApiClient spotifyApiClient,
            SpotifyMailService spotifyMailService) {
        this.userService = userService;
        this.artistSyncTaskManager = artistSyncTaskManager;
        this.albumService = albumService;
        this.spotifyApiClient = spotifyApiClient;
        this.spotifyMailService = spotifyMailService;
    }

    @Override
    @RabbitListener(queues = RabbitMQConfiguration.ARTIST_SYNC_QUEUE)
    public void execute(Task<ArtistTaskData> task) {
        Objects.requireNonNull(task);
        ArtistTaskData artistData = task.getData();
        Objects.requireNonNull(artistData);

        // TODO: Integration test rabbitmq configuration, if it works to send and receive messages
        // TODO: Unit test for this class
        // TODO: Save artist id to the next task, no matter if its a next node

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
            case INITIAL_SYNC -> initialSync(artistData, response);
            default -> logger.warn("Task type {} not supported", taskType);
        }
    }

    private void sync(ArtistTaskData artistData, GetArtistAlbumsApiResponse response) {
        int count = 0; // TODO: Get count of artist albums (cache supported) -> albumService.findCountByArtistId();
        int responseCount = response.getTotal();
        if (count == responseCount) {
            logger.debug("Total albums count ({}) matches the count from the response ({}) for artist task data {}",
                    count, responseCount, artistData);
            return;
        }
        List<Album> artistAlbums = albumService.findByArtist(artistData.getArtistId());
        Set<String> artistAlbumIds = artistAlbums.stream().map(Album::id).collect(Collectors.toSet());
        List<AlbumResponseData> newAlbums = response.getItems().stream()
                .filter(item -> !artistAlbumIds.contains(item.getId())).toList();
        if (!newAlbums.isEmpty()) {
            // TODO: Save albums in database
            // TODO: Get all users that follow this artist
            // TODO: Send email to all of them
        }
        if (count > (responseCount + newAlbums.size()) && artistData.getNextNode() != null) {
            artistSyncTaskManager.add(ArtistTaskData.fromNextNode(artistData.getNextNode(), TaskType.SYNC));
        }
    }

    private void initialSync(ArtistTaskData artistData, GetArtistAlbumsApiResponse response) {
        // TODO: Convert to response items to albums, and save them on database for the given artist
        // TODO: if there's a next, add the task to the task manager with initial sync type
    }
}
