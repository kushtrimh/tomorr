package com.kushtrimh.tomorr.sync.send;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
@FunctionalInterface
public interface ArtistSyncTaskSender {

    void send(List<Task<ArtistTaskData>> tasks);
}
