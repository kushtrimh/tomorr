package com.kushtrimh.tomorr.spotify.limit;

/**
 * @author Kushtrim Hajrizi
 */
public interface RequestLimitService {

    boolean canSendRequest();

    // TODO: Add tests for cant send request
    boolean cantSendRequest();

    int getRemainingRequestLimit();

    int getSentRequestsCounter();

    long increment();

    void reset();
}
