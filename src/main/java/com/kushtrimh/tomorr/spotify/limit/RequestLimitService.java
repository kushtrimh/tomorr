package com.kushtrimh.tomorr.spotify.limit;

/**
 * @author Kushtrim Hajrizi
 */
public interface RequestLimitService {

    boolean canSendRequest(LimitType limitType);

    boolean cantSendRequest(LimitType limitType);

    int getRemainingRequestLimit(LimitType limitType);

    int getSentRequestsCounter(LimitType limitType);

    long increment(LimitType limitType);

    void reset(LimitType limitType);

    // TODO: Add support for reset all
}
