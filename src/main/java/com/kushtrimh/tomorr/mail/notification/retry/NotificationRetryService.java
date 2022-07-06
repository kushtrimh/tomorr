package com.kushtrimh.tomorr.mail.notification.retry;

/**
 * @author Kushtrim Hajrizi
 */
public interface NotificationRetryService {

    void retryNotification(NotificationRetryData notificationRetryData);
}
