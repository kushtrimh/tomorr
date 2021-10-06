package com.kushtrimh.tomorr.sync.produce;

import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.properties.LimitProperties;
import com.kushtrimh.tomorr.sync.send.ArtistSyncTaskSender;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import com.kushtrimh.tomorr.task.manager.TaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class DefaultArtistSyncTaskProducer implements ArtistSyncTaskProducer {
    private final Logger logger = LoggerFactory.getLogger(DefaultArtistSyncTaskProducer.class);

    protected static final String ARTIST_SYNC_KEY = "syncKey";

    private final TaskManager<ArtistTaskData> artistSyncTaskManager;
    private final ArtistService artistService;
    private final ArtistSyncTaskSender artistSyncTaskSender;
    private final LimitProperties limitProperties;
    private final StringRedisTemplate stringRedisTemplate;

    // TODO: Add tests for this whole package, unit + integration

    public DefaultArtistSyncTaskProducer(
            TaskManager<ArtistTaskData> artistSyncTaskManager,
            ArtistService artistService,
            ArtistSyncTaskSender artistSyncTaskSender,
            LimitProperties limitProperties,
            StringRedisTemplate stringRedisTemplate) {
        this.artistSyncTaskManager = artistSyncTaskManager;
        this.artistService = artistService;
        this.artistSyncTaskSender = artistSyncTaskSender;
        this.limitProperties = limitProperties;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostConstruct
    public void init() {
        stringRedisTemplate.opsForValue().setIfAbsent(ARTIST_SYNC_KEY, UUID.randomUUID().toString());
    }

    @Override
    public void produce() {
        List<Task<ArtistTaskData>> existingTasks = artistSyncTaskManager.getAll();
        var tasksInQueue = existingTasks.size();
        logger.info("{} tasks currently in queue", tasksInQueue);

        var tasksToCreate = limitProperties.getSpotify() - tasksInQueue;
        logger.info("{} tasks will be produced", tasksToCreate);

        String syncKey = getSyncKey();
        List<Artist> artists = artistService.findToSync(syncKey, tasksToCreate);
        // If no artists returned, update sync key. Here we assume all artists
        // have been synced at least once with the same sync key.
        if (artists.isEmpty()) {
            String newSyncKey = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set(ARTIST_SYNC_KEY, newSyncKey);
            logger.info("Sync key updated from {} to {}", syncKey, newSyncKey);
            return;
        }
        List<Task<ArtistTaskData>> tasksToExecute = new ArrayList<>(existingTasks);
        tasksToExecute.addAll(
                artists.stream()
                        .map(artist -> new Task<>(ArtistTaskData.fromArtistId(artist.id(), TaskType.SYNC), Instant.now()))
                        .toList());
        logger.info("{} tasks forwarded to task sender", tasksToExecute.size());
        artistSyncTaskSender.send(tasksToExecute);
    }

    private String getSyncKey() {
        String syncKey = stringRedisTemplate.opsForValue().get(ARTIST_SYNC_KEY);
        if (syncKey == null) {
            syncKey = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set(ARTIST_SYNC_KEY, syncKey);
        }
        return syncKey;
    }
}
