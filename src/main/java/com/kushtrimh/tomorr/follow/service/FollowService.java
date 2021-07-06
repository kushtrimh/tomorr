package com.kushtrimh.tomorr.follow.service;

import com.kushtrimh.tomorr.user.User;

/**
 * @author Kushtrim Hajrizi
 */
public interface FollowService {

    void follow(User user, String artistId);
}
