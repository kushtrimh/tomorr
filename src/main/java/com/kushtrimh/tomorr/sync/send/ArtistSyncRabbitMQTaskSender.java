package com.kushtrimh.tomorr.sync.send;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public void send(List<Task<ArtistTaskData>> tasks) {
        var numberOfTasks = tasks.size();
        var tasksPerSecond = Math.ceil(numberOfTasks / 60.0);
        var delay = 0;
        for (int i = 0; i < numberOfTasks; i++) {
            var task = tasks.get(i);
            if (i != 0 && i % tasksPerSecond == 0) {
                delay += 1000;
            }
            var messageDelay = delay;
            artistTaskRabbitTemplate.convertAndSend(task, message -> {
                message.getMessageProperties().setDelay(messageDelay);
                return message;
            });
        }
    }
}
