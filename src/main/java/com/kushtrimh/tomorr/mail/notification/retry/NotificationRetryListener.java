package com.kushtrimh.tomorr.mail.notification.retry;

import com.kushtrimh.tomorr.task.Task;

/**
 * @author Kushtrim Hajrizi
 */
public interface NotificationRetryListener {

    void handleNotificationRetry(Task<NotificationRetryData> task);
}
