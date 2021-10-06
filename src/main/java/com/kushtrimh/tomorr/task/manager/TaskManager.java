package com.kushtrimh.tomorr.task.manager;

import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.data.TaskData;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
public interface TaskManager<T extends TaskData> {

    void add(T data);

    void add(List<T> dataList);

    long getQueuedTasksCount();

    List<Task<T>> getAll();
}
