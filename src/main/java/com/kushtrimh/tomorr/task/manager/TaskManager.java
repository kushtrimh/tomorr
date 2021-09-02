package com.kushtrimh.tomorr.task.manager;

import com.kushtrimh.tomorr.task.data.TaskData;

/**
 * @author Kushtrim Hajrizi
 */
public interface TaskManager {

    <T extends TaskData> void create(T data);

    int getQueuedTasksCount();
}
