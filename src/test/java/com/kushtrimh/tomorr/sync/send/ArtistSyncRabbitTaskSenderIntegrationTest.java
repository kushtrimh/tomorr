package com.kushtrimh.tomorr.sync.send;

import com.kushtrimh.tomorr.configuration.TestRabbitMQConfiguration;
import com.kushtrimh.tomorr.extension.TestRabbitMQExtension;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = {@Tag("rabbitmq"), @Tag("integration")})
@ContextConfiguration(classes = {TestRabbitMQConfiguration.class})
@ExtendWith({SpringExtension.class, TestRabbitMQExtension.class})
class ArtistSyncRabbitTaskSenderIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ArtistSyncRabbitTaskSender artistSyncRabbitTaskSender;

    @BeforeEach
    public void init() {
        artistSyncRabbitTaskSender = new ArtistSyncRabbitTaskSender(rabbitTemplate);
    }

    @Test
    public void send_WhenTasksAreProvided_SendToRabbitMQ() {
        List<Task<ArtistTaskData>> tasks = generateTasks(10);
        assertDoesNotThrow(() -> artistSyncRabbitTaskSender.send(tasks));
    }

    private List<Task<ArtistTaskData>> generateTasks(int count) {
        int half = (int) Math.floor(count / 2.0);
        var artistIdTasks = IntStream.range(0, half).mapToObj(i -> {
            var artistTaskData = ArtistTaskData.fromArtistId("artist-" + i, TaskType.SYNC);
            return new Task<>(artistTaskData);
        });
        var nextNodeTasks = IntStream.range(0, half).mapToObj(i -> {
            var artistTaskData = ArtistTaskData.fromNextNode("artist-" + i, "http://localhost:2424/next/node" + i, TaskType.INITIAL_SYNC);
            return new Task<>(artistTaskData);
        });
        return Stream.concat(artistIdTasks, nextNodeTasks).toList();
    }
}