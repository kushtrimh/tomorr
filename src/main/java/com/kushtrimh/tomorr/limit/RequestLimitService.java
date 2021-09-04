package com.kushtrimh.tomorr.limit;

/**
 * @author Kushtrim Hajrizi
 */
public interface RequestLimitService {

    boolean canSendRequest(LimitType limitType);

    boolean cantSendRequest(LimitType limitType);

    int getRemainingRequestLimit(LimitType limitType);

    int getSentRequestsCounter(LimitType limitType);

    void increment(LimitType limitType);

    /**
     * Checks if request can be sent for the given limit type, and increases the request counter
     * for the limit type if request can be sent. If not limit is not increased.
     * @param limitType
     * @return true if request can be sent, false otherwise
     */
    boolean tryFor(LimitType limitType);

    void reset(LimitType limitType);

    void resetAll();
}
