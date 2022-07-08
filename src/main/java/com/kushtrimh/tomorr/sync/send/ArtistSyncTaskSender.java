package com.kushtrimh.tomorr.sync.send;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Kushtrim Hajrizi
 */
@FunctionalInterface
public interface ArtistSyncTaskSender {

    List<CompletableFuture<Void>> send(List<Task<ArtistTaskData>> tasks);
}
