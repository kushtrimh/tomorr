package com.kushtrimh.tomorr.sync.execute;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;

/**
 * @author Kushtrim Hajrizi
 */
@FunctionalInterface
public interface ArtistSyncTaskExecutor {

    void execute(Task<ArtistTaskData> task);
}
