package com.kushtrimh.tomorr.sync.send;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class ArtistSyncRabbitMQTaskSender implements ArtistSyncTaskSender {

    private final RabbitTemplate artistTaskRabbitTemplate;

    public ArtistSyncRabbitMQTaskSender(RabbitTemplate artistTaskRabbitTemplate) {
        this.artistTaskRabbitTemplate = artistTaskRabbitTemplate;
    }

    @Override
    public List<CompletableFuture<Void>> send(List<Task<ArtistTaskData>> tasks) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        var numberOfTasks = tasks.size();
        var tasksPerSecond = Math.ceil(numberOfTasks / 60.0);
        var delay = 0;
        for (int i = 0; i < numberOfTasks; i++) {
            var task = tasks.get(i);
            if (i != 0 && i % tasksPerSecond == 0) {
                delay += 1000;
            }
            var messageDelay = delay;
            futures.add(CompletableFuture.runAsync(() -> artistTaskRabbitTemplate.convertAndSend(task),
                    CompletableFuture.delayedExecutor(messageDelay, TimeUnit.MILLISECONDS)));
        }
        return futures;
    }
}
