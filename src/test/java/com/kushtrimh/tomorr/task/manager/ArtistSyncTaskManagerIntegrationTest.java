package com.kushtrimh.tomorr.task.manager;

import com.kushtrimh.tomorr.configuration.TestRedisConfiguration;
import com.kushtrimh.tomorr.dal.extension.TestRedisExtension;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.kushtrimh.tomorr.task.manager.ArtistSyncTaskManager.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = {@Tag("redis"), @Tag("integration")})
@ContextConfiguration(classes = {TestRedisConfiguration.class})
@ExtendWith({SpringExtension.class, TestRedisExtension.class})
public class ArtistSyncTaskManagerIntegrationTest {

    @Autowired
    private RedisTemplate<String, Task<ArtistTaskData>> template;

    private ArtistSyncTaskManager manager;

    @BeforeEach
    public void init() {
        manager = new ArtistSyncTaskManager(template);
    }

    @Test
    public void create_WhenCalledWithValidList_CreateTasksAndAddToQueue() {
        List<ArtistTaskData> data = List.of(
                ArtistTaskData.fromArtistId("artist01", TaskType.SYNC),
                ArtistTaskData.fromArtistId("artist02", TaskType.INITIAL_SYNC),
                ArtistTaskData.fromArtistId("https://nextnode.node.next.tomorr/artist01", TaskType.CONTINUED_SYNC)
        );
        manager.add(data);
        List<ArtistTaskData> retrievedData = Stream.of(
                template.opsForList().leftPop(ARTIST_SYNC_TASK_QUEUE_KEY),
                template.opsForList().leftPop(ARTIST_SYNC_TASK_QUEUE_KEY),
                template.opsForList().leftPop(ARTIST_SYNC_TASK_QUEUE_KEY)
        ).filter(Objects::nonNull).map(Task::getData).toList();
        assertEquals(data, retrievedData);
    }

    @Test
    public void create_WhenCalledWithValidData_CreateTaskAndAddToQueue() {
        ArtistTaskData artistTaskData = ArtistTaskData.fromArtistId("artist04", TaskType.SYNC);
        manager.add(artistTaskData);
        Task<ArtistTaskData> returnedTask = template.opsForList().leftPop(ARTIST_SYNC_TASK_QUEUE_KEY);
        assertEquals(artistTaskData, returnedTask.getData());
    }

    @Test
    public void getQueuedTasksCount_WhenTasksExistInQueue_ReturnSizeOfTasksInQueue() {
        List<ArtistTaskData> data = List.of(
                ArtistTaskData.fromArtistId("artist01", TaskType.SYNC),
                ArtistTaskData.fromArtistId("https://nextnode.node.next.tomorr/artist01", TaskType.CONTINUED_SYNC)
        );
        manager.add(data);
        assertEquals(2, manager.getQueuedTasksCount());
        template.delete(ARTIST_SYNC_TASK_QUEUE_KEY);
    }
}
