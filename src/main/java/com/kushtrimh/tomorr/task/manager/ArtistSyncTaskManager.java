package com.kushtrimh.tomorr.task.manager;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class ArtistSyncTaskManager implements TaskManager<ArtistTaskData> {
    private final Logger logger = LoggerFactory.getLogger(ArtistSyncTaskManager.class);

    protected static final String ARTIST_SYNC_TASK_QUEUE_KEY = "queue:artistSync";

    private final RedisTemplate<String, Task<ArtistTaskData>> template;

    public ArtistSyncTaskManager(RedisTemplate<String, Task<ArtistTaskData>> template) {
        this.template = template;
    }

    @Override
    public void create(ArtistTaskData data) {
        create(List.of(data));
    }

    @Override
    public void create(List<ArtistTaskData> dataList) {
        Objects.requireNonNull(dataList);

        logger.info("{} tasks to be created", dataList.size());
        List<Task<ArtistTaskData>> tasks = dataList.stream().map(data -> {
            logger.debug("Task with data {} created", data);
            return new Task<>(data, Instant.now());
        }).toList();
        template.opsForList().rightPushAll(ARTIST_SYNC_TASK_QUEUE_KEY, tasks);
    }

    @Override
    public long getQueuedTasksCount() {
        Long count = template.opsForList().size(ARTIST_SYNC_TASK_QUEUE_KEY);
        return count != null ? count : 0;
    }
}
