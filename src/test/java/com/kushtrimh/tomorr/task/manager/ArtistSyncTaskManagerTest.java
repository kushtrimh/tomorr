package com.kushtrimh.tomorr.task.manager;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({MockitoExtension.class})
public class ArtistSyncTaskManagerTest {

    @Mock
    private RedisTemplate<String, Task<ArtistTaskData>> template;
    @Mock
    private ListOperations<String, Task<ArtistTaskData>> listOperations;

    private ArtistSyncTaskManager manager;

    @BeforeEach
    public void init() {
        manager = new ArtistSyncTaskManager(template);
    }

    @Test
    public void create_WhenCalledWithNullData_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> manager.create((ArtistTaskData) null));
    }

    @Test
    public void create_WhenCalledWithNullList_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> manager.create((List<ArtistTaskData>) null));
    }

    @Test
    public void create_WhenCalledWithValidList_CreateTasksAndAddToQueue() {
        when(template.opsForList()).thenReturn(listOperations);
        List<ArtistTaskData> artistTaskData = List.of(
                ArtistTaskData.fromArtistId("artist01", TaskType.SYNC),
                ArtistTaskData.fromNextNode("https://nextnode.node.next.tomorr/artist01", TaskType.CONTINUED_SYNC),
                ArtistTaskData.fromArtistId("artist02", TaskType.INITIAL_SYNC)
                );
        manager.create(artistTaskData);
        assertThatTasksAreCreatedSuccessfully(artistTaskData);
    }

    @Test
    public void create_WhenCalledWithValidData_CreateTaskAndAddToQueue() {
        when(template.opsForList()).thenReturn(listOperations);
        var taskData = ArtistTaskData.fromArtistId("artist01", TaskType.SYNC);
        manager.create(taskData);
        assertThatTasksAreCreatedSuccessfully(List.of(taskData));
    }

    @Test
    public void getQueuedTasksCount_WhenQueueDoesNotExists_Return0() {
        when(template.opsForList()).thenReturn(listOperations);
        when(listOperations.size(ArtistSyncTaskManager.ARTIST_SYNC_TASK_QUEUE_KEY)).thenReturn(null);
        assertEquals(0, manager.getQueuedTasksCount());
    }

    @Test
    public void getQueuedTasksCount_WhenQueueExistsAndContainsTasksReturnNumberOfContainedTasks() {
        long tasksCount = 55;
        when(template.opsForList()).thenReturn(listOperations);
        when(listOperations.size(ArtistSyncTaskManager.ARTIST_SYNC_TASK_QUEUE_KEY)).thenReturn(tasksCount);
        assertEquals(tasksCount, manager.getQueuedTasksCount());
    }

    private void assertThatTasksAreCreatedSuccessfully(List<ArtistTaskData> artistTaskData) {
        ArgumentCaptor<List<Task<ArtistTaskData>>> captor = ArgumentCaptor.forClass(List.class);
        verify(listOperations, times(1)).rightPushAll(
                eq(ArtistSyncTaskManager.ARTIST_SYNC_TASK_QUEUE_KEY),
                captor.capture()
        );

        List<Task<ArtistTaskData>> tasks = captor.getValue();
        for (var i = 0; i < tasks.size(); i++) {
            Task<ArtistTaskData> task = tasks.get(0);
            ArtistTaskData expectedArtistTaskData = artistTaskData.get(0);

            assertNotNull(task.getCreatedAt());
            assertNull(task.getReinsertedAt());
            assertEquals(expectedArtistTaskData, task.getData());
        }
    }

}