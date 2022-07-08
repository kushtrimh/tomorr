package com.kushtrimh.tomorr.mail.notification.retry;

import java.util.concurrent.CompletableFuture;

/**
 * @author Kushtrim Hajrizi
 */
public interface NotificationRetryService {

    CompletableFuture<Void> retryNotification(NotificationRetryData notificationRetryData);
}
