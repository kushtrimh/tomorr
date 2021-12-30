package com.kushtrimh.tomorr.sync.execute;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class DefaultArtistSyncTaskExecutor implements ArtistSyncTaskExecutor {

    private final Logger logger = LoggerFactory.getLogger(DefaultArtistSyncTaskExecutor.class);

    @Override
    @RabbitListener(queues = "artistSync")
    public void execute(Task<ArtistTaskData> task) {
        logger.info("Received {}", task);
    }
}
