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

import java.util.Collections;
import java.util.List;

import static com.kushtrimh.tomorr.task.manager.ArtistSyncTaskManager.*;
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
    public void add_WhenCalledWithNullData_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> manager.add((ArtistTaskData) null));
    }

    @Test
    public void add_WhenCalledWithNullList_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> manager.add((List<ArtistTaskData>) null));
    }

    @Test
    public void add_WhenCalledWithValidList_CreateTasksAndAddToQueue() {
        when(template.opsForList()).thenReturn(listOperations);
        List<ArtistTaskData> artistTaskData = List.of(
                ArtistTaskData.fromArtistId("artist01", TaskType.SYNC),
                ArtistTaskData.fromNextNode("https://nextnode.node.next.tomorr/artist01", TaskType.SYNC),
                ArtistTaskData.fromArtistId("artist02", TaskType.INITIAL_SYNC)
        );
        manager.add(artistTaskData);
        assertThatTasksAreCreatedSuccessfully(artistTaskData);
    }

    @Test
    public void add_WhenCalledWithValidData_CreateTaskAndAddToQueue() {
        when(template.opsForList()).thenReturn(listOperations);
        var taskData = ArtistTaskData.fromArtistId("artist01", TaskType.SYNC);
        manager.add(taskData);
        assertThatTasksAreCreatedSuccessfully(List.of(taskData));
    }

    @Test
    public void getQueuedTasksCount_WhenQueueDoesNotExists_Return0() {
        when(template.opsForList()).thenReturn(listOperations);
        when(listOperations.size(ARTIST_SYNC_TASK_QUEUE_KEY)).thenReturn(null);
        assertEquals(0, manager.getQueuedTasksCount());
    }

    @Test
    public void getQueuedTasksCount_WhenQueueExistsAndContainsTasksReturnNumberOfContainedTasks() {
        long tasksCount = 55;
        when(template.opsForList()).thenReturn(listOperations);
        when(listOperations.size(ARTIST_SYNC_TASK_QUEUE_KEY)).thenReturn(tasksCount);
        assertEquals(tasksCount, manager.getQueuedTasksCount());
    }

    @Test
    public void getAll_WhenTasksRangeReturnsNull_ReturnEmptyListAndDoNotDeleteEntry() {
        when(template.opsForList()).thenReturn(listOperations);
        when(listOperations.range(ARTIST_SYNC_TASK_QUEUE_KEY, 0, -1)).thenReturn(Collections.emptyList());
        assertTrue(manager.getAll().isEmpty());
        verify(template, never()).delete(ARTIST_SYNC_TASK_QUEUE_KEY);
    }

    @Test
    public void getAll_WhenThereAreNoTasks_ReturnEmptyListAndDoNotDeleteEntry() {
        when(template.opsForList()).thenReturn(listOperations);
        when(listOperations.range(ARTIST_SYNC_TASK_QUEUE_KEY, 0, -1)).thenReturn(Collections.emptyList());
        assertTrue(manager.getAll().isEmpty());
        verify(template, never()).delete(ARTIST_SYNC_TASK_QUEUE_KEY);
    }

    @Test
    public void getAll_WhenThereAreQueuedTasks_ReturnTasksAndDeleteEntry() {
        List<Task<ArtistTaskData>> tasks = List.of(
                new Task<>(ArtistTaskData.fromArtistId("artist01", TaskType.SYNC)),
                new Task<>(ArtistTaskData.fromArtistId("artist02", TaskType.SYNC)));
        when(template.opsForList()).thenReturn(listOperations);
        when(listOperations.range(ARTIST_SYNC_TASK_QUEUE_KEY, 0, -1)).thenReturn(tasks);
        assertEquals(tasks, manager.getAll());
        verify(template, times(1)).delete(ARTIST_SYNC_TASK_QUEUE_KEY);
    }

    private void assertThatTasksAreCreatedSuccessfully(List<ArtistTaskData> artistTaskData) {
        ArgumentCaptor<List<Task<ArtistTaskData>>> captor = ArgumentCaptor.forClass(List.class);
        verify(listOperations, times(1)).rightPushAll(
                eq(ARTIST_SYNC_TASK_QUEUE_KEY),
                captor.capture());

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