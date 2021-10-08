package com.kushtrimh.tomorr.sync.send;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class ArtistSyncRabbitTaskSender implements ArtistSyncTaskSender {

    @Override
    public void send(List<Task<ArtistTaskData>> tasks) {
        // TODO: Implement to send tasks to RabbitMQ with delay
    }
}
