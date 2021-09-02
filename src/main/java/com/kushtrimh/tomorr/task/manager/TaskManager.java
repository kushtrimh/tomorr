package com.kushtrimh.tomorr.task.manager;

import com.kushtrimh.tomorr.task.data.TaskData;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
public interface TaskManager<T extends TaskData> {

    void create(T data);

    void create(List<T> dataList);

    long getQueuedTasksCount();
}
