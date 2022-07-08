package com.kushtrimh.tomorr.sync.send;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({MockitoExtension.class})
class ArtistSyncRabbitMQTaskSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private ArtistSyncRabbitMQTaskSender artistSyncRabbitMQTaskSender;

    @BeforeEach
    public void init() {
        artistSyncRabbitMQTaskSender = new ArtistSyncRabbitMQTaskSender(rabbitTemplate);
    }

    @Test
    public void send_WhenThereAreNoTasks_DoNotSendAnythingToRabbitMQ() {
        artistSyncRabbitMQTaskSender.send(Collections.emptyList());
        verify(rabbitTemplate, never()).convertAndSend(any(Object.class), any(MessagePostProcessor.class));
    }

    @Test
    public void send_WhenThereAreTasks_SendTasksWithDelay() {
        int count = 10;

        var tasks = IntStream.range(0, count).mapToObj(i -> {
            var artistTask = ArtistTaskData.fromArtistId("artist" + i, TaskType.SYNC);
            return new Task<>(artistTask);
        }).toList();

        List<CompletableFuture<Void>> futures = artistSyncRabbitMQTaskSender.send(tasks);
        futures.forEach(CompletableFuture::join);

        verify(rabbitTemplate, times(count)).convertAndSend(any(Task.class));
    }
}