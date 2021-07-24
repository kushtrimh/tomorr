package com.kushtrimh.tomorr.spotify.limit;

/**
 * @author Kushtrim Hajrizi
 */
public interface RequestLimitService {

    boolean canSendRequest();

    boolean cantSendRequest();

    int getRemainingRequestLimit();

    int getSentRequestsCounter();

    long increment();

    void reset();
}
