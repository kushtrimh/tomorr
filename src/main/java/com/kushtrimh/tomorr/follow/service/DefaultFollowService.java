package com.kushtrimh.tomorr.follow.service;

import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.cache.ArtistCache;
import com.kushtrimh.tomorr.artist.service.ArtistSearchService;
import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import com.kushtrimh.tomorr.task.manager.TaskManager;
import com.kushtrimh.tomorr.user.User;
import com.kushtrimh.tomorr.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultFollowService implements FollowService {
    private final Logger logger = LoggerFactory.getLogger(DefaultFollowService.class);

    private final UserService userService;
    private final ArtistService artistService;
    private final ArtistCache artistCache;
    private final ArtistSearchService artistSearchService;
    private final TaskManager<ArtistTaskData> taskManager;

    public DefaultFollowService(
            UserService userService,
            ArtistService artistService,
            ArtistCache artistCache,
            ArtistSearchService artistSearchService,
            TaskManager<ArtistTaskData> taskManager) {
        this.userService = userService;
        this.artistService = artistService;
        this.artistCache = artistCache;
        this.artistSearchService = artistSearchService;
        this.taskManager = taskManager;
    }

    @Transactional
    @Override
    public boolean follow(User user, String artistId) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(artistId);
        boolean artistExistsOnSystem = artistCache.containsArtistId(artistId) || artistService.exists(artistId);
        if (!artistExistsOnSystem) {
            Optional<Artist> artistOpt = artistSearchService.get(artistId);
            if (artistOpt.isEmpty()) {
                return false;
            }
            Artist artist = artistOpt.get();
            artistService.save(artist);
            logger.info("Artist with id {} added", artistId);
            taskManager.add(ArtistTaskData.fromArtistId(artistId, TaskType.INITIAL_SYNC));
            logger.info("Submitted task for initial sync for artist {}", artistId);
        }
        userService.associate(user, artistId);
        return true;
    }
}
