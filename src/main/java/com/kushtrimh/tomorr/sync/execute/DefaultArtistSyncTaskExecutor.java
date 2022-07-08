package com.kushtrimh.tomorr.sync.execute;

import com.kushtrimh.tomorr.album.Album;
import com.kushtrimh.tomorr.album.AlbumType;
import com.kushtrimh.tomorr.album.cache.AlbumCache;
import com.kushtrimh.tomorr.album.service.AlbumService;
import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.configuration.RabbitMQConfiguration;
import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.mail.notification.NotificationMailService;
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
import com.kushtrimh.tomorr.user.User;
import com.kushtrimh.tomorr.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private final ArtistService artistService;
    private final SpotifyApiClient spotifyApiClient;
    private final AlbumCache albumCache;
    private final NotificationMailService notificationMailService;

    public DefaultArtistSyncTaskExecutor(
            UserService userService,
            ArtistSyncTaskManager artistSyncTaskManager,
            AlbumService albumService,
            ArtistService artistService,
            SpotifyApiClient spotifyApiClient,
            AlbumCache albumCache,
            NotificationMailService notificationMailService) {
        this.userService = userService;
        this.artistSyncTaskManager = artistSyncTaskManager;
        this.albumService = albumService;
        this.artistService = artistService;
        this.spotifyApiClient = spotifyApiClient;
        this.albumCache = albumCache;
        this.notificationMailService = notificationMailService;
    }

    @Override
    @RabbitListener(queues = RabbitMQConfiguration.ARTIST_SYNC_QUEUE)
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
            case INITIAL_SYNC -> initialSync(artistData, response);
            default -> logger.warn("Task type {} not supported", taskType);
        }
    }

    private void sync(ArtistTaskData artistData, GetArtistAlbumsApiResponse response) {
        int count = albumService.findCountByArtistId(artistData.getArtistId()).orElseGet(() -> {
            logger.warn("Could not find count for artist {}", artistData.getArtistId());
            return 0;
        });

        int responseCount = response.getTotal();
        if (count == responseCount) {
            logger.debug("Total albums count ({}) matches the count from the response ({}) for artist task data {}",
                    count, responseCount, artistData);
            return;
        }
        List<Album> artistAlbums = albumService.findByArtist(artistData.getArtistId());
        Set<String> artistAlbumIds = artistAlbums.stream().map(Album::id).collect(Collectors.toSet());
        List<AlbumResponseData> newAlbumsFromResponse = response.getItems().stream()
                .filter(item -> !artistAlbumIds.contains(item.getId())).toList();
        if (!newAlbumsFromResponse.isEmpty()) {
            var newAlbums = albumsFromResponse(newAlbumsFromResponse);
            for (var newAlbum : newAlbums) {
                albumService.save(artistData.getArtistId(), newAlbum);
            }
            List<User> users = userService.findByFollowedArtist(artistData.getArtistId());
            var artist = artistService.findById(artistData.getArtistId()).orElseGet(() -> {
                logger.error("Artist with id {} not found at a critical part of the process", artistData.getArtistId());
                return new Artist(artistData.getArtistId(), "Artist with ID (" + artistData.getArtistId() + ")", "", 0);
            });
            notify(newAlbums, artist, users);
        }
        if (responseCount > (count + newAlbumsFromResponse.size()) && response.getNext() != null) {
            artistSyncTaskManager.add(ArtistTaskData.fromNextNode(artistData.getArtistId(), response.getNext(), TaskType.SYNC));
        }
    }

    private void initialSync(ArtistTaskData artistData, GetArtistAlbumsApiResponse response) {
        for (var newAlbum : albumsFromResponse(response.getItems())) {
            albumService.save(artistData.getArtistId(), newAlbum);
        }
        var next = response.getNext();
        if (next == null || next.isBlank()) {
            artistService.activateArtist(artistData.getArtistId());
        } else {
            artistSyncTaskManager.add(ArtistTaskData.fromNextNode(artistData.getArtistId(), next, TaskType.INITIAL_SYNC));
        }
    }

    private List<Album> albumsFromResponse(List<AlbumResponseData> newAlbums) {
        return newAlbums.stream().map(item -> {
            AlbumType type = AlbumType.fromApiType(item.getAlbumType()).orElseGet(() -> {
                logger.warn("Could not find album type for album {}, {}. Using {} as default.",
                        item.getId(), item.getAlbumType(), AlbumType.ALBUM);
                return AlbumType.ALBUM;
            });
            var image = Optional.ofNullable(item.getImages())
                    .filter(images -> !images.isEmpty())
                    .map(images -> images.get(0).getUrl()).orElse(null);
            return new Album(
                    item.getId(),
                    item.getName(),
                    type,
                    null,
                    item.getReleaseDate(),
                    image);
        }).collect(Collectors.toList());
    }

    private void notify(List<Album> albums, Artist artist, List<User> users) {
        for (var album : albums) {
            if (!albumCache.isNotificationSent(album.name())) {
                notificationMailService.sendNewReleaseNotification(album, artist, users);
            }
        }
    }
}
