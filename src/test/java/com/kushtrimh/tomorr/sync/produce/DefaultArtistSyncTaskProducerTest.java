package com.kushtrimh.tomorr.sync.produce;

import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.properties.LimitProperties;
import com.kushtrimh.tomorr.sync.send.ArtistSyncTaskSender;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import com.kushtrimh.tomorr.task.manager.TaskManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.kushtrimh.tomorr.sync.produce.DefaultArtistSyncTaskProducer.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith(MockitoExtension.class)
class DefaultArtistSyncTaskProducerTest {

    @Mock
    private TaskManager<ArtistTaskData> artistSyncTaskManager;
    @Mock
    private ArtistService artistService;
    @Mock
    private ArtistSyncTaskSender artistSyncTaskSender;
    @Mock
    private LimitProperties limitProperties;
    @Mock
    private StringRedisTemplate stringRedisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    private DefaultArtistSyncTaskProducer producer;

    @BeforeEach
    public void init() {
        producer = new DefaultArtistSyncTaskProducer(
                artistSyncTaskManager, artistService, artistSyncTaskSender, limitProperties, stringRedisTemplate);
    }

    @Test
    public void init_WhenExecuted_SetSyncKeyIfMissing() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        producer.init();
        verify(valueOperations, times(1)).setIfAbsent(eq(ARTIST_SYNC_KEY), any(String.class));
    }

    @Test
    public void produce_WhenTasksToCreateNumberIsLessThanZero_DoNotForwardTasksToSender() {
        List<Task<ArtistTaskData>> tasks = generateTasks("artist", 50);
        when(artistSyncTaskManager.getAll()).thenReturn(tasks);
        when(limitProperties.getSpotify()).thenReturn(40);
        producer.produce();
        verify(artistService, never()).findToSync(any(String.class), any(Integer.class));
        verify(artistSyncTaskSender, never()).send(anyList());
        verify(valueOperations, never()).set(eq(ARTIST_SYNC_KEY), any(String.class));
    }

    @Test
    public void produce_WhenNoArtistsWithCurrentSyncKeyExist_GenerateNewSyncKeyAndForwardExistingTasksToSender() {
        String syncKey = UUID.randomUUID().toString();
        List<Task<ArtistTaskData>> existingTasks = generateTasks("existing-artist", 100);

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(ARTIST_SYNC_KEY)).thenReturn(syncKey);
        when(limitProperties.getSpotify()).thenReturn(600);
        when(artistSyncTaskManager.getAll()).thenReturn(existingTasks);
        when(artistService.findToSync(syncKey, 500)).thenReturn(Collections.emptyList());
        producer.produce();
        verify(valueOperations, times(1)).set(eq(ARTIST_SYNC_KEY), any(String.class));
        verify(artistSyncTaskSender, times(1)).send(existingTasks);
    }

    @Test
    public void produce_WhenNewArtistsNeedToBeSynced_CreateTasksForArtistsAndForwardWithExistingTasksToSender() {
        ArgumentCaptor<List<Task<ArtistTaskData>>> captor = ArgumentCaptor.forClass(List.class);

        String syncKey = UUID.randomUUID().toString();
        List<Task<ArtistTaskData>> existingTasks = generateTasks("existing-artist", 200);
        List<Task<ArtistTaskData>> newTasks = generateTasks("artist", 400);
        List<Artist> newArtists = generateArtists(400);

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(ARTIST_SYNC_KEY)).thenReturn(syncKey);
        when(limitProperties.getSpotify()).thenReturn(600);
        when(artistSyncTaskManager.getAll()).thenReturn(existingTasks);
        when(artistService.findToSync(syncKey, 400)).thenReturn(newArtists);
        producer.produce();
        List<Task<ArtistTaskData>> tasks = new ArrayList<>(existingTasks);
        tasks.addAll(newTasks);
        verify(valueOperations, never()).set(eq(ARTIST_SYNC_KEY), any(String.class));
        verify(artistSyncTaskSender, times(1)).send(captor.capture());

        List<Task<ArtistTaskData>> argumentTasks = captor.getValue();
        Assertions.assertThat(tasks.stream().map(Task::getData).map(ArtistTaskData::getArtistId).toList())
                .containsExactlyInAnyOrder(argumentTasks.stream().map(Task::getData).map(ArtistTaskData::getArtistId).toArray(String[]::new));
    }

    @Test
    public void produce_WhenRetrievedSyncKeyIsNull_GenerateAndUseNewSyncKey() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(ARTIST_SYNC_KEY)).thenReturn(null);
        when(limitProperties.getSpotify()).thenReturn(600);
        when(artistSyncTaskManager.getAll()).thenReturn(generateTasks("artist", 400));
        when(artistService.findToSync(any(String.class), eq(200))).thenReturn(generateArtists(10));
        producer.produce();
        verify(valueOperations, times(1)).set(eq(ARTIST_SYNC_KEY), any(String.class));
    }

    private List<Task<ArtistTaskData>> generateTasks(String prefix, int count) {
        return IntStream.range(0, count).mapToObj(idx -> {
            ArtistTaskData artistTaskData = ArtistTaskData.fromArtistId(prefix + idx, TaskType.SYNC);
            return new Task<>(artistTaskData);
        }).toList();
    }

    private List<Artist> generateArtists(int count) {
        return IntStream.range(0, count)
                .mapToObj(idx -> new Artist("artist" + idx, "Artist Name" + idx, null, 90))
                .toList();
    }
}