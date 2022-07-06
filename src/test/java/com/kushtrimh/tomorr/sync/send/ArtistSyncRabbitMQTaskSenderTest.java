package com.kushtrimh.tomorr.sync.send;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collections;
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
        int count = 323;

        var tasks = IntStream.range(0, count).mapToObj(i -> {
            var artistTask = ArtistTaskData.fromArtistId("artist" + i, TaskType.SYNC);
            return new Task<>(artistTask);
        }).toList();
        var messages = IntStream.range(0, count).mapToObj(i -> {
            Message message = mock(Message.class);
            lenient().when(message.getMessageProperties()).thenReturn(mock(MessageProperties.class));
            return message;
        }).toList();

        for (int i = 0; i < count; i++) {
            var task = tasks.get(i);
            var message = messages.get(i);
            doAnswer(invocation -> {
                MessagePostProcessor messagePostProcessor = invocation.getArgument(1, MessagePostProcessor.class);
                messagePostProcessor.postProcessMessage(message);
                return null;
            }).when(rabbitTemplate).convertAndSend(eq(task), any(MessagePostProcessor.class));
        }

        artistSyncRabbitMQTaskSender.send(tasks);

        var tasksPerTime = (int) Math.ceil(count / 60.0);
        for (int i = 0; i < count; i++) {
            var message = messages.get(i);
            verify(message.getMessageProperties(), times(1)).setDelay((int) (Math.floor((i * 1000.0 / tasksPerTime) / 1000.0) * 1000.0));
        }
    }
}