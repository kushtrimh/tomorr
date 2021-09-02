package com.kushtrimh.tomorr.task.manager;

import com.kushtrimh.tomorr.limit.RequestLimitService;
import com.kushtrimh.tomorr.task.data.TaskData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class DefaultTaskManager implements TaskManager {

    private final StringRedisTemplate stringRedisTemplate;
    private final RequestLimitService requestLimitService;

    public DefaultTaskManager(StringRedisTemplate stringRedisTemplate, RequestLimitService requestLimitService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.requestLimitService = requestLimitService;
    }

    @Override
    public <T extends TaskData> void create(T data) {

    }

    @Override
    public int getQueuedTasksCount() {
        return 0;
    }
}
